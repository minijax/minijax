package org.minijax.persistence.metamodel;

import java.util.Objects;

public class ColumnDefinition {
    private final String name;
    private final Datatype datatype;
    private final boolean primaryKey;
    private final ForeignReference foreignReference;

    public ColumnDefinition(
            final String name,
            final Datatype datatype,
            final boolean primaryKey,
            final ForeignReference foreignReference) {

        this.name = Objects.requireNonNull(name);
        this.datatype = Objects.requireNonNull(datatype);
        this.primaryKey = primaryKey;
        this.foreignReference = foreignReference;

        if (primaryKey && foreignReference != null) {
            throw new IllegalStateException("Column cannot be both primary key and foreign key");
        }
    }

    public String getName() {
        return name;
    }

    public Datatype getDatatype() {
        return datatype;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public ForeignReference getForeignReference() {
        return foreignReference;
    }
}
