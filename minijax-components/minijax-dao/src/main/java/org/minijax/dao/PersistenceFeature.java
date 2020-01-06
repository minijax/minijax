package org.minijax.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.minijax.rs.MinijaxApplicationContext;

public class PersistenceFeature implements Feature {

    @Override
    public boolean configure(final FeatureContext context) {
        if (!(context instanceof MinijaxApplicationContext)) {
            throw new IllegalArgumentException("Minijax PersistenceFeature only compatible with MinijaxApplication");
        }

        final MinijaxApplicationContext app = (MinijaxApplicationContext) context;
//        app.getInjector().registerPersistence();
        registerPersistence(app);
        return true;
    }



  public void registerPersistence(final MinijaxApplicationContext app) {
      final List<String> names = PersistenceUtils.getNames("META-INF/persistence.xml");
      final Map<String, Object> props = new HashMap<>();
      props.put(PersistenceUnitProperties.CLASSLOADER, this.getClass().getClassLoader());
      props.putAll(app.getProperties());

      final Map<String, EntityManagerFactory> factories = new HashMap<>();
      boolean first = true;

      for (final String name : names) {
          final EntityManagerFactory emf = Persistence.createEntityManagerFactory(name, props);
          factories.put(name, emf);
          if (first) {
              factories.put("", emf);
              first = false;
          }
//          registerEntityManagerFactory(injector, emf, name);
//          if (first) {
//              registerEntityManagerFactory(injector, emf, "");
//              first = false;
//          }
//          initEntityManager(emf);
      }

      app.getInjector().getFieldAnnotationProcessors().put(
              PersistenceContext.class,
              new PersistenceContextAnnotationProcessor<>(factories));
  }


//  private void registerEntityManagerFactory(final MinijaxInjector injector, final EntityManagerFactory emf, final String name) {
//
//
////      final Key<EntityManagerFactory> emfKey = buildKey(EntityManagerFactory.class, name);
////      final MinijaxProvider<EntityManagerFactory> emfProvider = new SingletonProvider<>(emf);
////      providers.put(emfKey, emfProvider);
////
////      final Key<EntityManager> emKey = buildKeyPersistenceContext(name);
////      final MinijaxProvider<EntityManager> emProvider = new RequestScopedProvider<>(emKey, new EntityManagerProvider(emf));
////      providers.put(emKey, emProvider);
//  }
//
//
////  private void initEntityManager(final EntityManagerFactory emf) {
////      EntityManager em = null;
////      try {
////          em = emf.createEntityManager();
////          em.getMetamodel();
////      } finally {
////          CloseUtils.closeQuietly(em);
////      }
////  }
}
