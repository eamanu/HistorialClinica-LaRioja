package net.pladema.emergencycare.triage.repository.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.triage.repository.entity.Triage;
import net.pladema.emergencycare.triage.repository.entity.TriageDetails;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TriageVo {

    private Integer id;

    private Integer emergencyCareEpisodeId;

    private Short emergencyCareTypeId;

    private Short categoryId;

    private Integer doctorsOfficeId;

    private Integer professionalId;

    private String notes;

    private Short bodyTemperatureId;

    private Boolean cryingExcessive;

    private Short muscleHypertoniaId;

    private Short respiratoryRetractionId;

    private Boolean stridor;

    private Short perfusionId;

    private LocalDateTime createdOn;

    public TriageVo(Triage triage, TriageDetails triageDetails, Short emergencyCareTypeId, Integer doctorsOfficeId) {
        this.id = triage.getId();
        this.emergencyCareEpisodeId = triage.getEmergencyCareEpisodeId();
        this.emergencyCareTypeId = emergencyCareTypeId;
        this.categoryId = triage.getTriageCategoryId();
        this.professionalId = triage.getHealthcareProfessionalId();
        this.doctorsOfficeId = doctorsOfficeId;
        this.notes = triage.getNotes();
        if (triageDetails != null) {
            this.bodyTemperatureId = triageDetails.getBodyTemperatureId();
            this.cryingExcessive = triageDetails.getCryingExcessive();
            this.muscleHypertoniaId = triageDetails.getMuscleHypertoniaId();
            this.respiratoryRetractionId = triageDetails.getRespiratoryRetractionId();
            this.stridor = triageDetails.getStridor();
            this.perfusionId = triageDetails.getPerfusionId();
        }
        this.createdOn = triage.getCreatedOn();
    }

}