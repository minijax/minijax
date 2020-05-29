package org.minijax.security;

import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;

import org.mindrot.jbcrypt.BCrypt;
import org.minijax.dao.DefaultNamedEntity;

/**
 * The SecurityUser class is an abstract base class for "security" entities
 * that can login, logout, change passwords, etc.
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("squid:S2160")
public abstract class SecurityUser extends DefaultNamedEntity {
    private static final long serialVersionUID = 1L;

    @Column(length = 128, unique = true)
    private String email;

    @XmlTransient
    @NotNull
    @Size(min = 1, max = 128)
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

    public void setPassword(final String password) {
        passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
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
        this.roles = Arrays.asList(roles).toString();
    }

    public boolean hasRole(final String role) {
        return roles != null && roles.contains(role);
    }
}
