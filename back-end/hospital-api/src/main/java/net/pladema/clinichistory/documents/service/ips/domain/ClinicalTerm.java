package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.exceptions.SelfValidating;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class ClinicalTerm extends SelfValidating<ClinicalTerm> {

    private Integer id;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private SnomedBo snomed;

    private String statusId;

    private String status;

    private String cie10codes;

    public ClinicalTerm(@Valid @NotNull(message = "{value.mandatory}") SnomedBo snomed) {
        this.snomed = snomed;
    }
}
