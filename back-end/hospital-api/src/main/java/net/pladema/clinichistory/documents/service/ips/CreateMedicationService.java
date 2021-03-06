package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;

import java.util.List;

public interface CreateMedicationService {

    List<MedicationBo> execute(PatientInfoBo patientInfo, Long documentId, List<MedicationBo> medications);

}
