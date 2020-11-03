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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.minijax.dao.DefaultBaseEntity;
import org.minijax.dao.converters.UuidConverter;

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
@Table(indexes = { @Index(columnList = "USERID") })
@NamedQuery(
        name = "PasswordChangeRequest.findByCode",
        query = "SELECT pcr FROM PasswordChangeRequest pcr" +
                " WHERE pcr.code = :code" +
                " AND pcr.deletedDateTime IS NULL")
@SuppressWarnings("squid:S2160")
public class PasswordChangeRequest extends DefaultBaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "BINARY(16)")
    @Convert(converter = UuidConverter.class)
    @NotNull
    private UUID userId;

    @Column(length = 64, unique = true)
    @Size(min = 32, max = 64)
    private String code;

    public UUID getUserId() {
        return userId;
    }

    public void setUser(final SecurityUser user) {
        userId = user.getId();
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }
}
