package net.pladema.clinichistory.documents.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class HealthConditionNewConsultationDto implements Serializable {

    private Integer id;

    private Integer patientId;

    private String sctidCode;

    private String statusId;

    private String verificationStatusId;

    private LocalDate startDate;

    private LocalDate inactivationDate;

    private Boolean main;

    private Long noteId;

    private String problemId;

    private SnomedDto snomed;

    private Boolean isChronic;

    @NotNull(message = "{value.mandatory}")
    private String severity;
}
