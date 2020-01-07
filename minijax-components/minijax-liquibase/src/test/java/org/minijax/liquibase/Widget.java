package org.minijax.liquibase;

import java.net.URI;

import javax.persistence.Entity;

import org.minijax.dao.DefaultNamedEntity;

@Entity
public class Widget extends DefaultNamedEntity {
    private static final long serialVersionUID = 1L;

    public Widget() {
        super();
    }

    public Widget(final String name) {
        super(name);
    }

    @Override
    public URI getUri() {
        return URI.create("/widgets/" + getId());
    }
}
