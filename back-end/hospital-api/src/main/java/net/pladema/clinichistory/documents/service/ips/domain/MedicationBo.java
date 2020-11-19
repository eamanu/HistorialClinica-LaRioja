package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.generalstate.domain.MedicationVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationBo extends ClinicalTerm {

    private String note;

    private boolean suspended = false;

    public MedicationBo(MedicationVo medicationVo) {
        super();
        setId(medicationVo.getId());
        setStatusId(medicationVo.getStatusId());
        setStatus(medicationVo.getStatus());
        setSnomed(new SnomedBo(medicationVo.getSnomed()));
        setNote(medicationVo.getNote());
        suspended = super.getStatusId().equalsIgnoreCase(MedicationStatementStatus.SUSPENDED);
    }

    public MedicationBo(SnomedBo snomedBo) {
        super(snomedBo);
    }

    @Override
    public String getStatusId(){
        return suspended ? MedicationStatementStatus.SUSPENDED : super.getStatusId();
    }

}