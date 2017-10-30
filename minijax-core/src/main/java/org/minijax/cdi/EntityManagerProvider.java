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

//        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
//        final ResourceCache resourceCache = context.getResourceCache();
//
//        T instance = resourceCache.get(key);
//
//        if (instance == null) {
//            instance = sourceProvider.get();
//            resourceCache.put(key, instance);
//        }
//
//        return instance;
    }
}
