package org.minijax.validation.referenceguide.chapter02.containerelement.list;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = { ValidPart.ValidPartValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ValidPart {
	String message() default "'null' is not a valid car part.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	class ValidPartValidator
			implements ConstraintValidator<ValidPart, String> {

		@Override
		public void initialize(final ValidPart annotation) {
		}

		@Override
		public boolean isValid(final String value, final ConstraintValidatorContext context) {
			return value != null;
		}
	}
}
