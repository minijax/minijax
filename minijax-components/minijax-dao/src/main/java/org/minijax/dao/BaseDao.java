package org.minijax.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

/**
 * The Dao class is the interface for all database access.
 */
public interface BaseDao {

    EntityManager getEntityManager();

    /**
     * Inserts a new instance in the database.
     *
     * @param obj The object to create.
     * @return The instance with ID.
     */
    <T extends BaseEntity> T create(final T obj);

    /**
     * Retrieves an object by ID.
     *
     * @param id The ID.
     * @return The object if found; null otherwise.
     */
    <T extends BaseEntity> T read(final Class<T> entityClass, final UUID id);

    /**
     * Finds a user by handle.
     * Returns the user on success.  Returns null on failure.
     *
     * @param handle The user's handle.
     * @return The user on success; null on failure.
     */
    <T extends NamedEntity> T readByHandle(final Class<T> entityClass, final String handle);

    /**
     * Returns a page of objects.
     *
     * @param entityClass The entity class.
     * @param page The page index (zero indexed).
     * @param pageSize The page size.
     * @return A page of objects.
     */
    <T extends BaseEntity> List<T> readPage(
            final Class<T> entityClass,
            final int page,
            final int pageSize);

    /**
     * Updates an object.
     *
     * @param obj The object to update.
     */
    <T extends BaseEntity> T update(final T obj);

    /**
     * Soft deletes an object.
     *
     * The data is still in the database, but with deleted flag.
     *
     * @param obj The object to delete.
     */
    <T extends BaseEntity> void delete(final T obj);

    /**
     * Hard deletes an object.
     *
     * This purges the data from the database.
     *
     * @param obj The object to delete.
     */
    <T extends BaseEntity> void purge(final T obj);

    /**
     * Counts all rows of a type.
     *
     * @param entityClass The entity class.
     * @return The count of rows.
     */
    <T extends BaseEntity> long countAll(final Class<T> entityClass);

    /**
     * Returns null if the list is empty.
     * Returns the first element otherwise.
     *
     * JPA getSingleResult() throws an exception if no results,
     * which is an annoying design.  So instead you can call
     * getResultList() and wrap it with firstOrNull(), which is
     * the more expected result.
     *
     * @param list
     * @return
     */
    public static <T extends BaseEntity> T firstOrNull(final List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }
}
