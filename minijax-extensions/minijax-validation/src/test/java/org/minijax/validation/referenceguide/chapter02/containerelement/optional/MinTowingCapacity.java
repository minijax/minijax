package org.minijax.validation.referenceguide.chapter02.containerelement.optional;

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
@Constraint(validatedBy = { MinTowingCapacity.MinTowingCapacityValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface MinTowingCapacity {
	long value();

	String message() default "Not enough towing capacity.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	class MinTowingCapacityValidator implements ConstraintValidator<MinTowingCapacity, Integer> {
		private long min;

		@Override
		public void initialize(final MinTowingCapacity annotation) {
			min = annotation.value();
		}

		@Override
		public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
			if ( value == null ) {
				return true;
			}

			return value >= min;
		}
	}
}
