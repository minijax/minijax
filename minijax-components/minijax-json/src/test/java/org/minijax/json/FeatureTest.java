package org.minijax.json;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class FeatureTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        register(JsonFeature.class);
    }

    @Test
    void testFeature() {
        assertNotNull(getServer().getResource(MinijaxJsonReader.class));
        assertNotNull(getServer().getResource(MinijaxJsonWriter.class));
    }
}
