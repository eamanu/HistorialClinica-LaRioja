package net.pladema.emergencycare.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class MedicalDischargeBo implements Document {

    private Integer sourceId;

    private List<ProblemBo> problems;

    private LocalDateTime medicalDischargeOn;

    private Integer medicalDischargeBy;

    private Boolean autopsy;

    private Short dischargeTypeId;

    private Integer patientId;

    private PatientInfoBo patientInfoBo;

    @Override
    public short getDocumentType() {
        return DocumentType.EMERGENCY_CARE;
    }

    @Override
    public Integer getEncounterId() {
        return sourceId;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.EMERGENCY_CARE;
    }

    @Override
    public Integer getPatientId() {
        return patientId;
    }

    @Override
    public List<ProblemBo> getProblems() {
        return problems;
    }

    @Override
    public PatientInfoBo getPatientInfo() {
        return patientInfoBo;
    }
}
