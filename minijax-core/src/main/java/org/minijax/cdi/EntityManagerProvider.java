package org.minijax.cdi;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.minijax.MinijaxRequestContext;

/**
 * The EntityManagerProvider is a specialty provider for JPA EntityManager instances.
 */
class EntityManagerProvider implements MinijaxProvider<EntityManager> {
    private final EntityManagerFactory emf;

    public EntityManagerProvider(final EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public EntityManager get(final MinijaxRequestContext context) {
        return emf.createEntityManager();
    }
}
