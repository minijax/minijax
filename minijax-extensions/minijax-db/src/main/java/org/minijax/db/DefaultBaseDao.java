package org.minijax.db;

import static org.minijax.db.BaseDao.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Dao class is the interface for all database access.
 */
public class DefaultBaseDao implements BaseDao {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBaseDao.class);

    @PersistenceContext
    protected EntityManager em;


    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    /**
     * Inserts a new instance in the database.
     *
     * @param obj The object to create.
     * @return The instance with ID.
     */
    @Override
    public <T extends BaseEntity> T create(final T obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
            return obj;
        } catch (final PersistenceException ex) {
            throw convertRollbackToConflict(ex);
        }
    }


    /**
     * Retrieves an object by ID.
     *
     * @param id The ID.
     * @return The object if found; null otherwise.
     */
    @Override
    public <T extends BaseEntity> T read(final Class<T> entityClass, final UUID id) {
        return em.find(entityClass, id);
    }


    /**
     * Finds a user by handle.
     * Returns the user on success.  Returns null on failure.
     *
     * @param handle The user's handle.
     * @return The user on success; null on failure.
     */
    @Override
    public <T extends NamedEntity> T readByHandle(final Class<T> entityClass, final String handle) {
        // Unfortunately @CacheIndex does not work with CriteriaBuilder, so using string query instead.
        return firstOrNull(em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.handle = :handle", entityClass)
                .setParameter("handle", handle)
                .getResultList());
    }


    /**
     * Returns a page of objects.
     *
     * @param entityClass The entity class.
     * @param page The page index (zero indexed).
     * @param pageSize The page size.
     * @return A page of objects.
     */
    @Override
    public <T extends BaseEntity> List<T> readPage(
            final Class<T> entityClass,
            final int page,
            final int pageSize) {

        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<T> cq = cb.createQuery(entityClass);
        final Root<T> root = cq.from(entityClass);
        cq.select(root);
        cq.orderBy(cb.desc(root.get("id")));
        return em.createQuery(cq)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }


    /**
     * Updates an object.
     *
     * @param obj The object to update.
     */
    @Override
    public <T extends BaseEntity> T update(final T obj) {
        try {
            em.getTransaction().begin();
            em.merge(obj);
            em.getTransaction().commit();
            return obj;
        } catch (final PersistenceException ex) {
            throw convertRollbackToConflict(ex);
        }
    }


    /**
     * Soft deletes an object.
     *
     * The data is still in the database, but with deleted flag.
     *
     * @param obj The object to delete.
     */
    @Override
    public <T extends BaseEntity> void delete(final T obj) {
        obj.setDeleted(true);
        update(obj);
    }


    /**
     * Hard deletes an object.
     *
     * This purges the data from the database.
     *
     * @param obj The object to delete.
     */
    @Override
    public <T extends BaseEntity> void purge(final T obj) {
        @SuppressWarnings("unchecked")
        final T actual = (T) em.find(obj.getClass(), obj.getId());
        if (actual != null) {
            em.getTransaction().begin();
            em.remove(actual);
            em.getTransaction().commit();
        }
    }


    /**
     * Counts all rows of a type.
     *
     * @param entityClass The entity class.
     * @return The count of rows.
     */
    @Override
    public <T extends BaseEntity> long countAll(final Class<T> entityClass) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        return em.createQuery(cq.select(cb.count(cq.from(entityClass)))).getSingleResult();
    }


    /*
     * Private helper methods.
     */


    /**
     * Converts a JPA rollback exception into a conflict exception.
     *
     * @param ex The database rollback exception.
     * @return A structured key/value conflict exception.
     */
    private static ConflictException convertRollbackToConflict(final PersistenceException ex) {
        final List<Pattern> patterns = Arrays.asList(
                Pattern.compile("Duplicate entry '(?<value>[^']+)' for key '(?<key>[^']+)'"),
                Pattern.compile("CONSTRAINT_INDEX_[a-zA-Z0-9_]+ ON PUBLIC\\.[a-zA-Z]+\\((?<key>[a-zA-Z]+)\\) VALUES \\('(?<value>[^']+)'"),
                Pattern.compile("CONSTRAINT_INDEX_[a-zA-Z0-9_]+ ON PUBLIC\\.[a-zA-Z]+\\((?<key>[a-zA-Z]+)\\) VALUES (?<value>\\d+)"));

        for (final Pattern pattern : patterns) {
            final Matcher matcher = pattern.matcher(ex.getMessage());
            if (matcher.find()) {
                final String key = matcher.group("key").toLowerCase();
                final String value = matcher.group("value");
                return new ConflictException(key, value);
            }
        }

        LOG.warn("Unrecognized RollbackException: {}", ex.getMessage(), ex);
        throw ex;
    }
}
