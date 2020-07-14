package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.entity.DocumentHealthCondition;
import net.pladema.clinichistory.documents.repository.entity.DocumentHealthConditionPK;
import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.HealthConditionVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentHealthConditionRepository extends JpaRepository<DocumentHealthCondition, DocumentHealthConditionPK> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.hospitalization.repository.generalstate.domain.HealthConditionVo(" +
            "hc.id, s, hc.statusId, hc.main, hc.verificationStatusId, " +
            "hc.problemId, hc.startDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentHealthCondition dh " +
            "JOIN HealthCondition hc ON (dh.pk.healthConditionId = hc.id) " +
            "JOIN Snomed s ON (s.id = hc.sctidCode) " +
            "LEFT JOIN Note n ON (n.id = hc.noteId) " +
            "WHERE dh.pk.documentId = :documentId " +
            "AND NOT hc.verificationStatusId = ('" + ConditionVerificationStatus.ERROR + "')")
    List<HealthConditionVo> getHealthConditionFromDocument(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.hospitalization.repository.generalstate.domain.HealthConditionVo(" +
            "hc.id, s, hc.statusId, ccs.description as status, hc.main, " +
            "hc.verificationStatusId, cvs.description as verification, " +
            "hc.problemId, hc.startDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentHealthCondition dh " +
            "JOIN HealthCondition hc ON (dh.pk.healthConditionId = hc.id) " +
            "JOIN Snomed s ON (s.id = hc.sctidCode) " +
            "JOIN ConditionClinicalStatus ccs ON (ccs.id = hc.statusId) " +
            "JOIN ConditionVerificationStatus cvs ON (cvs.id = hc.verificationStatusId) " +
            "LEFT JOIN Note n ON (n.id = hc.noteId) " +
            "WHERE dh.pk.documentId = :documentId ")
    List<HealthConditionVo> getHealthConditionFromDocumentToReport(@Param("documentId") Long documentId);

}
