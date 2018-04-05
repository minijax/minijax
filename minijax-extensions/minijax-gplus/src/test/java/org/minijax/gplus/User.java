package org.minijax.gplus;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.minijax.security.SecurityUser;

@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User extends SecurityUser implements GooglePlusUser {
    private static final long serialVersionUID = 1L;
    private String googleCredentials;

    @Override
    public String getGoogleCredentials() {
        return googleCredentials;
    }

    @Override
    public void setGoogleCredentials(final String googleCredentials) {
        this.googleCredentials = googleCredentials;
    }
}
