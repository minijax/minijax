package org.minijax.security;

import java.net.URI;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User extends SecurityUser {
    private static final long serialVersionUID = 1L;

    @Override
    public URI getUri() {
        throw new UnsupportedOperationException();
    }
}
