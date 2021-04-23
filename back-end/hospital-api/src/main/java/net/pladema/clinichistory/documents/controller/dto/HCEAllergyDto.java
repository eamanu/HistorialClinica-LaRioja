package net.pladema.clinichistory.documents.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.ClinicalTermDto;

@Getter
@Setter
@ToString
public class HCEAllergyDto extends ClinicalTermDto {

    private Short categoryId;

    private Short criticalityId;
}
