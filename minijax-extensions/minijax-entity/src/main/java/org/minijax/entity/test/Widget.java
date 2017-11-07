package org.minijax.entity.test;

import java.net.URI;
import java.util.UUID;

import javax.persistence.Entity;

import org.minijax.entity.DefaultNamedEntity;

@Entity
public class Widget extends DefaultNamedEntity {
    private static final long serialVersionUID = 1L;

    public Widget() {
        super();
    }

    public Widget(final UUID id) {
        super(id);
    }

    public Widget(final UUID id, final String name) {
        super(id, name);
    }

    @Override
    public URI getUri() {
        return URI.create("/widgets/" + getId());
    }
}
