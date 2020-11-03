package org.minijax.security;

import java.util.UUID;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import org.minijax.dao.DefaultBaseEntity;
import org.minijax.dao.converters.UuidConverter;

/**
 * The UserSession class represents an authenticated session which
 * can persist beyond the Java EE container session.  For example,
 * a UserSession can be used for "remember me" functionality.
 */
@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(indexes = { @Index(columnList = "USERID") })
@NamedQuery(
        name = "UserSession.readByUser",
        query = "SELECT s FROM UserSession s WHERE s.userId = :userId")
@NamedQuery(
        name = "UserSession.deleteByUser",
        query = "DELETE FROM UserSession s WHERE s.userId = :userId")
@SuppressWarnings("squid:S2160")
public class UserSession extends DefaultBaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "BINARY(16)")
    @Convert(converter = UuidConverter.class)
    @NotNull
    private UUID userId;

    @Transient
    private SecurityUser user;

    public UUID getUserId() {
        return userId;
    }

    public SecurityUser getUser() {
        return user;
    }

    public void setUser(final SecurityUser user) {
        userId = user.getId();
        this.user = user;
    }
}
