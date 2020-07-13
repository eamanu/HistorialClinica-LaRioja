package net.pladema.clinichistory.generalstate.service.impl;

import net.pladema.clinichistory.generalstate.repository.HCEHealthConditionRepository;
import net.pladema.clinichistory.generalstate.repository.domain.HCEHealthConditionVo;
import net.pladema.clinichistory.generalstate.service.HCEGeneralStateService;
import net.pladema.clinichistory.generalstate.service.domain.HCEPersonalHistoryBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HCEGeneralStateServiceImpl implements HCEGeneralStateService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEGeneralStateServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEHealthConditionRepository hceHealthConditionRepository;

    public HCEGeneralStateServiceImpl(HCEHealthConditionRepository hceHealthConditionRepository) {
        this.hceHealthConditionRepository = hceHealthConditionRepository;
    }


    @Override
    public List<HCEPersonalHistoryBo> getPersonalHistory(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistory(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream().map(HCEPersonalHistoryBo::new).collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEPersonalHistoryBo> getFamilyHistory(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getFamilyHistory(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream().map(HCEPersonalHistoryBo::new).collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }
}