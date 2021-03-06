//tag::include[]
package org.minijax.validation.referenceguide.chapter02.objectgraph;

//end::include[]

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

//tag::include[]
public class Car {

	@NotNull
	@Valid
	private Person driver;

	//...
}
//end::include[]

