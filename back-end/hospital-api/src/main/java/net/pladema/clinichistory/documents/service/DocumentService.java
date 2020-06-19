package net.pladema.clinichistory.documents.service;

import net.pladema.clinichistory.ips.service.domain.*;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.repository.entity.DocumentInmunization;
import net.pladema.clinichistory.documents.repository.entity.DocumentLab;
import net.pladema.clinichistory.documents.repository.entity.DocumentVitalSign;
import net.pladema.sgx.auditable.entity.Updateable;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    Optional<Document> findById(Long documentId);

    Document save(Document document);

    void createDocumentHealthCondition(Long documentId, Integer healthConditionId);

    DocumentVitalSign createDocumentVitalSign(Long documentId, Integer observationVitalSignId);

    DocumentLab createDocumentLab(Long documentId, Integer observationLabId);

    void createDocumentAllergyIntolerance(Long documentId, Integer allergyIntoleranceId);

    DocumentInmunization createInmunization(Long documentId, Integer inmunizationId);
    
    void createDocumentMedication(Long documentId, Integer medicationStatementId);

    GeneralHealthConditionBo getHealthConditionFromDocument(Long documentId);

    List<InmunizationBo> getInmunizationStateFromDocument(Long documentId);

    List<AllergyConditionBo> getAllergyIntoleranceStateFromDocument(Long documentId);

    List<MedicationBo> getMedicationStateFromDocument(Long documentId);

    AnthropometricDataBo getAnthropometricDataStateFromDocument(Long documentId);

    VitalSignBo getVitalSignStateFromDocument(Long documentId);

    List<Updateable> getUpdatablesDocuments(Integer internmentEpisodeId);

    void deleteHealthConditionHistory(Long documentId);

    void deleteAllergiesHistory(Long documentId);

    void deleteInmunizationsHistory(Long documentId);

    void deleteMedicationsHistory(Long documentId);

    void deleteObservationsVitalSignsHistory(Long documentId);

    void deleteObservationsLabHistory(Long documentId);

}
