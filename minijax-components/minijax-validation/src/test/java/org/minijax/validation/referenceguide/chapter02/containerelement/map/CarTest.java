package org.minijax.validation.referenceguide.chapter02.containerelement.map;

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
	public void validateMapValueContainerElementConstraint() {
		//tag::validateMapValueContainerElementConstraint[]
		final Car car = new Car();
		car.setFuelConsumption( Car.FuelConsumption.HIGHWAY, 20 );

		final Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

		assertEquals( 1, constraintViolations.size() );

		final ConstraintViolation<Car> constraintViolation =
				constraintViolations.iterator().next();
		assertEquals(
				"20 is outside the max fuel consumption.",
				constraintViolation.getMessage()
		);
		assertEquals(
				"fuelConsumption[HIGHWAY].<map value>",
				constraintViolation.getPropertyPath().toString()
		);
		//end::validateMapValueContainerElementConstraint[]
	}

	@Test
	public void validateMapKeyContainerElementConstraint() {
		//tag::validateMapKeyContainerElementConstraint[]
		final Car car = new Car();
		car.setFuelConsumption( null, 5 );

		final Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

		assertEquals( 1, constraintViolations.size() );

		final ConstraintViolation<Car> constraintViolation =
				constraintViolations.iterator().next();
		assertEquals(
				"must not be null",
				constraintViolation.getMessage()
		);
		assertEquals(
				"fuelConsumption<K>[].<map key>",
				constraintViolation.getPropertyPath().toString()
		);
		//end::validateMapKeyContainerElementConstraint[]
	}
}
