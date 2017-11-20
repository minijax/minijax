package org.minijax.validator.referenceguide.chapter02.containerelement.optional;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.validator.MinijaxValidationProvider;

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
	public void validateOptionalContainerElementConstraint() {
		//tag::validateOptionalContainerElementConstraint[]
		Car car = new Car();
		car.setTowingCapacity( 100 );

		Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

		assertEquals( 1, constraintViolations.size() );

		ConstraintViolation<Car> constraintViolation = constraintViolations.iterator().next();
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
