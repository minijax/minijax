package org.minijax.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The BaseEntity class is the abstract base class for all web primitives.
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = UuidGenerator.ID_GENERATOR_NAME)
    @Column(columnDefinition = "BINARY(16)")
    @Convert(converter = UuidConverter.class)
    private UUID id;

    @Convert(converter = InstantConverter.class)
    @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant createdDateTime;

    @Convert(converter = InstantConverter.class)
    @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant updatedDateTime;

    @Convert(converter = InstantConverter.class)
    @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant deletedDateTime;


    public BaseEntity() {
        // For Jackson
    }


    public BaseEntity(final UUID id) {
        setId(id);
    }


    /**
     * Returns the ID of the object.
     *
     * @return
     */
    public UUID getId() {
        return id;
    }


    /**
     * Sets the ID of the object.
     *
     * @param id
     */
    public void setId(final UUID id) {
        this.id = id;
    }


    /**
     * Returns the date/time when the object was created in the database.
     *
     * @return The created date/time.
     */
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
    public void setDeleted(final boolean deleted) {
        setDeletedDateTime(deleted ? Instant.now() : null);
    }


    /**
     * Returns the date/time when the object was last deleted in the database.
     *
     * Data is never truly deleted from the database.  It is the responsibility
     * of all application logic to enforce the deleted flag as appropriate.
     *
     * @return The deleted date/time.
     */
    public Instant getDeletedDateTime() {
        return deletedDateTime;
    }


    /**
     * Sets the date/time when the object was last deleted in the database.
     *
     * Data is never truly deleted from the database.  It is the responsibility
     * of all application logic to enforce the deleted flag as appropriate.
     *
     * @param deletedDateTime The deleted date/time.
     */
    public void setDeletedDateTime(final Instant deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }


    /**
     * Validates that the object is in a good state.
     *
     * This method is called by DAO's before persistence.
     *
     * Descending classes should override this method to enforce any special
     * validation logic.
     */
    public void validate() {
        // Descendant classes should override this method for custom validation
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

        final BaseEntity other = (BaseEntity) obj;
        return Objects.equals(id, other.id);
    }


    /**
     * Returns a SQL hint for super admin debugging.
     *
     * Note that this value *IS NOT* used in any database code.  This is merely
     * to help developers quickly navigate their developer databases.
     *
     * @return A SQL hint.
     */
    public String getSqlHint() {
        final String tableName = getClass().getSimpleName().toUpperCase();
        final String hexId = getId().toString().replaceAll("-", "");
        return "SELECT * FROM `" + tableName + "` WHERE ID=UNHEX('" + hexId + "');";
    }


    /**
     * Copies all non-null properties from the other object to this object.
     *
     * @param other The other entity.
     */
    public <T extends BaseEntity> void copyNonNullProperties(final T other) {
        if (!getClass().equals(other.getClass())) {
            throw new IllegalArgumentException("Incorrect type (expected " + getClass() + ", actual " + other.getClass() + ")");
        }

        Class<?> currClass = getClass();
        while (currClass != null) {
            for (final Field field : currClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    final Object value = field.get(other);
                    if (value != null) {
                        field.set(this, value);
                    }
                } catch (final IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
            currClass = currClass.getSuperclass();
        }
    }


    /**
     * Sorts a list of ID objects by created time (ascending).
     *
     * @param list The list of ID objects (modified in place).
     */
    public static <T extends BaseEntity> void sortByCreatedDateTime(final List<T> list) {
        Collections.sort(list, (o1, o2) -> o1.getCreatedDateTime().compareTo(o2.getCreatedDateTime()));
    }
}
