package com.example;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.minijax.test.MinijaxTest;

import com.example.services.Dao;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

public abstract class PetClinicTest extends MinijaxTest {
    private static EntityManagerFactory emf;

    public PetClinicTest() {
        register(new DefaultMustacheFactory(), MustacheFactory.class);
        register(getEntityManagerFactory(), EntityManagerFactory.class);
        register(Dao.class);
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            final Map<String, String> props = new HashMap<String, String>();
            props.put(JDBC_URL, "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            emf = Persistence.createEntityManagerFactory("petclinic", props);
        }
        return emf;
    }
}
