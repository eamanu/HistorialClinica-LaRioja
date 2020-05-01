package net.pladema.internation.repository.core;

import net.pladema.internation.repository.ips.generalstate.InmunizationVo;
import net.pladema.internation.repository.core.entity.DocumentInmunization;
import net.pladema.internation.repository.core.entity.DocumentInmunizationPK;
import net.pladema.internation.repository.masterdata.entity.InmunizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentInmunizationRepository extends JpaRepository<DocumentInmunization, DocumentInmunizationPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.internation.repository.ips.generalstate.InmunizationVo(" +
            "i.id, s, i.statusId, i.administrationDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentInmunization di " +
            "JOIN Inmunization i ON (di.pk.inmunizationId = i.id) " +
            "JOIN Snomed s ON (s.id = i.sctidCode) " +
            "LEFT JOIN Note n ON (n.id = i.noteId) " +
            "WHERE di.pk.documentId = :documentId " +
            "AND i.statusId NOT IN ('"+ InmunizationStatus.ERROR+"')")
    List<InmunizationVo> getInmunizationStateFromDocument(@Param("documentId") Long documentId);
}
