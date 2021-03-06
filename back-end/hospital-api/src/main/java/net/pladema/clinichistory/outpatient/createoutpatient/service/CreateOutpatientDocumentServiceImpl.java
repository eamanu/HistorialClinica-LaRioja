package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.*;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateOutpatientDocumentServiceImpl implements CreateOutpatientDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOutpatientDocumentServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final UpdateOutpatientConsultationService updateOutpatientConsultationService;

    private final HealthConditionService healthConditionService;

    private final ProceduresService proceduresService;

    private final AllergyService allergyService;

    private final CreateMedicationService createMedicationService;

    private final ImmunizationService immunizationService;

    private final ClinicalObservationService clinicalObservationService;

    private final NoteService noteService;

    public CreateOutpatientDocumentServiceImpl(DocumentService documentService,
                                               UpdateOutpatientConsultationService updateOutpatientConsultationService,
                                               HealthConditionService healthConditionService,
                                               ProceduresService proceduresService, AllergyService allergyService,
                                               CreateMedicationService createMedicationService,
                                               ImmunizationService immunizationService, ClinicalObservationService clinicalObservationService,
                                               NoteService noteService) {
        this.documentService = documentService;
        this.updateOutpatientConsultationService = updateOutpatientConsultationService;
        this.healthConditionService = healthConditionService;
        this.proceduresService = proceduresService;
        this.allergyService = allergyService;
        this.createMedicationService = createMedicationService;
        this.immunizationService = immunizationService;
        this.clinicalObservationService = clinicalObservationService;
        this.noteService = noteService;
    }


    @Override
    public OutpatientDocumentBo create(Integer outpatientId, PatientInfoBo patientInfo, OutpatientDocumentBo outpatient) {
        LOG.debug("Input parameters -> outpatientId {}, patientInfo {}, outpatient {}", outpatientId, patientInfo, outpatient);
        Document doc = new Document(outpatientId, DocumentStatus.FINAL, DocumentType.OUTPATIENT, SourceType.OUTPATIENT);
        loadNotes(doc, Optional.ofNullable(outpatient.getEvolutionNote()));
        doc = documentService.save(doc);

        outpatient.setProblems(healthConditionService.loadProblems(patientInfo, doc.getId(), outpatient.getProblems()));
        outpatient.setProcedures(proceduresService.loadProcedures(patientInfo, doc.getId(), outpatient.getProcedures()));
        outpatient.setFamilyHistories(healthConditionService.loadFamilyHistories(patientInfo, doc.getId(), outpatient.getFamilyHistories()));
        outpatient.setMedications(createMedicationService.execute(patientInfo, doc.getId(), outpatient.getMedications()));
        outpatient.setAllergies(allergyService.loadAllergies(patientInfo, doc.getId(), outpatient.getAllergies()));
        outpatient.setImmunizations(immunizationService.loadImmunization(patientInfo, doc.getId(), outpatient.getImmunizations()));

        outpatient.setVitalSigns(clinicalObservationService.loadVitalSigns(patientInfo, doc.getId(), Optional.ofNullable(outpatient.getVitalSigns())));
        outpatient.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientInfo, doc.getId(), Optional.ofNullable(outpatient.getAnthropometricData())));

        updateOutpatientConsultationService.updateOutpatientDocId(outpatientId, doc.getId());
        outpatient.setId(doc.getId());
        LOG.debug(OUTPUT, outpatient);
        return outpatient;
    }

    private Document loadNotes(Document document, Optional<String> optNotes) {
        LOG.debug("Input parameters -> document {}, notes {}", document, optNotes);
        optNotes.ifPresent(notes ->
            document.setOtherNoteId(noteService.createNote(optNotes.get()))
        );
        LOG.debug(OUTPUT, document);
        return document;
    }
}

