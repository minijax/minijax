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
 * The ApiKey class represents a single API key for a user.
 */
@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(indexes = { @Index(columnList = "USERID") })
@NamedQuery(
        name = "ApiKey.findByUser",
        query = "SELECT k FROM ApiKey k" +
                " WHERE k.userId = :userId" +
                " AND k.deletedDateTime IS NULL")
@NamedQuery(
        name = "ApiKey.findByValue",
        query = "SELECT k FROM ApiKey k" +
                " WHERE k.value = :value")
@SuppressWarnings("squid:S2160")
public class ApiKey extends DefaultBaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "BINARY(16)")
    @Convert(converter = UuidConverter.class)
    @NotNull
    private UUID userId;

    private String name;

    @Column(length = 64, unique = true)
    @Size(min = 8, max = 64)
    private String value;

    public UUID getUserId() {
        return userId;
    }

    public void setUser(final SecurityUser user) {
        userId = user.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
