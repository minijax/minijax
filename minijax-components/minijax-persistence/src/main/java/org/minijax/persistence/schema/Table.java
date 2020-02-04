package org.minijax.persistence.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Table {
    private final Schema schema;
    private final String name;
    private final List<Column> columns;

    public Table(final Schema schema, final String name) {
        this.schema = Objects.requireNonNull(schema);
        this.name = Objects.requireNonNull(name);
        this.columns = new ArrayList<>();
    }

    public Schema getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }
}
