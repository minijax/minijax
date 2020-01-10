package org.minijax.persistence.metamodel;

import java.util.Objects;

public class ColumnDefinition {
    private final String name;
    private final Datatype datatype;
    private final boolean primaryKey;
    private final ForeignReference foreignReference;
//    private final AssociationTable associationTable;

    public ColumnDefinition(
            final String name,
            final Datatype datatype,
            final boolean primaryKey,
            final ForeignReference foreignReference) {

        this.name = Objects.requireNonNull(name);
        this.datatype = Objects.requireNonNull(datatype);
        this.primaryKey = primaryKey;
        this.foreignReference = foreignReference;
//        this.associationTable = associationTable;

        if (primaryKey && foreignReference != null) {
            throw new IllegalStateException("Column cannot be both primary key and foreign key");
        }

//        if (primaryKey && associationTable != null) {
//            throw new IllegalStateException("Column cannot be both primary key and association key");
//        }
//
//        if (foreignReference != null && associationTable != null) {
//            throw new IllegalStateException("Column cannot be both foreign key and association key");
//        }
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

//    public AssociationTable getAssociationTable() {
//        return associationTable;
//    }
}
