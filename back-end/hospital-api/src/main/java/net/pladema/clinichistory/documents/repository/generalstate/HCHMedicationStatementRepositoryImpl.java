package net.pladema.clinichistory.documents.repository.generalstate;

import net.pladema.clinichistory.documents.repository.generalstate.domain.MedicationVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCHMedicationStatementRepositoryImpl implements HCHMedicationStatementRepository {

    private final EntityManager entityManager;

    public HCHMedicationStatementRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<MedicationVo> findGeneralState(Integer internmentEpisodeId) {

        String sqlString = "with temporal as (" +
                "select distinct " +
                "ms.id, " +
                "ms.snomed_id, " +
                "ms.status_id, " +
                "ms.note_id, " +
                "ms.updated_on, " +
                "row_number() over (partition by ms.snomed_id order by ms.updated_on desc) as rw " +
                "from document d " +
                "join document_medicamention_statement dms on d.id = dms.document_id " +
                "join medication_statement ms on dms.medication_statement_id = ms.id " +
                "where d.source_id = :internmentEpisodeId " +
                "and d.source_type_id = " + SourceType.HOSPITALIZATION +" "+
                "and d.status_id = :documentStatusId " +
                ") " +
                "select t.id as id, s.sctid as sctid, s.pt, status_id, n.id as note_id, n.description as note " +
                "from temporal t " +
                "left join note n on t.note_id = n.id " +
                "join snomed s on t.snomed_id = s.id " +
                "where rw = 1 and not status_id = :medicationStatusId " +
                "order by t.updated_on";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("internmentEpisodeId", internmentEpisodeId)
                .setParameter("documentStatusId", DocumentStatus.FINAL)
                .setParameter("medicationStatusId", MedicationStatementStatus.ERROR)
                .getResultList();
        List<MedicationVo> result = new ArrayList<>();
        queryResult.forEach(m -> 
            result.add(new MedicationVo(
                    (Integer)m[0],
                    new Snomed((String)m[1], (String)m[2], null, null),
                    (String)m[3],
                    m[4] != null ? ((BigInteger)m[4]).longValue() : null,
                    (String)m[5]
            ))
        );
        return result;
    }
}
