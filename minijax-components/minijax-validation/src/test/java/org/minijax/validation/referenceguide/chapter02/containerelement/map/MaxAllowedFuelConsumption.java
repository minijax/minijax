package org.minijax.validation.referenceguide.chapter02.containerelement.map;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = { MaxAllowedFuelConsumption.MaxAllowedFuelConsumptionValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface MaxAllowedFuelConsumption {
	String message() default "${validatedValue} is outside the max fuel consumption.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	class MaxAllowedFuelConsumptionValidator implements ConstraintValidator<MaxAllowedFuelConsumption, Integer> {

		@Override
		public void initialize(final MaxAllowedFuelConsumption annotation) {
		}

		@Override
		public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
			if ( value == null ) {
				return true;
			}

			return value >= 0 && value <= 10;
		}
	}
}
