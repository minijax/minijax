package org.minijax.persistence.schema;

import java.util.ArrayList;
import java.util.List;

public class Schema {
    private final String name;
    private final List<Table> tables;

    public Schema(final String name) {
        this.name = name;
        this.tables = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Table> getTables() {
        return tables;
    }
}
