package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.entity.DocumentAllergyIntolerance;
import net.pladema.clinichistory.documents.repository.entity.DocumentAllergyIntolerancePK;
import net.pladema.clinichistory.ips.repository.generalstate.AllergyConditionVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentAllergyIntoleranceRepository extends JpaRepository<DocumentAllergyIntolerance, DocumentAllergyIntolerancePK> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.ips.repository.generalstate.AllergyConditionVo(" +
            "ai.id, s, ai.statusId, ai.verificationStatusId, ai.categoryId," +
            "ai.startDate) " +
            "FROM DocumentAllergyIntolerance da " +
            "JOIN AllergyIntolerance ai ON (da.pk.allergyIntoleranceId = ai.id) " +
            "JOIN Snomed s ON (s.id = ai.sctidCode) " +
            "WHERE da.pk.documentId = :documentId " +
            "AND ai.verificationStatusId NOT IN ('"+ AllergyIntoleranceVerificationStatus.ERROR+"')")
    List<AllergyConditionVo> getAllergyIntoleranceStateFromDocument(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.ips.repository.generalstate.AllergyConditionVo(" +
            "ai.id, s, ai.statusId, aics.description as status, " +
            "ai.verificationStatusId, aivs.description as verification, " +
            "ai.categoryId, ai.startDate) " +
            "FROM DocumentAllergyIntolerance da " +
            "JOIN AllergyIntolerance ai ON (da.pk.allergyIntoleranceId = ai.id) " +
            "JOIN Snomed s ON (s.id = ai.sctidCode) " +
            "JOIN AllergyIntoleranceClinicalStatus aics ON (aics.id = ai.statusId) " +
            "JOIN AllergyIntoleranceVerificationStatus aivs ON (aivs.id = ai.verificationStatusId) " +
            "WHERE da.pk.documentId = :documentId ")
    List<AllergyConditionVo> getAllergyIntoleranceStateFromDocumentToReport(@Param("documentId") Long documentId);
}