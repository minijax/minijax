//tag::include[]
package org.minijax.validator.referenceguide.chapter02.objectgraph.containerelement;

//end::include[]

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

//tag::include[]
public class Car {

	public List<@NotNull @Valid Person> passengers = new ArrayList<Person>();

	public Map<@Valid Part, List<@Valid Manufacturer>> partManufacturers = new HashMap<>();

	//...
}
//end::include[]
