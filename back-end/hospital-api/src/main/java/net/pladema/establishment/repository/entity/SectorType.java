package net.pladema.establishment.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sector_type")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class SectorType {

    public static final Short OUTPATIENT_ID = 1;
    public static final Short INTERNMENT_ID = 2;

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Short id;

    @Column(name = "description", nullable = false)
    private String description;
}
