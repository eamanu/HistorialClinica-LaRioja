package net.pladema.internation.controller.dto.internmentpatient;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class InternmentPatientDto {

    private Integer patientId;

    private Short identificationTypeId;

    private String identificationNumber;

    private String firstName;

    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Short genderId;


}
