package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.generalstate.ClinicalObservationVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.service.domain.MapClinicalObservationVo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClinicalObservationRepositoryImpl implements ClinicalObservationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalObservationRepositoryImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final EntityManager entityManager;

    public ClinicalObservationRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public MapClinicalObservationVo getGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        Query query = entityManager.createNativeQuery(
                "   (SELECT  ovs.id, " +
                "            ovs.sctid_code, " +
                "            ovs.status_id, " +
                "            ovs.value, " +
                "            ovs.effective_time " +
                "    FROM document d " +
                "    JOIN document_vital_sign dvs ON (d.id = dvs.document_id) " +
                "    JOIN observation_vital_sign ovs ON (dvs.observation_vital_sign_id = ovs.id) " +
                "    WHERE source_id = :internmentEpisodeId " +
                "          AND source_type_id = " + SourceType.INTERNACION+" "+
                "          AND d.status_id = :statusId " +
                " )UNION( " +
                "   SELECT  ovs.id, " +
                "            ovs.sctid_code, " +
                "            ovs.status_id, " +
                "            ovs.value, " +
                "            ovs.effective_time " +
                "    FROM document d " +
                "    JOIN document_lab dl ON (d.id = dl.document_id) " +
                "    JOIN observation_lab ovs ON (dl.observation_lab_id = ovs.id) " +
                "    WHERE source_id = :internmentEpisodeId " +
                "          AND source_type_id = " + SourceType.INTERNACION+" "+
                "          AND d.status_id = :statusId " +
                ")" +
                "    ORDER BY effective_time DESC " );
        query.setParameter("internmentEpisodeId", internmentEpisodeId);
        query.setParameter("statusId", DocumentStatus.FINAL);
        List<Object[]> queryResult = query.getResultList();
        List<ClinicalObservationVo> clinicalObservationVos = new ArrayList<>();
        for (Object[] o : queryResult) {
            Timestamp timeStamp = (Timestamp) o[4];
            clinicalObservationVos.add(new ClinicalObservationVo((int) o[0], (String) o[1], (String) o[2],
                    (String) o[3], timeStamp != null ? timeStamp.toLocalDateTime() : null));
        }
        MapClinicalObservationVo result = new MapClinicalObservationVo(clinicalObservationVos);
        LOG.debug(OUTPUT, result);
        return result;
    }
}
