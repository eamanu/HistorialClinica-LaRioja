package net.pladema.clinichistory.documents.repository.hce;

import net.pladema.clinichistory.documents.repository.hce.domain.HCEMedicationVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCEMedicationStatementRepositoryImpl implements HCEMedicationStatementRepository {

    private final EntityManager entityManager;

    public HCEMedicationStatementRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEMedicationVo> getMedication(Integer patientId) {

        String sqlString = "with temporal as (" +
                "SELECT DISTINCT " +
                "ms.id, ms.snomed_id, ms.status_id, ms.updated_on, ms.dosage_id, " +
                "row_number() OVER (PARTITION by ms.snomed_id ORDER BY ms.updated_on desc) AS rw " +
                "FROM document d " +
                "JOIN document_medicamention_statement dms ON d.id = dms.document_id " +
                "JOIN medication_statement ms ON dms.medication_statement_id = ms.id " +
                "WHERE ms.patient_id = :patientId  " +
                "AND d.type_id IN (:documentType) "+
                "AND d.status_id = :documentStatusId " +
                ") " +
                "SELECT t.id AS id, s.sctid AS sctid, s.pt, status_id,  " +
                "d.id AS d_id, d.chronic, d.start_date, d.end_date, d.suspended_start_date, d.suspended_end_date " +
                "FROM temporal t " +
                "JOIN snomed s ON t.snomed_id = s.id " +
                "LEFT JOIN dosage d ON (t.dosage_id = d.id) " +
                "WHERE rw = 1 AND status_id IN (:medicationStatusId) " +
                "ORDER BY t.updated_on";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("documentStatusId", DocumentStatus.FINAL)
                .setParameter("medicationStatusId", List.of(MedicationStatementStatus.ACTIVE))
                .setParameter("patientId", patientId)
                .setParameter("documentType", List.of(DocumentType.OUTPATIENT, DocumentType.RECIPE))
                .getResultList();
        List<HCEMedicationVo> result = new ArrayList<>();
        queryResult.forEach(m ->
                result.add(new HCEMedicationVo(
                        (Integer)m[0],
                        new Snomed((String)m[1], (String)m[2], null, null),
                        (String)m[3],
                        (Integer)m[4],
                        (Boolean)m[5],
                        m[6] != null ? ((Date) m[6]).toLocalDate() : null,
                        m[7] != null ? ((Date) m[7]).toLocalDate() : null,
                        m[8] != null ? ((Date) m[8]).toLocalDate() : null,
                        m[9] != null ? ((Date) m[9]).toLocalDate() : null
                ))
        );
        return result;
    }
}
