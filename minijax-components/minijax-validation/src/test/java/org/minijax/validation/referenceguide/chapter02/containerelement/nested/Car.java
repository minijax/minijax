//tag::include[]
package org.minijax.validation.referenceguide.chapter02.containerelement.nested;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotNull;

//tag::include[]
public class Car {

	public Map<@NotNull Part, List<@NotNull Manufacturer>> partManufacturers =
			new HashMap<>();

	//...
}
//end::include[]
