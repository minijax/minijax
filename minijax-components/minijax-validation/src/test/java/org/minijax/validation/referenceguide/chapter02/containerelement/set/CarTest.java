package org.minijax.validation.referenceguide.chapter02.containerelement.set;

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
	public void validateSetContainerElementConstraint() {
		//tag::validateSetContainerElementConstraint[]
		final Car car = new Car();
		car.addPart( "Wheel" );
		car.addPart( null );

		final Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

		assertEquals( 1, constraintViolations.size() );

		final ConstraintViolation<Car> constraintViolation =
				constraintViolations.iterator().next();
		assertEquals(
				"'null' is not a valid car part.",
				constraintViolation.getMessage()
		);
		assertEquals( "parts[].<iterable element>",
				constraintViolation.getPropertyPath().toString() );
		//end::validateSetContainerElementConstraint[]
	}
}
