package org.minijax.persistence.schema;

import java.util.Objects;

import org.minijax.persistence.metamodel.Datatype;
import org.minijax.persistence.metamodel.ForeignReference;

public class Column {
    private final Table table;
    private final String name;
    private final Datatype datatype;
    private boolean primaryKey;
    private ForeignReference foreignReference;
    private Table joinTable;

    public Column(final Table table, final String name, final Datatype datatype) {
        this.table = Objects.requireNonNull(table);
        this.name = Objects.requireNonNull(name);
        this.datatype = Objects.requireNonNull(datatype);
    }

    public Table getTable() {
        return table;
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

    public void setPrimaryKey(final boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public ForeignReference getForeignReference() {
        return foreignReference;
    }

    public void setForeignReference(final ForeignReference foreignReference) {
        this.foreignReference = foreignReference;
    }

    public Table getJoinTable() {
        return joinTable;
    }

    public void setJoinTable(final Table joinTable) {
        this.joinTable = joinTable;
    }
}
