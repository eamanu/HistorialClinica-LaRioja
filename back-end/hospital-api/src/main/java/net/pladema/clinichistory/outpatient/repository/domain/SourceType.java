package net.pladema.clinichistory.outpatient.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "source_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SourceType implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -596696291256492911L;

    public static final short HOSPITALIZATION = (short)0;
    public static final short OUTPATIENT = (short)1;
    public static final short RECIPE = (short)2;
    public static final short ORDER = (short)3;
    public static final short EMERGENCY_CARE = (short)4;

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "description", nullable = false, length = 20)
    private String description;

}