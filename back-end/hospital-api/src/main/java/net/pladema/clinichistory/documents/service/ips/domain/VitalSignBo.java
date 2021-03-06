package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.exceptions.SelfValidating;

import javax.validation.Valid;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VitalSignBo extends SelfValidating<VitalSignBo> {

    @Valid
    private ClinicalObservationBo systolicBloodPressure;

    @Valid
    private ClinicalObservationBo diastolicBloodPressure;

    @Valid
    private ClinicalObservationBo meanPressure;

    @Valid
    private ClinicalObservationBo temperature;

    @Valid
    private ClinicalObservationBo heartRate;

    @Valid
    private ClinicalObservationBo respiratoryRate;

    @Valid
    private ClinicalObservationBo bloodOxygenSaturation;

    public boolean hasValues(){
        return (systolicBloodPressure != null ||
                diastolicBloodPressure != null ||
                meanPressure != null ||
                temperature != null ||
                heartRate != null ||
                respiratoryRate != null ||
                bloodOxygenSaturation != null);
    }
}
