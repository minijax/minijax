package org.minijax.security;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.minijax.db.BaseDao;

public interface SecurityDao extends BaseDao {

    /**
     * Returns a list of API keys for the specified user.
     *
     * @param user The user.
     * @return The list of API keys.
     */
    default List<ApiKey> findApiKeysByUser(final SecurityUser user) {
        return getEntityManager()
                .createNamedQuery("ApiKey.findByUser", ApiKey.class)
                .setParameter("userId", user.getId())
                .getResultList();
    }


    /**
     * Finds a API key by value.
     *
     * @param value The API key value.
     * @return The API key on success; null on failure.
     */
    default ApiKey findApiKeyByValue(final String value) {
        return getEntityManager()
                .createNamedQuery("ApiKey.findByValue", ApiKey.class)
                .setParameter("value", value)
                .getSingleResult();
    }


    /**
     * Finds a user by email address.
     * Returns the user on success.  Returns null on failure.
     *
     * @param email The user's email address.
     * @return the user on success; null on failure.
     */
    default <T extends SecurityUser> T findUserByEmail(final Class<T> entityClass, final String email) {
        // Unfortunately @CacheIndex does not work with CriteriaBuilder, so using string query instead.
        return getEntityManager()
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.email = :email", entityClass)
                .setParameter("email", email)
                .getSingleResult();
    }


    /**
     * Finds a password change request by code.
     *
     * @param code The password change request code.
     * @return The password change request on success; null on failure.
     */
    default PasswordChangeRequest findPasswordChangeRequest(final String code) {
        return getEntityManager()
                .createNamedQuery("PasswordChangeRequest.findByCode", PasswordChangeRequest.class)
                .setParameter("code", code)
                .getSingleResult();
    }


    /*
     * UserSession queries.
     */


    /**
     * Deletes all sessions for a user by user ID.
     *
     * This should be used when there is evidence for potential security or
     * cookie tampering.
     *
     * @param userId The user ID.
     */
    default void deleteUserSessionsByUser(final UUID userId) {
        final EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.createNamedQuery("UserSession.deleteByUser", UserSession.class)
                .setParameter("userId", userId)
                .executeUpdate();
        em.getTransaction().commit();
    }

}
