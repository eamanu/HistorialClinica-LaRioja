package net.pladema.establishment.controller.dto;

import java.io.Serializable;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.dto.ClinicalSpecialtyDto;

@Getter
@Setter
@ToString
public class BedSummaryDto implements Serializable {

	private static final long serialVersionUID = 5649684681812631433L;

	private BedDto bed;

	private SectorDto sector;

	private BedCategoryDto bedCategory;

	private ClinicalSpecialtyDto clinicalSpecialty;

	@Nullable
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private String probableDischargeDate;

}