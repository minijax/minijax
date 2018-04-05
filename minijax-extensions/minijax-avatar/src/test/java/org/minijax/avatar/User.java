package org.minijax.avatar;

import java.net.URI;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.minijax.security.SecurityUser;

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
