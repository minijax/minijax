package org.minijax.mustache;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

import com.github.mustachejava.MustacheFactory;

public class FeatureTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        register(MustacheFeature.class);
    }

    @Test
    public void testFeature() {
        final MustacheFactory mapper = getServer().getResource(MustacheFactory.class);
        assertNotNull(mapper);
    }
}
