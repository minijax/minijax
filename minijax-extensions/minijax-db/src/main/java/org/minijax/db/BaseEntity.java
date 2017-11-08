package org.minijax.db;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
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
    public UUID getId();


    /**
     * Returns the date/time when the object was created in the database.
     *
     * @return The created date/time.
     */
    public Instant getCreatedDateTime();


    /**
     * Returns the date/time when the object was last updated in the database.
     *
     * @return The updated date/time.
     */
    public Instant getUpdatedDateTime();


    /**
     * Returns whether the object is deleted.
     *
     * Data is never truly deleted from the database.  It is the responsibility
     * of all application logic to enforce the deleted flag as appropriate.
     *
     * @return True if the object is deleted.
     */
    public boolean isDeleted();

    public void setDeleted(boolean deleted);


    /**
     * Returns the date/time when the object was last deleted in the database.
     *
     * Data is never truly deleted from the database.  It is the responsibility
     * of all application logic to enforce the deleted flag as appropriate.
     *
     * @return The deleted date/time.
     */
    public Instant getDeletedDateTime();


    /**
     * Validates that the object is in a good state.
     *
     * This method is called by DAO's before persistence.
     *
     * Descending classes should override this method to enforce any special
     * validation logic.
     */
    public void validate();


    /**
     * Sorts a list of ID objects by created time (ascending).
     *
     * @param list The list of ID objects (modified in place).
     */
    public static <T extends BaseEntity> void sortByCreatedDateTime(final List<T> list) {
        Collections.sort(list, (o1, o2) -> o1.getCreatedDateTime().compareTo(o2.getCreatedDateTime()));
    }
}
