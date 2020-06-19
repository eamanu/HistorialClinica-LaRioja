package net.pladema.clinichistory.hospitalization.service.evolutionnote.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.InternmentDocument;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.service.domain.*;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class EvolutionNoteBo implements InternmentDocument {

    private Long id;

    private boolean confirmed;

    private DocumentObservationsBo notes;

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis;

    private List<InmunizationBo> inmunizations;

    private List<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData;

    private VitalSignBo vitalSigns;

    public String getDocumentStatusId(){
        return confirmed ? DocumentStatus.FINAL : DocumentStatus.DRAFT;
    }


    @Override
    public List<HealthHistoryConditionBo> getPersonalHistories() {
        return Collections.emptyList();
    }

    @Override
    public List<HealthHistoryConditionBo> getFamilyHistories() {
        return Collections.emptyList();
    }

    @Override
    public List<MedicationBo> getMedications() {
        return Collections.emptyList();
    }
}