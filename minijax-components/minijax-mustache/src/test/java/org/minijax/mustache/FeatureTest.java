package org.minijax.mustache;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

import com.github.mustachejava.MustacheFactory;

public class FeatureTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(MustacheFeature.class);
    }

    @Test
    public void testFeature() {
        final MustacheFactory mapper = getServer().getResource(MustacheFactory.class);
        assertNotNull(mapper);
    }
}
