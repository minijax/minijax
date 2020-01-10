package com.example;

import org.minijax.commons.MinijaxProperties;
import org.minijax.mustache.MustacheFeature;
import org.minijax.rs.persistence.PersistenceFeature;
import org.minijax.rs.test.MinijaxTest;

public abstract class PetClinicTest extends MinijaxTest {
    protected PetClinicTest() {
        getServer()
            .property(MinijaxProperties.DB_URL, "jdbc:h2:mem:test")
            .register(PersistenceFeature.class)
            .register(MustacheFeature.class);
    }
}
