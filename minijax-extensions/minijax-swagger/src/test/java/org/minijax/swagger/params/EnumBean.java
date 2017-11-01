package org.minijax.swagger.params;

import javax.ws.rs.HeaderParam;

import org.minijax.swagger.models.TestEnum;

public class EnumBean {

    @HeaderParam("HeaderParam")
    private TestEnum value;

    public TestEnum getValue() {
        return value;
    }

    public void setValue(TestEnum value) {
        this.value = value;
    }
}
