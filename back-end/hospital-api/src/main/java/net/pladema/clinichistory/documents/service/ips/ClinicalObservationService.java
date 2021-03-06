package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.domain.AnthropometricDataBo;
import net.pladema.clinichistory.documents.service.ips.domain.VitalSignBo;
import net.pladema.clinichistory.documents.service.ips.domain.VitalSignObservationBo;

import java.util.Optional;

public interface ClinicalObservationService {

    VitalSignBo loadVitalSigns(PatientInfoBo patientInfo, Long documentId, Optional<VitalSignBo> optVitalSigns);

    AnthropometricDataBo loadAnthropometricData(PatientInfoBo patientInfo, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData);

    VitalSignObservationBo getObservationById(Integer vitalSignObservationId);
}
