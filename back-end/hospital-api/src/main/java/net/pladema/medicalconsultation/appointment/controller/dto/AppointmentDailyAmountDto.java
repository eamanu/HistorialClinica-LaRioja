package net.pladema.medicalconsultation.appointment.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

@Value
@Builder
@ToString
@AllArgsConstructor
public class AppointmentDailyAmountDto {

    private Integer spontaneous;

    private Integer programmedAvailable;

    private Integer programmed;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String date;
}