package net.pladema.clinichistory.outpatient.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;
import net.pladema.clinichistory.ips.service.domain.ClinicalTerm;
import net.pladema.clinichistory.ips.service.domain.SnomedBo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProcedureSummaryVo extends ClinicalTerm {

    private LocalDate performedDate;

    private Integer consultationID;

    public ProcedureSummaryVo(Integer id, Snomed snomed, LocalDate performedDate, Integer consultationID){
        this.setSnomed(new SnomedBo(snomed));
        this.performedDate = performedDate;
        this.consultationID = consultationID;
    }
}
