package net.pladema.clinichistory.requests.medicationrequests.repository;

import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class GetMedicationRequestInfoRepositoryImpl implements GetMedicationRequestInfoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(GetMedicationRequestInfoRepositoryImpl.class);

    private final EntityManager entityManager;

    public GetMedicationRequestInfoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> execute(Integer medicationRequestId) {
        LOG.debug("Input parameters -> medicationRequestId {}", medicationRequestId);

        String sqlString = "" +
                "WITH temporal AS (" +
                "SELECT mr.id AS mr_id, mr.doctor_id AS doctor_id, mr.request_date AS request_date, " +
                "mr.medical_coverage_id AS medical_coverage_id, n.description AS note, " +
                "row_number() OVER (PARTITION by ms.snomed_id, ms.health_condition_id ORDER BY ms.updated_on ASC) AS rw, " +
                "ms.snomed_id, ms.health_condition_id " +
                "FROM medication_request mr " +
                "JOIN document d ON (mr.id = d.source_id AND d.source_type_id = "+ SourceType.RECIPE + ") " +
                "JOIN document_medicamention_statement dms ON (d.id = dms.document_id) " +
                "JOIN medication_statement ms ON (dms.medication_statement_id = ms.id) " +
                "LEFT JOIN note n ON (ms.note_id = n.id) " +
                "WHERE mr.id = :medicationRequestId  " +
                ")" +
                "SELECT t.mr_id, t.doctor_id, t.request_date, t.medical_coverage_id, t.note, " +
                "s.id AS m_s_id, s.sctid AS m_s_sctid, s.pt AS m_s_pt,  " +
                "h.id AS hid, h.s_id AS h_s_id, h.sctid_id AS h_sctid, h.pt AS h_pt, h.cie10_codes AS cie10_codes " +
                "FROM temporal t " +
                "JOIN snomed s ON (t.snomed_id = s.id) " +
                "JOIN ( SELECT h1.id, s1.id as s_id, s1.sctid as sctid_id, s1.pt, h1.cie10_codes " +
                "            FROM health_condition h1 " +
                "            JOIN snomed s1 ON (h1.snomed_id = s1.id) " +
                "          ) AS h ON (h.id = t.health_condition_id) " +
                "WHERE rw = 1";
        Query query = entityManager.createNativeQuery(sqlString);

        query.setParameter("medicationRequestId", medicationRequestId);

        List<Object[]> result = query.getResultList();
        LOG.trace("execute result query -> {}", result);
        return result;
    }
}
