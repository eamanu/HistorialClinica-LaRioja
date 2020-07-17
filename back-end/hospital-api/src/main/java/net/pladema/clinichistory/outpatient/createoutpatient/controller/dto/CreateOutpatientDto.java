package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateOutpatientDto {

    @NotNull
    private String reasonId;

    @Nullable
    private String evolutionNote;

    @NotNull
    private List<@Valid OutpatientProblemDto> problems = new ArrayList<>();

    @NotNull
    private List<@Valid OutpatientFamilyHistoryDto> familyHistories = new ArrayList<>();

    @NotNull
    private  List<@Valid OutpatientMedicationDto> medications = new ArrayList<>();

    @NotNull
    private List<@Valid OutpatientAllergyConditionDto> allergies = new ArrayList<>();

    @Valid
    @Nullable
    private OutpatientAnthropometricDataDto anthropometricData;

    @Valid
    @Nullable
    private OutpatientVitalSignDto vitalSigns;



}