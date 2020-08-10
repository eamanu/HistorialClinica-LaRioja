package net.pladema.pdf.service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import net.pladema.clinichistory.documents.repository.DocumentFileRepository;
import net.pladema.clinichistory.documents.repository.entity.DocumentFile;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.documents.service.ReportDocumentService;
import net.pladema.clinichistory.hospitalization.controller.dto.ResponsibleDoctorDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.VitalSignsReportDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.VitalSignMapper;
import net.pladema.clinichistory.hospitalization.controller.mapper.ResponsibleDoctorMapper;
import net.pladema.flavor.service.FlavorService;
import net.pladema.patient.controller.service.PatientExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Locale;

@Component
public class PdfService {

    private static final Logger LOG = LoggerFactory.getLogger(PdfService.class);

    public static final String OUTPUT = "Output -> {}";

    private final String flavor;

    private final SpringTemplateEngine templateEngine;

    private final StreamFile streamFile;

    private final PatientExternalService patientExternalService;

    private final ReportDocumentService reportDocumentService;

    private final ResponsibleDoctorMapper responsibleDoctorMapper;

    private final VitalSignMapper vitalSignMapper;

    private final DocumentFileRepository documentFileRepository;

    public PdfService(
            FlavorService flavorService,
            SpringTemplateEngine templateEngine,
            StreamFile streamFile,
            PatientExternalService patientExternalService,
            ReportDocumentService reportDocumentService,
            ResponsibleDoctorMapper responsibleDoctorMapper,
            VitalSignMapper vitalSignMapper,
            DocumentFileRepository documentFileRepository) {
        super();
        this.flavor = flavorService.getFlavor().toString();
        this.templateEngine = templateEngine;
        this.streamFile = streamFile;
        this.patientExternalService = patientExternalService;
        this.reportDocumentService = reportDocumentService;
        this.responsibleDoctorMapper = responsibleDoctorMapper;
        this.vitalSignMapper = vitalSignMapper;
        this.documentFileRepository = documentFileRepository;
    }

    public ResponseEntity<InputStreamResource> getResponseEntityPdf(String name, String fileName,Context ctx)
            throws IOException, DocumentException {
        LOG.debug("Input parameters -> name {}, fileName {}, ctx {}",
                name, fileName, ctx);
        String templateName = fileName + "-" + flavor;
        ByteArrayOutputStream writer = writer(templateName, ctx);
        return reader(writer, streamFile.buildDownloadName(name));
    }

    private ByteArrayOutputStream writer(String templateName, Context ctx) throws IOException, DocumentException {
        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, os);
            document.open();
            String templateNameWithFlavor = templateName+"-"+flavor;
            InputStream is = new ByteArrayInputStream(templateEngine.process(templateNameWithFlavor, ctx).getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            document.close();
            writer.close();
            return os;
        } catch (IOException e){
            throw new IOException(e);
        }
        catch(DocumentException e) {
            throw new DocumentException(e);
        }
    }

    private ResponseEntity<InputStreamResource> reader(ByteArrayOutputStream os, String name) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
        ResponseEntity<InputStreamResource> response;
        response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + name)
                .contentType(MediaType.APPLICATION_PDF).contentLength(os.size()).body(resource);
        return response;
    }

    @Async
    @EventListener
    public void loadDocument(OnGenerateDocumentEvent event) throws IOException, DocumentException {
        LOG.debug("Input parameters -> onGenerateDocumentEvent {}", event);
        if(event.getDocument().isConfirmed()) {
            Context context = buildContext(event.getDocument(), event.getPatientId());
            ByteArrayOutputStream writer = writer(event.getTemplateName(), context);
            String path = streamFile.buildPath(event.getRelativeDirectory());
            String realFileName = event.getUuid();
            String fictitiousFileName = streamFile.buildDownloadName(event.getDocument().getId(), event.getDocumentType());

            try {
                boolean loaded = streamFile.loadFileInDirectory(path, writer);
                String checksum = getHash(path);

                if (loaded)
                    documentFileRepository.save(new DocumentFile(
                            event.getDocument().getId(),
                            event.getSourceId(),
                            event.getSourceType(),
                            event.getDocumentTypeId(), path, fictitiousFileName, realFileName, checksum));
            } catch (IOException ex) {
                throw new IOException(ex);
            }
        }
    }

    private String getHash(String path) {
        LOG.debug("Input parameters -> path {}", path);
        String result;
        String algorithm = "SHA-256";
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] sha256Hash = md.digest(Files.readAllBytes(Paths.get(path)));
            result = Base64.getEncoder().encodeToString(sha256Hash);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Algorithm doesn't exist -> {} ",algorithm);
            result = null;
        }
        catch (IOException e) {
            LOG.error("Error with path file {} ", path, e);
            result = null;
        }
        LOG.debug(OUTPUT, result);
        return result;
    }

    public <T extends Document> Context buildContext(T document, Integer patientId){
        LOG.debug("Input parameters -> document {}", document);

        ResponsibleDoctorDto author = responsibleDoctorMapper.toResponsibleDoctorDto(
                reportDocumentService.getAuthor(document.getId()));

        VitalSignsReportDto vitalSignsReportDto = vitalSignMapper.toVitalSignsReportDto(document.getVitalSigns());
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("patient", patientExternalService.getBasicDataFromPatient(patientId));
        ctx.setVariable("mainDiagnosis", document.getMainDiagnosis());
        ctx.setVariable("diagnosis", document.getDiagnosis());
        ctx.setVariable("reasons", document.getReasons());
        ctx.setVariable("procedures", document.getProcedures());
        ctx.setVariable("problems", document.getProblems());
        ctx.setVariable("personalHistories", document.getPersonalHistories());
        ctx.setVariable("familyHistories", document.getFamilyHistories());
        ctx.setVariable("allergies", document.getAllergies());
        ctx.setVariable("immunizations", document.getImmunizations());
        ctx.setVariable("medications", document.getMedications());
        ctx.setVariable("anthropometricData", document.getAnthropometricData());
        ctx.setVariable("vitalSigns", vitalSignsReportDto);
        ctx.setVariable("notes", document.getNotes());
        ctx.setVariable("author", author);
        LOG.debug(OUTPUT, ctx);
        return ctx;
    }
}
