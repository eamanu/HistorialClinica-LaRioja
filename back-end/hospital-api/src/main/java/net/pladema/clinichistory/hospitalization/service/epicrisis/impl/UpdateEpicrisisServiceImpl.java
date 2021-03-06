package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.*;
import net.pladema.clinichistory.documents.service.ips.domain.DocumentObservationsBo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.UpdateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateEpicrisisServiceImpl implements UpdateEpicrisisService {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateEpicrisisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final InternmentEpisodeService internmentEpisodeService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final CreateMedicationService createMedicationService;

    private final ClinicalObservationService clinicalObservationService;

    private final ImmunizationService immunizationService;


    public UpdateEpicrisisServiceImpl(DocumentService documentService,
                                      InternmentEpisodeService internmentEpisodeService,
                                      NoteService noteService,
                                      HealthConditionService healthConditionService,
                                      AllergyService allergyService,
                                      CreateMedicationService createMedicationService,
                                      ClinicalObservationService clinicalObservationService,
                                      ImmunizationService immunizationService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.createMedicationService = createMedicationService;
        this.clinicalObservationService = clinicalObservationService;
        this.immunizationService = immunizationService;
    }

    @Override
    public EpicrisisBo updateDocument(Integer internmentEpisodeId, PatientInfoBo patientInfo, EpicrisisBo epicrisis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientInfo {}, epicrisis {}", internmentEpisodeId, patientInfo, epicrisis);

        Optional<Document> optDoc = documentService.findById(epicrisis.getId());
        optDoc.ifPresent(doc -> {
            deleteDocumentData(epicrisis.getId());
            loadNotes(doc, Optional.ofNullable(epicrisis.getNotes()));
            doc = documentService.save(doc);

            epicrisis.setDiagnosis(healthConditionService.loadDiagnosis(patientInfo, doc.getId(), epicrisis.getDiagnosis()));
            epicrisis.setPersonalHistories(healthConditionService.loadPersonalHistories(patientInfo, doc.getId(), epicrisis.getPersonalHistories()));
            epicrisis.setFamilyHistories(healthConditionService.loadFamilyHistories(patientInfo, doc.getId(), epicrisis.getFamilyHistories()));
            epicrisis.setAllergies(allergyService.loadAllergies(patientInfo, doc.getId(), epicrisis.getAllergies()));
            epicrisis.setImmunizations(immunizationService.loadImmunization(patientInfo, doc.getId(), epicrisis.getImmunizations()));
            epicrisis.setMedications(createMedicationService.execute(patientInfo, doc.getId(), epicrisis.getMedications()));

            epicrisis.setVitalSigns(clinicalObservationService.loadVitalSigns(patientInfo, doc.getId(), Optional.ofNullable(epicrisis.getVitalSigns())));
            epicrisis.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientInfo, doc.getId(), Optional.ofNullable(epicrisis.getAnthropometricData())));

            internmentEpisodeService.updateEpicrisisDocumentId(internmentEpisodeId, doc.getId());
            epicrisis.setId(doc.getId());

            LOG.debug(OUTPUT, epicrisis);
        });
        return epicrisis;
    }

    private Document loadNotes(Document document, Optional<DocumentObservationsBo> optNotes) {
        LOG.debug("Input parameters -> document {}, notes {}", document, optNotes);
        optNotes.ifPresent(notes -> {
            document.setCurrentIllnessNoteId(noteService.createNote(notes.getCurrentIllnessNote()));
            document.setPhysicalExamNoteId(noteService.createNote(notes.getPhysicalExamNote()));
            document.setStudiesSummaryNoteId(noteService.createNote(notes.getStudiesSummaryNote()));
            document.setEvolutionNoteId(noteService.createNote(notes.getEvolutionNote()));
            document.setClinicalImpressionNoteId(noteService.createNote(notes.getClinicalImpressionNote()));
            document.setOtherNoteId(noteService.createNote(notes.getOtherNote()));
        });
        LOG.debug(OUTPUT, document);
        return document;
    }

    private void deleteDocumentData(Long documentId) {
        LOG.debug("Input parameters -> documentId {}", documentId);
        documentService.deleteHealthConditionHistory(documentId);
        documentService.deleteAllergiesHistory(documentId);
        documentService.deleteImmunizationsHistory(documentId);
        documentService.deleteMedicationsHistory(documentId);
        documentService.deleteObservationsVitalSignsHistory(documentId);
        documentService.deleteObservationsLabHistory(documentId);
    }

    private void deleteNotes(Document anamnesisDocument) {
        LOG.debug("Input parameters -> Document {}", anamnesisDocument);
        List<Long> notesToDelete = new ArrayList<>();
        if (anamnesisDocument.getClinicalImpressionNoteId() != null)
            notesToDelete.add(anamnesisDocument.getClinicalImpressionNoteId());
        if (anamnesisDocument.getCurrentIllnessNoteId() != null)
            notesToDelete.add(anamnesisDocument.getCurrentIllnessNoteId());
        if (anamnesisDocument.getEvolutionNoteId() != null)
            notesToDelete.add(anamnesisDocument.getEvolutionNoteId());
        if (anamnesisDocument.getIndicationsNoteId() != null)
            notesToDelete.add(anamnesisDocument.getIndicationsNoteId());
        if (anamnesisDocument.getPhysicalExamNoteId() != null)
            notesToDelete.add(anamnesisDocument.getPhysicalExamNoteId());
        if (anamnesisDocument.getStudiesSummaryNoteId() != null)
            notesToDelete.add(anamnesisDocument.getStudiesSummaryNoteId());
        if (anamnesisDocument.getOtherNoteId() != null)
            notesToDelete.add(anamnesisDocument.getOtherNoteId());
        noteService.deleteAllNotes(notesToDelete);
    }



}
