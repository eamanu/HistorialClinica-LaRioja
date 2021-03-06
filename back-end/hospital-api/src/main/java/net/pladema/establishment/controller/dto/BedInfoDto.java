package net.pladema.establishment.controller.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.controller.dto.BasicPatientDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

@Getter
@Setter
@ToString
public class BedInfoDto implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1999979362232232018L;

	private BedDto bed;

    private BasicPatientDto patient;

	@JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
	private String probableDischargeDate;
    
}
