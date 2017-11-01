package com.example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.json.MinijaxJsonFeature;
import org.minijax.test.MinijaxTest;

import com.example.HelloSwagger.Widget;

public class HelloSwaggerTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(MinijaxJsonFeature.class);
        register(HelloSwagger.class);
    }


    @Test
    public void testHelloJson() {
        final Widget widget = target("/widgets/123").request().get(Widget.class);
        assertNotNull(widget);
        assertEquals("123", widget.id);
        assertEquals("foo", widget.value);
    }
}
