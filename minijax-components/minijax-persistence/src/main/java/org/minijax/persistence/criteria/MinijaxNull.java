package org.minijax.persistence.criteria;

public class MinijaxNull extends MinijaxExpression<Object> {
    public static final MinijaxNull INSTANCE = new MinijaxNull();

    private MinijaxNull() {
        super(Object.class);
    }
}
