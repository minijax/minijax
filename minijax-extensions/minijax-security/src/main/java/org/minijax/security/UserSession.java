package org.minijax.security;

import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.Validate;
import org.minijax.db.DefaultBaseEntity;
import org.minijax.db.UuidConverter;

/**
 * The UserSession class represents an authenticated session which
 * can persist beyond the Java EE container session.  For example,
 * a UserSession can be used for "remember me" functionality.
 */
@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(indexes = { @Index(columnList = "USERID", unique = false) })
@NamedQuery(
        name = "UserSession.deleteByUser",
        query = "DELETE FROM UserSession s" +
                " WHERE s.userId = :userId")
@SuppressWarnings("squid:S2160")
public class UserSession extends DefaultBaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "BINARY(16)")
    @Convert(converter = UuidConverter.class)
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

    @Override
    public void validate() {
        Validate.notNull(userId, "User ID must not be null or empty.");
    }
}
