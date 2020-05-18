package net.pladema.internation.controller.constraints.validator;

import net.pladema.internation.controller.constraints.EvolutionNoteDiagnosisValid;
import net.pladema.internation.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.internation.service.ips.HealthConditionService;
import net.pladema.internation.service.ips.domain.HealthConditionBo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class EvolutionNoteDiagnosisValidator implements ConstraintValidator<EvolutionNoteDiagnosisValid, Object[]> {

    private static final String DIAGNOSIS_PROPERTY = "evolutionNoteDto.diagnosis";

    private final HealthConditionService healthConditionService;

    public EvolutionNoteDiagnosisValidator(HealthConditionService healthConditionService) {
        this.healthConditionService = healthConditionService;
    }

    @Override
    public void initialize(EvolutionNoteDiagnosisValid constraintAnnotation) {
        // Do nothing.
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Integer internmentEpisodeId = (Integer) parameters[1];
        EvolutionNoteDto evolutionNoteDto = (EvolutionNoteDto) parameters[2];
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{diagnosis.secondary.repeated.principal}")
                .addPropertyNode(DIAGNOSIS_PROPERTY)
                .addConstraintViolation();
        if (evolutionNoteDto.getDiagnosis() == null || evolutionNoteDto.getDiagnosis().isEmpty())
            return true;

        HealthConditionBo mainDiagnosis = healthConditionService.getMainDiagnosisGeneralState(internmentEpisodeId);
        if (mainDiagnosis == null)
            return true;
        return evolutionNoteDto.getDiagnosis().stream().noneMatch(d -> d.getSnomed().getId().equals(mainDiagnosis.getSnomed().getId()));
    }
}