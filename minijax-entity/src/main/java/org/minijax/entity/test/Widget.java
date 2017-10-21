package org.minijax.entity.test;

import java.util.UUID;

import javax.persistence.Entity;

import org.minijax.entity.NamedEntity;

@Entity
public class Widget extends NamedEntity {
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
    public String getUrl() {
        return "/widgets/" + getId();
    }
}