package net.pladema.clinichistory.documents.core.hce;

import net.pladema.clinichistory.documents.repository.hce.HCEClinicalObservationRepository;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEMapClinicalObservationVo;
import net.pladema.clinichistory.documents.service.hce.HCEClinicalObservationService;
import net.pladema.clinichistory.documents.service.hce.domain.HCEAnthropometricDataBo;
import net.pladema.clinichistory.documents.service.hce.domain.Last2HCEVitalSignsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HCEClinicalObservationServiceImpl implements HCEClinicalObservationService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEClinicalObservationServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEClinicalObservationRepository hceClinicalObservationRepository;

    public HCEClinicalObservationServiceImpl(HCEClinicalObservationRepository hceClinicalObservationRepository) {
        this.hceClinicalObservationRepository = hceClinicalObservationRepository;
    }


    @Override
    public HCEAnthropometricDataBo getLastAnthropometricDataGeneralState(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        HCEMapClinicalObservationVo resultQuery = hceClinicalObservationRepository.getGeneralState(patientId);
        HCEAnthropometricDataBo result = resultQuery.getLastNAnthropometricData(0).orElse(null);
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }


    @Override
    public Last2HCEVitalSignsBo getLast2VitalSignsGeneralState(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        HCEMapClinicalObservationVo resultQuery = hceClinicalObservationRepository.getGeneralState(patientId);
        Last2HCEVitalSignsBo result = new Last2HCEVitalSignsBo();
        for (int i=0;i<2;i++){
            if (i==0) {
                resultQuery.getLastNVitalSigns(i).ifPresent(result::setCurrent);
            }
            if (i==1) {
                resultQuery.getLastNVitalSigns(i).ifPresent(result::setPrevious);
            }
        }
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }
}
