package org.minijax.security;

import java.util.Arrays;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.Validate;
import org.minijax.db.DefaultNamedEntity;

/**
 * The User class represents a web application user.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("squid:S2160")
public abstract class SecurityUser extends DefaultNamedEntity {
    private static final long serialVersionUID = 1L;

    @Column(length = 128, unique = true)
    private String email;

    @XmlTransient
    private String roles;

    @XmlTransient
    private String passwordHash;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns all user roles as a string.
     *
     * @return All user roles.
     */
    public String getRoles() {
        return roles;
    }

    /**
     * Sets the user roles.
     *
     * @param roles The user roles.
     */
    public void setRoles(final String... roles) {
        Validate.notEmpty(roles, "User roles cannot be empty.");

        this.roles = Arrays.asList(roles).toString();
    }

    public boolean hasRole(final String role) {
        return roles != null && roles.contains(role);
    }

    /**
     * Validates that the user object is in a good state.
     *
     * Validates that name and email are not null and not empty.
     */
    @Override
    public void validate() {
        super.validate();

        Validate.notEmpty(email, "Email must not be null or empty.");
        Validate.inclusiveBetween(1, 128, email.length(), "Email must be between 1 and 32 characters long");

        Validate.notEmpty(roles, "Roles must not be null or empty.");
    }
}
