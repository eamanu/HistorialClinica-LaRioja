package net.pladema.clinichistory.documents.repository.hce;

import net.pladema.clinichistory.documents.repository.hce.domain.HCEAllergyVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.AllergyIntoleranceVerificationStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCEAllergyIntoleranceRepositoryImpl implements HCEAllergyIntoleranceRepository {

    private final EntityManager entityManager;

    public HCEAllergyIntoleranceRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEAllergyVo> findAllergies(Integer patientId) {

        String sqlString = "with temporal as (" +
                "SELECT DISTINCT " +
                "ai.id, " +
                "ai.snomed_id, " +
                "ai.status_id, " +
                "ai.verification_status_id, " +
                "ai.category_id, " +
                "ai.criticality, " +
                "ai.start_date, " +
                "ai.updated_on, " +
                "row_number() over (partition by ai.snomed_id order by ai.updated_on desc) as rw " +
                "FROM document d " +
                "JOIN document_allergy_intolerance dai ON d.id = dai.document_id " +
                "JOIN allergy_intolerance ai ON dai.allergy_intolerance_id = ai.id " +
                "WHERE d.type_id = :documentType "+
                "AND d.status_id = :documentStatusId " +
                "AND ai.patient_id = :patientId " +
                ") " +
                "SELECT t.id AS id, s.sctid AS sctid, s.pt, t.status_id, t.verification_status_id, t.category_id, t.criticality, t.start_date " +
                "FROM temporal t " +
                "JOIN snomed s ON t.snomed_id = s.id " +
                "WHERE rw = 1 AND NOT status_id = :allergyIntoleranceStatus " +
                "ORDER BY t.updated_on desc ";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("patientId", patientId)
                .setParameter("documentStatusId", DocumentStatus.FINAL)
                .setParameter("documentType", DocumentType.OUTPATIENT)
                .setParameter("allergyIntoleranceStatus", AllergyIntoleranceVerificationStatus.ERROR)
                .getResultList();
        List<HCEAllergyVo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new HCEAllergyVo(
                        (Integer) a[0],
                        new Snomed((String) a[1], (String) a[2], null, null),
                        (String) a[3],
                        (String) a[4],
                        (Short) a[5],
                        (Short) a[6],
                        a[7] != null ? ((Date) a[7]).toLocalDate() : null
                ))
        );
        return result;
    }
}
