package org.minijax.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import org.minijax.commons.IdUtils;
import org.minijax.dao.converters.InstantConverter;
import org.minijax.dao.converters.UuidConverter;
import org.minijax.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The BaseEntity class is the abstract base class for all web primitives.
 *
 * ID's are set by the class.  ID's are *not* populated by JPA.
 *
 * For background:
 * <ul>
 * <li>https://stackoverflow.com/questions/5031614/the-jpa-hashcode-equals-dilemma</li>
 * <li>http://www.onjava.com/pub/a/onjava/2006/09/13/dont-let-hibernate-steal-your-identity.html</li>
 * </ul>
 */
@MappedSuperclass
public abstract class DefaultBaseEntity implements BaseEntity {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBaseEntity.class);
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @Convert(converter = UuidConverter.class)
    private UUID id;

    @Convert(converter = InstantConverter.class)
    @SuppressWarnings("squid:S3437")
    private Instant createdDateTime;

    @Convert(converter = InstantConverter.class)
    @SuppressWarnings("squid:S3437")
    private Instant updatedDateTime;

    @Convert(converter = InstantConverter.class)
    @SuppressWarnings("squid:S3437")
    private Instant deletedDateTime;

    protected DefaultBaseEntity() {
        id = IdUtils.create();
    }

    /**
     * Returns the ID of the object.
     *
     * @return
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Sets the ID of the object.
     *
     * @param id
     */
    @Override
    public void setId(final UUID id) {
        this.id = id;
    }

    /**
     * Returns the date/time when the object was created in the database.
     *
     * @return The created date/time.
     */
    @Override
    public Instant getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * Sets the date/time when the object was created in the database.
     *
     * @param createdDateTime The created date/time.
     */
    public void setCreatedDateTime(final Instant createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /**
     * Returns the date/time when the object was last updated in the database.
     *
     * @return The updated date/time.
     */
    @Override
    public Instant getUpdatedDateTime() {
        return updatedDateTime;
    }

    /**
     * Sets the date/time when the object was last updated in the database.
     *
     * @param updatedDateTime The updated date/time.
     */
    public void setUpdatedDateTime(final Instant updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    /**
     * Returns whether the object is deleted.
     *
     * Data is never truly deleted from the database.  It is the responsibility
     * of all application logic to enforce the deleted flag as appropriate.
     *
     * @return True if the object is deleted.
     */
    @Override
    public boolean isDeleted() {
        return deletedDateTime != null;
    }

    /**
     * Marks the entity as deleted.
     *
     * Shortcut for <code>setDeletedDateTime(Instant.now())</code>.
     *
     * @param deleted True if the object is deleted.
     */
    @Override
    public void setDeleted(final boolean deleted) {
        deletedDateTime = deleted ? Instant.now() : null;
    }

    /**
     * Returns the date/time when the object was last deleted in the database.
     *
     * Data is never truly deleted from the database.  It is the responsibility
     * of all application logic to enforce the deleted flag as appropriate.
     *
     * @return The deleted date/time.
     */
    @Override
    public Instant getDeletedDateTime() {
        return deletedDateTime;
    }

    @PrePersist
    public void onPrePersist() {
        createdDateTime = Instant.now();
        updatedDateTime = createdDateTime;
    }

    @PreUpdate
    public void onPreUpdate() {
        updatedDateTime = Instant.now();
    }

    /**
     * Returns a hash code for this details object.
     * The hash code is completely based on the ID.
     * If a deriving class expects to mix/match several classes
     * within a hash table, then you should override and replace this.
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    /**
     * Determines if this object equals another.
     * Equality is defined as same class type and same ID.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        final DefaultBaseEntity other = (DefaultBaseEntity) obj;
        return Objects.equals(id, other.id);
    }

    public String toJson() {
        return Json.getObjectMapper().toJson(this);
    }

    public static <T extends DefaultBaseEntity> T fromJson(final Class<T> c, final String str) {
        return Json.getObjectMapper().fromJson(str, c);
    }

    /**
     * Copies all non-null properties from the other object to this object.
     *
     * @param other The other entity.
     */
    public <T extends DefaultBaseEntity> void copyNonNullProperties(final T other) {
        if (!getClass().equals(other.getClass())) {
            throw new IllegalArgumentException("Incorrect type (expected " + getClass() + ", actual " + other.getClass() + ")");
        }

        Class<?> currClass = getClass();
        while (currClass != null) {
            for (final Field field : currClass.getDeclaredFields()) {
                copyNonNullField(other, field);
            }
            currClass = currClass.getSuperclass();
        }
    }

    private <T extends DefaultBaseEntity> void copyNonNullField(final T other, final Field field) {
        if (Modifier.isStatic(field.getModifiers())) {
            return;
        }
        if (field.getName().equals("id")) {
            return;
        }
        try {
            field.setAccessible(true);
            final Object value = field.get(other);
            if (value != null) {
                field.set(this, value);
            }
        } catch (final IllegalAccessException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
