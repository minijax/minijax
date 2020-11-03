package org.minijax.validation.referenceguide.chapter02.containerelement.optional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.validation.MinijaxValidationProvider;

public class CarTest {

	private static Validator validator;

	@BeforeAll
	public static void setUpValidator() {
		final ValidatorFactory factory = Validation.byProvider( MinijaxValidationProvider.class )
				.configure()
				.buildValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void validateOptionalContainerElementConstraint() {
		//tag::validateOptionalContainerElementConstraint[]
		final Car car = new Car();
		car.setTowingCapacity( 100 );

		final Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

		assertEquals( 1, constraintViolations.size() );

		final ConstraintViolation<Car> constraintViolation = constraintViolations.iterator().next();
		assertEquals(
				"Not enough towing capacity.",
				constraintViolation.getMessage()
		);
		assertEquals(
				"towingCapacity",
				constraintViolation.getPropertyPath().toString()
		);
		//end::validateOptionalContainerElementConstraint[]
	}

}
