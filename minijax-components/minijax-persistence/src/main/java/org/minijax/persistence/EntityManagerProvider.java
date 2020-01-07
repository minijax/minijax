package org.minijax.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.minijax.cdi.MinijaxProvider;

/**
 * The EntityManagerProvider is a specialty provider for JPA EntityManager instances.
 */
class EntityManagerProvider implements MinijaxProvider<EntityManager> {
    private final EntityManagerFactory emf;

    public EntityManagerProvider(final EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public EntityManager get(final Object context) {
        return emf.createEntityManager();
    }
}
