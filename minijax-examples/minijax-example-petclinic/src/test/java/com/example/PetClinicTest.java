package com.example;

import org.minijax.dao.PersistenceFeature;
import org.minijax.mustache.MustacheFeature;
import org.minijax.rs.MinijaxProperties;
import org.minijax.rs.test.MinijaxTest;

public abstract class PetClinicTest extends MinijaxTest {
    protected PetClinicTest() {
        getServer()
            .property(MinijaxProperties.DB_URL, "jdbc:h2:mem:test")
            .register(PersistenceFeature.class)
            .register(MustacheFeature.class);
    }
}
