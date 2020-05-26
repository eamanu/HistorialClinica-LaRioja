package net.pladema.internation.controller.ips.constraints;

import net.pladema.internation.controller.ips.constraints.validator.EffectiveVitalSignTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EffectiveVitalSignTimeValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EffectiveVitalSignTimeValid {

    String message() default "{clinicalobservation.effetivetime.mandatory}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}