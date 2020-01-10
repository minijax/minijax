package org.minijax.persistence.metamodel;

import java.util.Objects;

public class AssociationTable {
    private final String tableName;
    private final ColumnDefinition primaryColumn;
    private final ColumnDefinition foreignColumn;

    public AssociationTable(
            final String tableName,
            final ColumnDefinition primaryColumn,
            final ColumnDefinition foreignColumn) {

        this.tableName = Objects.requireNonNull(tableName);
        this.primaryColumn = Objects.requireNonNull(primaryColumn);
        this.foreignColumn = Objects.requireNonNull(foreignColumn);
    }

    public String getTableName() {
        return tableName;
    }

    public ColumnDefinition getPrimaryColumn() {
        return primaryColumn;
    }

    public ColumnDefinition getForeignColumn() {
        return foreignColumn;
    }
}
