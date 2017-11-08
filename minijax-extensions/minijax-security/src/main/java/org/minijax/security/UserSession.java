package org.minijax.security;

import javax.persistence.Cacheable;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.apache.commons.lang3.Validate;
import org.minijax.entity.DefaultBaseEntity;

/**
 * The UserSession class represents an authenticated session which
 * can persist beyond the Java EE container session.  For example,
 * a UserSession can be used for "remember me" functionality.
 */
@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQuery(
        name = "UserSession.deleteByUser",
        query = "DELETE FROM UserSession s" +
                " WHERE s.user.id = :userId")
@SuppressWarnings("squid:S2160")
public class UserSession extends DefaultBaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SecurityUser user;

    public SecurityUser getUser() {
        return user;
    }

    public void setUser(final SecurityUser user) {
        this.user = user;
    }

    @Override
    public void validate() {
        Validate.notNull(user, "User must not be null.");
        Validate.notNull(user.getId(), "User ID must not be null or empty.");
    }
}
