package com.example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.json.JsonFeature;
import org.minijax.test.MinijaxTest;

import com.example.HelloJson.Widget;

public class HelloJsonTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(JsonFeature.class);
        register(HelloJson.class);
    }


    @Test
    public void testHelloJson() {
        final Widget widget = target("/widgets/123").request().get(Widget.class);
        assertNotNull(widget);
        assertEquals("123", widget.id);
        assertEquals("foo", widget.value);
    }
}
