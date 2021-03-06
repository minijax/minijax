package org.minijax.dao;

import java.io.Serializable;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * The BaseEntity class is the abstract base class for all web primitives.
 */
public interface BaseEntity extends Serializable {

    /**
     * Returns the ID of the object.
     *
     * @return
     */
    UUID getId();

    void setId(UUID id);

    /**
     * Returns the date/time when the object was created in the database.
     *
     * @return The created date/time.
     */
    Instant getCreatedDateTime();

    /**
     * Returns the date/time when the object was last updated in the database.
     *
     * @return The updated date/time.
     */
    Instant getUpdatedDateTime();

    /**
     * Returns whether the object is deleted.
     *
     * Data is never truly deleted from the database.  It is the responsibility
     * of all application logic to enforce the deleted flag as appropriate.
     *
     * @return True if the object is deleted.
     */
    boolean isDeleted();

    void setDeleted(boolean deleted);

    /**
     * Returns the date/time when the object was last deleted in the database.
     *
     * Data is never truly deleted from the database.  It is the responsibility
     * of all application logic to enforce the deleted flag as appropriate.
     *
     * @return The deleted date/time.
     */
    Instant getDeletedDateTime();

    /**
     * Sorts a list of ID objects by created time (ascending).
     *
     * @param list The list of ID objects (modified in place).
     */
    static <T extends BaseEntity> void sortByCreatedDateTime(final List<T> list) {
        list.sort(Comparator.comparing(BaseEntity::getCreatedDateTime));
    }
}
