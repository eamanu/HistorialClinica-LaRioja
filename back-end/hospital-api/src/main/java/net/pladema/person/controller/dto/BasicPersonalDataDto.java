package net.pladema.person.controller.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BasicPersonalDataDto implements IBasicPersonalData {

    private String firstName;

    private String lastName;

    private String identificationNumber;

    private Short identificationTypeId;

    private String phoneNumber;

    private Short genderId;
}
