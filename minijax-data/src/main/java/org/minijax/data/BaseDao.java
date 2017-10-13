/*
 * AJIBOT CONFIDENTIAL
 * __________________
 *
 *  2017 Ajibot Inc
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Ajibot Inc and its suppliers, if any.
 * The intellectual and technical concepts contained herein
 * are proprietary to Ajibot Inc and its suppliers and may
 * be covered by U.S. and Foreign Patents, patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from Ajibot Inc.
 */
package org.minijax.data;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public abstract class BaseDao {
    private static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);
    protected final EntityManagerFactory emf;


    /**
     * Creates a new Dao.
     *
     * @param emf The JPA entity manager factory.
     */
    public BaseDao(final EntityManagerFactory emf) {
        this.emf = emf;
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

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
            return obj;
        } catch (final RollbackException ex) {
            throw convertRollbackToConflict(ex);
        } finally {
            closeQuietly(em);
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

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            return em.find(entityClass, id);
        } finally {
            closeQuietly(em);
        }
    }


    /**
     * Finds a user by handle.
     * Returns the user on success.  Returns null on failure.
     *
     * @param handle The user's handle.
     * @return The user on success; null on failure.
     */
    public <T extends NamedEntity> T readByHandle(final Class<T> entityClass, final String handle) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            // Unfortunately @CacheIndex does not work with CriteriaBuilder, so using string query instead.
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.handle = :handle", entityClass)
                    .setParameter("handle", handle)
                    .getSingleResult();

        } catch (final NoResultException ex) {
            return null;
        } finally {
            closeQuietly(em);
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

        EntityManager em = null;
        try {
            em = emf.createEntityManager();

            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<T> cq = cb.createQuery(entityClass);
            final Root<T> root = cq.from(entityClass);
            cq.select(root);
            cq.orderBy(cb.desc(root.get("id")));
            return em.createQuery(cq)
                    .setFirstResult(page * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

        } finally {
            closeQuietly(em);
        }
    }


    /**
     * Updates an object.
     *
     * @param obj The object to update.
     */
    public <T extends BaseEntity> void update(final T obj) {
        Validate.notNull(obj);
        Validate.notNull(obj.getId());
        obj.validate();
        obj.setUpdatedDateTime(Instant.now());

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.merge(obj);
            em.getTransaction().commit();
        } catch (final RollbackException ex) {
            throw convertRollbackToConflict(ex);
        } finally {
            closeQuietly(em);
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

        EntityManager em = null;
        try {
            em = emf.createEntityManager();

            @SuppressWarnings("unchecked")
            final T actual = (T) em.find(obj.getClass(), obj.getId());
            if (actual != null) {
                em.getTransaction().begin();
                em.remove(actual);
                em.getTransaction().commit();
            }

        } finally {
            closeQuietly(em);
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

        EntityManager em = null;
        try {
            em = emf.createEntityManager();

            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            return em.createQuery(cq.select(cb.count(cq.from(entityClass)))).getSingleResult();

        } finally {
            closeQuietly(em);
        }
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
