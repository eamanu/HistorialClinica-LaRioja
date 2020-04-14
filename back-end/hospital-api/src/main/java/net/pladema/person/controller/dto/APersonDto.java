package net.pladema.person.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class APersonDto {

    /* datos de Persona*/
    @NotNull
    private String firstName;

    private String middleNames;

    @NotNull
    private String lastName;

    private String otherLastNames;

    @NotNull
    private Short identificationTypeId;

    private String identificationNumber;

    @NotNull
    private Short genderId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    /*datos de persona Extended*/
    private String cuil;

    private String mothersLastName;

    private String phoneNumber;

    private String email;

    private String ethnic;

    private String religion;

    private String nameSelfDetermination;

    private Short genderSelfDetermination;

    private String healthInsuranceId;

    /*datos de Address*/
    private String street;

    private String number;

    private String floor;

    private String apartment;

    private String quarter;

    private Short cityId;

    private String postcode;

}
