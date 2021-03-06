package net.pladema.clinichistory.hospitalization.service.maindiagnoses;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.documents.service.generalstate.HealthConditionGeneralStateService;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.clinichistory.documents.service.ips.HealthConditionService;
import net.pladema.clinichistory.documents.service.ips.domain.DiagnosisBo;
import net.pladema.clinichistory.documents.service.ips.domain.DocumentObservationsBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class MainDiagnosesServiceImpl implements MainDiagnosesService {

    private static final Logger LOG = LoggerFactory.getLogger(MainDiagnosesServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final InternmentEpisodeService internmentEpisodeService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final HealthConditionGeneralStateService healthConditionGeneralStateService;

    public MainDiagnosesServiceImpl(DocumentService documentService,
                                    InternmentEpisodeService internmentEpisodeService,
                                    NoteService noteService,
                                    HealthConditionService healthConditionService,
                                    HealthConditionGeneralStateService healthConditionGeneralStateService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.healthConditionGeneralStateService = healthConditionGeneralStateService;
    }

    @Override
    public Long createDocument(Integer internmentEpisodeId, PatientInfoBo patientInfo, MainDiagnosisBo mainDiagnosisBo) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientInfo {}, mainDiagnosisBo {}", internmentEpisodeId, patientInfo, mainDiagnosisBo);

        Document document = new Document(internmentEpisodeId, DocumentStatus.FINAL, DocumentType.EVALUATION_NOTE, SourceType.HOSPITALIZATION);
        loadNotes(document, Optional.ofNullable(mainDiagnosisBo.getNotes()));
        document = documentService.save(document);
        Long result = document.getId();

        mainDiagnosisBo.getMainDiagnosis().setVerificationId(ConditionVerificationStatus.CONFIRMED); 

        HealthConditionBo currentMainDiagnose = healthConditionGeneralStateService.getMainDiagnosisGeneralState(internmentEpisodeId);
        if (!currentMainDiagnose.getSnomed().equals(mainDiagnosisBo.getMainDiagnosis().getSnomed()))
            downgradeToAlternativeDiagnose(patientInfo, document.getId(), currentMainDiagnose);
        healthConditionService.loadMainDiagnosis(patientInfo, result, Optional.ofNullable(mainDiagnosisBo.getMainDiagnosis()));

        internmentEpisodeService.addEvolutionNote(internmentEpisodeId, document.getId());

        LOG.debug(OUTPUT, result);
        return result;
    }

    private void downgradeToAlternativeDiagnose(PatientInfoBo patientInfo, Long docId, HealthConditionBo currentMainDiagnose) {
        LOG.debug("Input parameters -> patientInfo {}, docId {}, currentMainDiagnose {}", patientInfo, docId, currentMainDiagnose);
        DiagnosisBo diagnosisBo = new DiagnosisBo();
        diagnosisBo.setMain(false);
        diagnosisBo.setSnomed(currentMainDiagnose.getSnomed());
        diagnosisBo.setStatus(currentMainDiagnose.getStatus());
        diagnosisBo.setStatusId(currentMainDiagnose.getStatusId());
        diagnosisBo.setVerification(currentMainDiagnose.getVerification());
        diagnosisBo.setVerificationId(currentMainDiagnose.getVerificationId());
        healthConditionService.loadDiagnosis(patientInfo, docId, Arrays.asList(diagnosisBo));
    }

    private Document loadNotes(Document evolutionNote, Optional<DocumentObservationsBo> optNotes) {
        LOG.debug("Input parameters -> evolutionNote {}, notes {}", evolutionNote, optNotes);
        optNotes.ifPresent(notes -> {
            evolutionNote.setCurrentIllnessNoteId(noteService.createNote(notes.getCurrentIllnessNote()));
            evolutionNote.setPhysicalExamNoteId(noteService.createNote(notes.getPhysicalExamNote()));
            evolutionNote.setStudiesSummaryNoteId(noteService.createNote(notes.getStudiesSummaryNote()));
            evolutionNote.setEvolutionNoteId(noteService.createNote(notes.getEvolutionNote()));
            evolutionNote.setClinicalImpressionNoteId(noteService.createNote(notes.getClinicalImpressionNote()));
            evolutionNote.setOtherNoteId(noteService.createNote(notes.getOtherNote()));
        });
        LOG.debug(OUTPUT, evolutionNote);
        return evolutionNote;
    }


}
