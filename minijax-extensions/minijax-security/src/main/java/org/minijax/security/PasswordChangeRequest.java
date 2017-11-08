package org.minijax.security;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;
import org.minijax.db.DefaultBaseEntity;

/**
 * The PasswordChangeRequest class represents a password change request.
 *
 * This is a central component to the "Forgot Password" / "Reset Password" flow.
 *
 * See this stackoverflow article for the general design:
 * http://stackoverflow.com/a/1102817/2051724
 */
@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(
        name = "PasswordChangeRequest.findByCode",
        query = "SELECT pcr FROM PasswordChangeRequest pcr" +
                " WHERE pcr.code = :code" +
                " AND pcr.deletedDateTime IS NULL")
@SuppressWarnings("squid:S2160")
public class PasswordChangeRequest extends DefaultBaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SecurityUser user;

    @Column(length = 64, unique = true)
    private String code;

    public SecurityUser getUser() {
        return user;
    }

    public void setUser(final SecurityUser user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public void validate() {
        Validate.isTrue(code.length() >= 32, "Password change request code must be at least 32 characters");
        Validate.notNull(user, "Password change request user must not be null");
    }
}
