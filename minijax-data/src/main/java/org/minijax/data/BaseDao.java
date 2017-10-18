package org.minijax.data;

import java.io.Closeable;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.Validate;
import org.minijax.util.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Dao class is the interface for all database access.
 */
public abstract class BaseDao implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);
    protected EntityManagerFactory emf;
    protected EntityManager em;

    @Inject
    protected void setEntityManagerFactory(final EntityManagerFactory emf) {
        em = emf.createEntityManager();
    }


    /**
     * Inserts a new instance in the database.
     *
     * @param obj The object to create.
     * @return The instance with ID.
     */
    public <T extends BaseEntity> T create(final T obj) {
        Validate.notNull(obj);
        Validate.isTrue(obj.getId() == null, "ID must not be set before create");
        obj.validate();

        obj.setId(IdUtils.create());
        obj.setCreatedDateTime(Instant.now());
        obj.setUpdatedDateTime(obj.getCreatedDateTime());

        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.flush();
            em.getTransaction().commit();
            return obj;
        } catch (final RollbackException ex) {
            throw convertRollbackToConflict(ex);
        }
    }


    /**
     * Retrieves an object by ID.
     *
     * @param id The ID.
     * @return The object if found; null otherwise.
     */
    public <T extends BaseEntity> T read(final Class<T> entityClass, final UUID id) {
        Validate.notNull(entityClass);
        Validate.notNull(id);

        return em.find(entityClass, id);
    }


    /**
     * Finds a user by handle.
     * Returns the user on success.  Returns null on failure.
     *
     * @param handle The user's handle.
     * @return The user on success; null on failure.
     */
    public <T extends NamedEntity> T readByHandle(final Class<T> entityClass, final String handle) {
        try {
            // Unfortunately @CacheIndex does not work with CriteriaBuilder, so using string query instead.
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.handle = :handle", entityClass)
                    .setParameter("handle", handle)
                    .getSingleResult();

        } catch (final NoResultException ex) {
            return null;
        }
    }


    /**
     * Returns a page of objects.
     *
     * @param entityClass The entity class.
     * @param page The page index (zero indexed).
     * @param pageSize The page size.
     * @return A page of objects.
     */
    public <T extends BaseEntity> List<T> readPage(
            final Class<T> entityClass,
            final int page,
            final int pageSize) {

        Validate.notNull(entityClass);
        Validate.inclusiveBetween(0, 1000, page);
        Validate.inclusiveBetween(1, 1000, pageSize);

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
    public <T extends BaseEntity> T update(final T obj) {
        Validate.notNull(obj);
        Validate.notNull(obj.getId());
        obj.validate();
        obj.setUpdatedDateTime(Instant.now());

        try {
            em.getTransaction().begin();
            em.merge(obj);
            em.flush();
            em.getTransaction().commit();
            return obj;
        } catch (final RollbackException ex) {
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
    public <T extends BaseEntity> void delete(final T obj) {
        Validate.notNull(obj);
        Validate.notNull(obj.getId());

        obj.setDeletedDateTime(Instant.now());
        update(obj);
    }


    /**
     * Hard deletes an object.
     *
     * This purges the data from the database.
     *
     * @param obj The object to delete.
     */
    public <T extends BaseEntity> void purge(final T obj) {
        Validate.notNull(obj);
        Validate.notNull(obj.getId());

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
    public <T extends BaseEntity> long countAll(final Class<T> entityClass) {
        Validate.notNull(entityClass);

        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        return em.createQuery(cq.select(cb.count(cq.from(entityClass)))).getSingleResult();
    }


    @Override
    public void close() {
        em.close();
    }


    /*
     * Private helper methods.
     */


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
    protected static <T extends BaseEntity> T firstOrNull(final List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }


    /**
     * Closes an EntityManager instance if not null.
     *
     * @param em The EntityManager.
     */
    protected static void closeQuietly(final EntityManager em) {
        if (em != null) {
            em.close();
        }
    }


    /**
     * Converts a JPA rollback exception into a conflict exception.
     *
     * @param ex The database rollback exception.
     * @return A structured key/value conflict exception.
     */
    static ConflictException convertRollbackToConflict(final RollbackException ex) {
        final List<Pattern> patterns = Arrays.asList(
                Pattern.compile("Duplicate entry '(?<value>[^']+)' for key '(?<key>[^']+)'"),
                Pattern.compile("CONSTRAINT_INDEX_[a-zA-Z0-9_]+ ON PUBLIC\\.[a-zA-Z]+\\((?<key>[a-zA-Z]+)\\) VALUES \\('(?<value>[^']+)'"));

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
