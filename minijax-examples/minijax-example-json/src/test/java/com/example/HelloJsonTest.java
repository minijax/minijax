package com.example;

import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import java.util.Collection;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.minijax.json.JsonFeature;
import org.minijax.rs.test.MinijaxTest;

import com.example.HelloJson.Widget;

public class HelloJsonTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(JsonFeature.class);
        register(HelloJson.class);
    }

    @Test
    public void testEmptyCollection() {
        HelloJson.WIDGETS.clear();

        final Collection<Widget> widgets = target("/widgets").request().get(new GenericType<Collection<Widget>>() {});
        assertNotNull(widgets);
        assertTrue(widgets.isEmpty());
    }

    @Test
    public void testSingleWidget() {
        HelloJson.WIDGETS.clear();
        HelloJson.WIDGETS.put("1", new Widget("1", "Hello"));

        final Collection<Widget> widgets = target("/widgets").request().get(new GenericType<Collection<Widget>>() {});
        assertNotNull(widgets);
        assertEquals(1, widgets.size());

        final Widget widget = target("/widgets/1").request().get(Widget.class);
        assertNotNull(widget);
        assertEquals("1", widget.id);
        assertEquals("Hello", widget.value);
    }

    @Test
    public void testCreateWidget() {
        HelloJson.WIDGETS.clear();

        final String json = "{\"id\":\"2\",\"value\":\"World\"}";

        final Response response = target("/widgets").request().post(Entity.entity(json, APPLICATION_JSON_TYPE));
        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals("/widgets/2", response.getLocation().toString());

        final Widget widget = target(response.getLocation()).request().get(Widget.class);
        assertNotNull(widget);
        assertEquals("2", widget.id);
        assertEquals("World", widget.value);
    }
}
