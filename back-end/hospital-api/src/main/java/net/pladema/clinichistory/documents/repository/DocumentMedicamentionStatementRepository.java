package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.entity.DocumentMedicamentionStatement;
import net.pladema.clinichistory.documents.repository.entity.DocumentMedicamentionStatementPK;
import net.pladema.clinichistory.documents.repository.generalstate.domain.MedicationVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentMedicamentionStatementRepository extends JpaRepository<DocumentMedicamentionStatement, DocumentMedicamentionStatementPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.documents.repository.generalstate.domain.MedicationVo(" +
            "ms.id, s, ms.statusId, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentMedicamentionStatement dm " +
            "JOIN MedicationStatement ms ON (dm.pk.medicationStatementId = ms.id) " +
            "JOIN Snomed s ON (s.id = ms.snomedId) " +
            "LEFT JOIN Note n ON (n.id = ms.noteId) " +
            "WHERE dm.pk.documentId = :documentId " +
            "AND ms.statusId NOT IN ('"+ MedicationStatementStatus.ERROR+"')")
    List<MedicationVo> getMedicationStateFromDocument(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.documents.repository.generalstate.domain.MedicationVo(" +
            "ms.id, s, ms.statusId, mss.description as status, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentMedicamentionStatement dm " +
            "JOIN MedicationStatement ms ON (dm.pk.medicationStatementId = ms.id) " +
            "JOIN Snomed s ON (s.id = ms.snomedId) " +
            "JOIN MedicationStatementStatus mss ON (mss.id = ms.statusId) " +
            "LEFT JOIN Note n ON (n.id = ms.noteId) " +
            "WHERE dm.pk.documentId = :documentId ")
    List<MedicationVo> getMedicationStateFromDocumentToReport(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query("SELECT dm " +
            "FROM DocumentMedicamentionStatement dm " +
            "WHERE dm.pk.medicationStatementId = :mid ")
    DocumentMedicamentionStatement findByMedicationId(@Param("mid")  Integer mid);
}
