package net.pladema.internation.controller.ips.constraints.validator;

import net.pladema.dates.configuration.JacksonDateFormatConfig;
import net.pladema.internation.controller.documents.dto.DocumentDto;
import net.pladema.internation.controller.ips.constraints.EffectiveVitalSignTimeValid;
import net.pladema.internation.controller.ips.dto.EffectiveClinicalObservationDto;
import net.pladema.internation.controller.ips.dto.VitalSignDto;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class EffectiveVitalSignTimeValidator implements ConstraintValidator<EffectiveVitalSignTimeValid, Object[]> {

    private static final Logger LOG = LoggerFactory.getLogger(EffectiveVitalSignTimeValidator.class);

    private final InternmentEpisodeService internmentEpisodeService;

    private final MessageSource messageSource;

    public EffectiveVitalSignTimeValidator(InternmentEpisodeService internmentEpisodeService, MessageSource messageSource) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.messageSource = messageSource;
    }

    @Override
    public void initialize(EffectiveVitalSignTimeValid constraintAnnotation) {
        // Nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> parameters {}", parameters);
        Integer internmentEpisodeId = (Integer) parameters[1];
        DocumentDto documentDto = (DocumentDto) parameters[2];
        VitalSignDto vitalSigns = documentDto.getVitalSigns();
        if (vitalSigns == null)
            return true;


        LocalDate entryDate = internmentEpisodeService.getEntryDate(internmentEpisodeId);
        if (!validEffectiveClinicalObservation(context,"vitalsign.blood.oxygen.saturation", vitalSigns.getBloodOxygenSaturation(), entryDate))
            return false;

        if (!validEffectiveClinicalObservation(context,"vitalsign.diastolic.blood.pressure", vitalSigns.getDiastolicBloodPressure(), entryDate))
            return false;

        if (!validEffectiveClinicalObservation(context,"vitalsign.heart.rate", vitalSigns.getHeartRate(), entryDate))
            return false;

        if (!validEffectiveClinicalObservation(context,"vitalsign.respiratory.rate", vitalSigns.getRespiratoryRate(), entryDate))
            return false;

        if (!validEffectiveClinicalObservation(context,"vitalsign.systolic.blood.pressure", vitalSigns.getSystolicBloodPressure(), entryDate))
            return false;

        return validEffectiveClinicalObservation(context,"vitalsign.temperature", vitalSigns.getTemperature(), entryDate);
    }

    private boolean validEffectiveClinicalObservation(ConstraintValidatorContext context, String label,
                                                      EffectiveClinicalObservationDto effectiveClinicalObservationDto, LocalDate entryDate){
        if (effectiveClinicalObservationDto == null)
            return true;
        String property = messageSource.getMessage(label, null, LocaleContextHolder.getLocale());
        if (effectiveClinicalObservationDto.getEffectiveTime() == null)
            return false;
        LocalDateTime datetime = LocalDateTime.parse(effectiveClinicalObservationDto.getEffectiveTime(), DateTimeFormatter.ofPattern(JacksonDateFormatConfig.DATE_TIME_FORMAT));
        if (datetime.isAfter(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{clinicalobservation.effetivetime.future}")
                    .addPropertyNode(property)
                    .addConstraintViolation();
            return false;
        }
        if (datetime.isBefore(entryDate.atStartOfDay())){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{clinicalobservation.effetivetime.before.entrydate}")
                    .addPropertyNode(property)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
