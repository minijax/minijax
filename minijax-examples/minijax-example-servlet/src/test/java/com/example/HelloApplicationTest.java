package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

public class HelloApplicationTest extends MinijaxTest {

    @Test
    public void testApp() {
        final HelloApplication app = new HelloApplication();
        final Set<Class<?>> c = app.getClasses();
        assertTrue(c.contains(HelloResource.class));
        assertTrue(c.contains(PostResource.class));
        assertTrue(c.contains(ShoutResource.class));
    }
}
