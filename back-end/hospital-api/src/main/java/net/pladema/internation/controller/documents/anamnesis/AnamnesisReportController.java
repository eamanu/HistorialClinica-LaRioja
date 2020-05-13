package net.pladema.internation.controller.documents.anamnesis;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.internment.dto.ResponsibleDoctorDto;
import net.pladema.internation.controller.internment.mapper.ResponsibleDoctorMapper;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.ReportDocumentService;
import net.pladema.internation.service.documents.anamnesis.AnamnesisReportService;
import net.pladema.internation.service.documents.anamnesis.domain.Anamnesis;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.pdf.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/anamnesis-report")
@Api(value = "Anamnesis Report", tags = { "Anamnesis Report" })
@Validated
public class AnamnesisReportController {

    private static final Logger LOG = LoggerFactory.getLogger(AnamnesisReportController.class);

    public static final String OUTPUT = "Output -> {}";
    public static final String INVALID_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final AnamnesisReportService anamnesisReportService;

    private final PatientExternalService patientExternalService;

    private final PdfService pdfService;

    private final ReportDocumentService reportDocumentService;

    private final ResponsibleDoctorMapper responsibleDoctorMapper;

    public AnamnesisReportController(InternmentEpisodeService internmentEpisodeService,
                                     AnamnesisReportService anamnesisReportService,
                                     PatientExternalService patientExternalService,
                                     PdfService pdfService,
                                     ReportDocumentService reportDocumentService,
                                     ResponsibleDoctorMapper responsibleDoctorMapper) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.anamnesisReportService = anamnesisReportService;
        this.patientExternalService = patientExternalService;
        this.pdfService = pdfService;
        this.reportDocumentService = reportDocumentService;
        this.responsibleDoctorMapper = responsibleDoctorMapper;
    }


    @GetMapping("/{anamnesisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.ANAMNESIS)
    public ResponseEntity<InputStreamResource> getPDF(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}",
                institutionId, internmentEpisodeId, anamnesisId);
        Anamnesis anamnesis = anamnesisReportService.getDocument(anamnesisId);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        BasicPatientDto patientData = patientExternalService.getBasicDataFromPatient(patientId);
        ResponsibleDoctorDto author = responsibleDoctorMapper.toResponsibleDoctorDto(
                reportDocumentService.getAuthor(anamnesisId));
        Context ctx = createAnamnesisContext(anamnesis, patientData, author);
        String name = "Anamnesis_" + anamnesis.getId();
        return pdfService.getResponseEntityPdf(name, "anamnesis", LocalDateTime.now(), ctx);
    }

    private Context createAnamnesisContext(Anamnesis anamnesis, BasicPatientDto patientData, ResponsibleDoctorDto author ) {
        LOG.debug("Input parameters -> anamnesis {}", anamnesis);
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("patient", patientData);
        ctx.setVariable("mainDiagnosis", anamnesis.getMainDiagnosis());
        ctx.setVariable("diagnosis", anamnesis.getDiagnosis());
        ctx.setVariable("personalHistories", anamnesis.getPersonalHistories());
        ctx.setVariable("familyHistories", anamnesis.getFamilyHistories());
        ctx.setVariable("allergies", anamnesis.getAllergies());
        ctx.setVariable("inmunizations", anamnesis.getInmunizations());
        ctx.setVariable("medications", anamnesis.getMedications());
        ctx.setVariable("anthropometricData", anamnesis.getAnthropometricData());
        ctx.setVariable("vitalSigns", anamnesis.getVitalSigns());
        ctx.setVariable("notes", anamnesis.getNotes());
        ctx.setVariable("author", author);
        LOG.debug(OUTPUT, ctx);
        return ctx;
    }

}