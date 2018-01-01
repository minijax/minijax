package org.minijax.cdi;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * The EntityManagerProvider is a specialty provider for JPA EntityManager instances.
 */
class EntityManagerProvider implements Provider<EntityManager> {
    private final EntityManagerFactory emf;

    public EntityManagerProvider(final EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public EntityManager get() {
        return emf.createEntityManager();
    }
}
