package net.pladema.clinichistory.documents.core.hce;

import net.pladema.clinichistory.documents.core.ips.MedicationCalculateStatus;
import net.pladema.clinichistory.documents.repository.hce.HCEMedicationStatementRepository;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEMedicationVo;
import net.pladema.clinichistory.documents.service.hce.HCEMedicationService;
import net.pladema.clinichistory.documents.service.hce.domain.HCEMedicationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HCEMedicationServiceImpl implements HCEMedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEMedicationServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEMedicationStatementRepository hceMedicationStatementRepository;

    private final MedicationCalculateStatus medicationCalculateStatus;

    public HCEMedicationServiceImpl(HCEMedicationStatementRepository hceMedicationStatementRepository,
                                    MedicationCalculateStatus medicationCalculateStatus) {
        this.hceMedicationStatementRepository = hceMedicationStatementRepository;
        this.medicationCalculateStatus = medicationCalculateStatus;
    }

    @Override
    public List<HCEMedicationBo> getMedication(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEMedicationVo> resultQuery = hceMedicationStatementRepository.getMedication(patientId);
        List<HCEMedicationBo> result = resultQuery.stream()
                .map(HCEMedicationBo::new)
                .collect(Collectors.toList());
        result.forEach((hceMedicationBo -> hceMedicationBo.setStatus(medicationCalculateStatus.execute(hceMedicationBo.getStatusId(), hceMedicationBo.getDosage()))));
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }
}
