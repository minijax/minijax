//tag::include[]
package org.minijax.validation.referenceguide.chapter02.containerelement.list;

//end::include[]

import java.util.ArrayList;
import java.util.List;

//tag::include[]
public class Car {

	private final List<@ValidPart String> parts = new ArrayList<>();

	public void addPart(String part) {
		parts.add( part );
	}

	//...

}
//end::include[]
