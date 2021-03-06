package net.pladema.patient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.service.domain.PrivateHealthInsuranceDetailsBo;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PrivateHealthInsuranceDetailsDto {

    private Integer id;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String startDate;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String endDate;

    public PrivateHealthInsuranceDetailsDto(PrivateHealthInsuranceDetailsBo privateHealthInsuranceDetailsBo){
        this.id = privateHealthInsuranceDetailsBo.getId();
    }
}
