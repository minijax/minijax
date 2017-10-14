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
    private static Dao dao;

    public PetClinicTest() {
        register(new DefaultMustacheFactory(), MustacheFactory.class);
        register(getDao());
    }

    public static Dao getDao() {
        if (dao == null) {
            final Map<String, String> props = new HashMap<String, String>();
            props.put(JDBC_URL, "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

            final EntityManagerFactory emf = Persistence.createEntityManagerFactory("petclinic", props);

            dao = new Dao(emf);
        }
        return dao;
    }
}
