package org.minijax.persistence.metamodel;

public class ForeignReference {
    private final String tableName;
    private final String columnName;
    private final boolean association;

    public ForeignReference(final String tableName, final String columnName, final boolean association) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.association = association;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isAssociation() {
        return association;
    }
}
