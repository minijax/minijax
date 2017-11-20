//tag::include[]
package org.minijax.validator.referenceguide.chapter02.containerelement.optional;

//end::include[]

import java.util.Optional;

//tag::include[]
public class Car {

	public Optional<@MinTowingCapacity(1000) Integer> towingCapacity = Optional.empty();

	public void setTowingCapacity(final Integer alias) {
		towingCapacity = Optional.of( alias );
	}

	//...

}
//end::include[]
