package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.generalstate.domain.ImmunizationVo;
import net.pladema.clinichistory.documents.repository.entity.DocumentInmunization;
import net.pladema.clinichistory.documents.repository.entity.DocumentInmunizationPK;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.InmunizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentImmunizationRepository extends JpaRepository<DocumentInmunization, DocumentInmunizationPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.documents.repository.generalstate.domain.ImmunizationVo(" +
            "i.id, s, i.statusId, i.administrationDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentInmunization di " +
            "JOIN Inmunization i ON (di.pk.inmunizationId = i.id) " +
            "JOIN Snomed s ON (s.id = i.snomedId) " +
            "LEFT JOIN Note n ON (n.id = i.noteId) " +
            "WHERE di.pk.documentId = :documentId " +
            "AND i.statusId NOT IN ('"+ InmunizationStatus.ERROR+"')")
    List<ImmunizationVo> getImmunizationStateFromDocument(@Param("documentId") Long documentId);


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.documents.repository.generalstate.domain.ImmunizationVo(" +
            "i.id, s, i.statusId, iss.description as status, i.administrationDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentInmunization di " +
            "JOIN Inmunization i ON (di.pk.inmunizationId = i.id) " +
            "JOIN Snomed s ON (s.id = i.snomedId) " +
            "JOIN InmunizationStatus iss ON (iss.id = i.statusId) " +
            "LEFT JOIN Note n ON (n.id = i.noteId) " +
            "WHERE di.pk.documentId = :documentId ")
    List<ImmunizationVo> getImmunizationStateFromDocumentToReport(@Param("documentId") Long documentId);
}
