package net.pladema.clinichistory.documents.repository.ips.masterdata.entity;

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
@Table(name = "allergy_intolerance_category")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllergyIntoleranceCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "name", nullable = false, length = 15)
	private String description;

	@Column(name = "display", nullable = false, length = 15)
	private String display;

}
