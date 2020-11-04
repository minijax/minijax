//tag::include[]
package org.minijax.validation.referenceguide.chapter02.fieldlevel;

//end::include[]

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

//tag::include[]
public class Car {

	@NotNull
	private final String manufacturer;

	@AssertTrue
	private final boolean isRegistered;

	public Car(String manufacturer, boolean isRegistered) {
		this.manufacturer = manufacturer;
		this.isRegistered = isRegistered;
	}

	//getters and setters...
}
//end::include[]
