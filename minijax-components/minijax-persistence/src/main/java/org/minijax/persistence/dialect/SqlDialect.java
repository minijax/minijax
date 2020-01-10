package org.minijax.persistence.dialect;

import java.util.List;

import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxNativeQuery;
import org.minijax.persistence.MinijaxQuery;

public interface SqlDialect {

    /*
     * EntityManagerFactory
     */

    <T> void createTables(MinijaxEntityManager em, Class<T> entityClass);

    /*
     * EntityManager
     */

    <T> void persist(MinijaxEntityManager em, T entity);

    <T> T merge(MinijaxEntityManager em, T entity);

    <T> void remove(MinijaxEntityManager em, T entity);

    <T> T find(MinijaxEntityManager em, Class<T> entityClass, Object primaryKey);

    /*
     * NativeQuery
     */

    <T> List<T> getResultList(MinijaxEntityManager em, MinijaxNativeQuery<T> query);

    <T> T getSingleResult(MinijaxEntityManager em, MinijaxNativeQuery<T> query);

    <T> int executeUpdate(MinijaxEntityManager em, MinijaxNativeQuery<T> query);

    /*
     * CriteriaQuery
     */

    <T> List<T> getResultList(MinijaxEntityManager em, MinijaxQuery<T> query);

    <T> T getSingleResult(MinijaxEntityManager em, MinijaxQuery<T> query);

    <T> int executeUpdate(MinijaxEntityManager em, MinijaxQuery<T> query);
}
