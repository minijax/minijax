package org.minijax.json;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

public class FeatureTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(JsonFeature.class);
    }

    @Test
    public void testFeature() {
        assertNotNull(getServer().getResource(MinijaxJsonReader.class));
        assertNotNull(getServer().getResource(MinijaxJsonWriter.class));
    }
}
