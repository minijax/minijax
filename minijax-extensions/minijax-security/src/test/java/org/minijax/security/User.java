package org.minijax.security;

import java.net.URI;
import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User extends SecurityUser {
    private static final long serialVersionUID = 1L;

    public User() {
        super();
    }

    public User(final UUID id) {
        super(id);
    }

    @Override
    public URI getUri() {
        throw new UnsupportedOperationException();
    }
}
