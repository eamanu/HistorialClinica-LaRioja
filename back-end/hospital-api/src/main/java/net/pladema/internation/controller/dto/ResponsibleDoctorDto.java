package net.pladema.internation.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ResponsibleDoctorDto implements Serializable {

    private Integer id;

    private String firstName;

    private String lastName;
}
