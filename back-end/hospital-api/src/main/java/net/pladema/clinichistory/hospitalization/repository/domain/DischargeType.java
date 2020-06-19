package net.pladema.clinichistory.hospitalization.repository.domain;

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
@Table(name = "discharge_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DischargeType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4387583883246442169L;

	@Id
	@Column(name = "id", nullable = false)
	private Short id;

	@Column(name = "description", nullable = false)
	private String description;

}