package net.pladema.clinichistory.hospitalization.controller.maindiagnoses;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import net.pladema.clinichistory.documents.events.OnGenerateInternmentDocumentEvent;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.constraints.EvolutionNoteValid;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.HealthConditionMapper;
import net.pladema.clinichistory.hospitalization.controller.maindiagnoses.dto.MainDiagnosisDto;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteReportService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.MainDiagnosesService;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.ips.repository.masterdata.entity.EDocumentType;
import net.pladema.pdf.service.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/main-diagnoses")
@Api(value = "Main diagnoses", tags = { "Main diagnoses" })
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
@Validated
public class MainDiagnosesController {

    private static final Logger LOG = LoggerFactory.getLogger(MainDiagnosesController.class);

    public static final String OUTPUT = "Output -> {}";
    
    public static final String INVALID_INTERNMENT_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final MainDiagnosesService mainDiagnosesService;

    private final EvolutionNoteReportService evolutionNoteReportService;

    private final HealthConditionMapper healthConditionMapper;

    private final PdfService pdfService;

    public MainDiagnosesController(InternmentEpisodeService internmentEpisodeService,
                                   MainDiagnosesService mainDiagnosesService,
                                   EvolutionNoteReportService evolutionNoteReportService,
                                   HealthConditionMapper healthConditionMapper,
                                   PdfService pdfService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.mainDiagnosesService = mainDiagnosesService;
        this.evolutionNoteReportService = evolutionNoteReportService;
        this.healthConditionMapper = healthConditionMapper;
        this.pdfService = pdfService;
    }

    @PostMapping
    @Transactional
    @InternmentValid
    @EvolutionNoteValid
    public ResponseEntity<Long> addMainDiagnosis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid MainDiagnosisDto mainDiagnosis) throws IOException, DocumentException{
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, mainDiagnosis {}",
                institutionId, internmentEpisodeId, mainDiagnosis);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_INTERNMENT_EPISODE));

        MainDiagnosisBo mainDiagnoseBo = healthConditionMapper.fromMainDiagnoseDto(mainDiagnosis);
        Long documentId = mainDiagnosesService.createDocument(internmentEpisodeId, patientId, mainDiagnoseBo);

        EvolutionNoteBo evolutionNoteResult = evolutionNoteReportService.getDocument(documentId);
        generateDocument(evolutionNoteResult, institutionId, internmentEpisodeId, patientId);
        Long result = evolutionNoteResult.getId();
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    private void generateDocument(Document evolutionNote, Integer institutionId, Integer internmentEpisodeId,
                                  Integer patientId) throws IOException, DocumentException {

        LOG.debug("Input parameters -> evolutionNote {}, institutionId {}, internmentEpisodeId {} , patientId {}",
                evolutionNote, institutionId, internmentEpisodeId, patientId);
        OnGenerateDocumentEvent event = new OnGenerateInternmentDocumentEvent(evolutionNote, institutionId, internmentEpisodeId,
                EDocumentType.map(DocumentType.EVALUATION_NOTE), patientId);
        pdfService.loadDocument(event);
    }


}