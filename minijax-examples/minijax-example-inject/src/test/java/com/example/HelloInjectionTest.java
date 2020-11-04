package com.example;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

import com.example.HelloInjection.MyResource;
import com.example.HelloInjection.MyService;

class HelloInjectionTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        final MyService mockService = mock(MyService.class);
        when(mockService.shout(eq("friend"))).thenReturn("FRIEND");
        when(mockService.shout(eq("cody"))).thenReturn("CODY");

        register(mockService, MyService.class);
        register(MyResource.class);
    }

    @Test
    void testDefault() {
        assertEquals("Hello FRIEND", target("/").request().get(String.class));
    }

    @Test
    void testQueryString() {
        assertEquals("Hello CODY", target("/?name=cody").request().get(String.class));
    }
}
