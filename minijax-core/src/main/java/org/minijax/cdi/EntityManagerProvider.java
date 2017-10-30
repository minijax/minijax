package org.minijax.cdi;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagerProvider implements Provider<EntityManager> {
    private final EntityManagerFactory emf;

    public EntityManagerProvider(final EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public EntityManager get() {
        return emf.createEntityManager();
    }
}
