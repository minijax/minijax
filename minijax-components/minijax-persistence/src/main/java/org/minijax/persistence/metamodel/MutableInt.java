package org.minijax.persistence.metamodel;

public class MutableInt {
    private int value;

    public MutableInt(final int value) {
        this.value = value;
    }

    public int next() {
        return value++;
    }
}
