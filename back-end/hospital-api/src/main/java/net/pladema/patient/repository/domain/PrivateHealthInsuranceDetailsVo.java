package net.pladema.patient.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.repository.entity.PrivateHealthInsuranceDetails;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PrivateHealthInsuranceDetailsVo {

    private Integer id;

    private LocalDate startDate;

    private LocalDate endDate;

    public PrivateHealthInsuranceDetailsVo(Integer id, LocalDate startDate, LocalDate endDate){
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PrivateHealthInsuranceDetailsVo(PrivateHealthInsuranceDetails privateHealthInsuranceDetails){
        this.id = privateHealthInsuranceDetails.getId();
        this.startDate = privateHealthInsuranceDetails.getStartDate() != null ? privateHealthInsuranceDetails.getStartDate() : null;
        this.endDate = privateHealthInsuranceDetails.getEndDate() != null ? privateHealthInsuranceDetails.getEndDate() : null;
    }
}
