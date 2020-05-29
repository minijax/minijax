//tag::include[]
package org.minijax.validation.referenceguide.chapter02.inheritance;

//end::include[]

import jakarta.validation.constraints.NotNull;

//tag::include[]
public class RentalCar extends Car {

	private String rentalStation;

	@NotNull
	public String getRentalStation() {
		return rentalStation;
	}

	//...
}
//end::include[]
