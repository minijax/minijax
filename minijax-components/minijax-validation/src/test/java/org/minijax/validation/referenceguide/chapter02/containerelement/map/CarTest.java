package org.minijax.validation.referenceguide.chapter02.containerelement.map;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.validation.MinijaxValidationProvider;

public class CarTest {

	private static Validator validator;

	@BeforeClass
	public static void setUpValidator() {
		ValidatorFactory factory = Validation.byProvider( MinijaxValidationProvider.class )
				.configure()
				.buildValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void validateMapValueContainerElementConstraint() {
		//tag::validateMapValueContainerElementConstraint[]
		Car car = new Car();
		car.setFuelConsumption( Car.FuelConsumption.HIGHWAY, 20 );

		Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

		assertEquals( 1, constraintViolations.size() );

		ConstraintViolation<Car> constraintViolation =
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
		Car car = new Car();
		car.setFuelConsumption( null, 5 );

		Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

		assertEquals( 1, constraintViolations.size() );

		ConstraintViolation<Car> constraintViolation =
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
