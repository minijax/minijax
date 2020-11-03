package org.minijax.validation.referenceguide.chapter02.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ValidationTest {

	private static Validator validator;

	@BeforeAll
	public static void setUpValidator() {
		//tag::setUpValidator[]
		final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		//end::setUpValidator[]
	}

	@Test
	public void validate() {
		//tag::validate[]
		final Car car = new Car( null, true );

		final Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

		assertEquals( 1, constraintViolations.size() );
		assertEquals( "must not be null", constraintViolations.iterator().next().getMessage() );
		//end::validate[]
	}

	@Test
	public void validateProperty() {
		//tag::validateProperty[]
		final Car car = new Car( null, true );

		final Set<ConstraintViolation<Car>> constraintViolations = validator.validateProperty(
				car,
				"manufacturer"
		);

		assertEquals( 1, constraintViolations.size() );
		assertEquals( "must not be null", constraintViolations.iterator().next().getMessage() );
		//end::validateProperty[]
	}

	@Test
	public void validateValue() {
		//tag::validateValue[]
		final Set<ConstraintViolation<Car>> constraintViolations = validator.validateValue(
				Car.class,
				"manufacturer",
				null
		);

		assertEquals( 1, constraintViolations.size() );
		assertEquals( "must not be null", constraintViolations.iterator().next().getMessage() );
		//end::validateValue[]
	}

}
