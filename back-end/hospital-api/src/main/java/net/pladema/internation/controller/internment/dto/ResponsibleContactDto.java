package net.pladema.internation.controller.internment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class ResponsibleContactDto implements Serializable {

    @Nullable
    private String fullName;

    @Nullable
    private String phoneNumber;

    @Nullable
    private String relationship;
}