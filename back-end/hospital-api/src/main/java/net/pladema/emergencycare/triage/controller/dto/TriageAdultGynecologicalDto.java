package net.pladema.emergencycare.triage.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.pladema.clinichistory.documents.controller.dto.NewVitalSignsObservationDto;

@Getter
@ToString
@NoArgsConstructor(force = true)
public class TriageAdultGynecologicalDto extends TriageNoAdministrativeDto {

    private NewVitalSignsObservationDto vitalSigns;

    @Builder(builderMethodName = "adultGynecologicalBuilder")
    public TriageAdultGynecologicalDto(Short categoryId, Integer doctorsOfficeId, String notes){
        super(categoryId, doctorsOfficeId, notes);
    }
}
