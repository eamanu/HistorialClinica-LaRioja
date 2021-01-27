package net.pladema.emergencycare.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.PoliceIntervention;
import net.pladema.emergencycare.triage.repository.entity.TriageCategory;
import net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo;
import net.pladema.person.repository.entity.Person;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmergencyCareVo implements Serializable {

	private static final long serialVersionUID = -8118445529514102823L;

	private Integer id;

	private PatientECEVo patient;

	private Short triageCategoryId;

	private String triageName;

	private String triageColorCode;

	private Integer institutionId;

	private Short emergencyCareTypeId;

	private Short emergencyCareStateId;

	private Short emergencyCareEntranceTypeId;

	private DoctorsOfficeVo doctorsOffice;

	private String ambulanceCompanyId;

	private LocalDateTime createdOn;

	private PoliceInterventionVo policeIntervention;

	public EmergencyCareVo(EmergencyCareEpisode emergencyCareEpisode, Person person, Short patientTypeId, String doctorsOfficeDescription, TriageCategory triage){
		this.id = emergencyCareEpisode.getId();
		this.patient = emergencyCareEpisode.getPatientId() != null ? new PatientECEVo(emergencyCareEpisode.getPatientId(), emergencyCareEpisode.getPatientMedicalCoverageId(), patientTypeId, person) : null;
		this.triageCategoryId = triage.getId();
		this.triageName = triage.getName();
		this.triageColorCode = triage.getColorCode();
		this.institutionId = emergencyCareEpisode.getInstitutionId();
		this.emergencyCareTypeId = emergencyCareEpisode.getEmergencyCareTypeId();
		this.emergencyCareStateId = emergencyCareEpisode.getEmergencyCareStateId();
		this.emergencyCareEntranceTypeId = emergencyCareEpisode.getEmergencyCareEntranceTypeId();
		this.ambulanceCompanyId = emergencyCareEpisode.getAmbulanceCompanyId();
		this.createdOn = emergencyCareEpisode.getCreatedOn();
		this.doctorsOffice = emergencyCareEpisode.getDoctorsOfficeId() != null ? new DoctorsOfficeVo(emergencyCareEpisode.getDoctorsOfficeId(), doctorsOfficeDescription) : null;
	}

	public EmergencyCareVo(EmergencyCareEpisode emergencyCareEpisode, Person person, Short patientTypeId, String doctorsOfficeDescription, TriageCategory triage, PoliceIntervention policeIntervention){
		this(emergencyCareEpisode, person, patientTypeId, doctorsOfficeDescription, triage);
		this.policeIntervention = policeIntervention != null ? new PoliceInterventionVo(policeIntervention) : null;
	}
}
