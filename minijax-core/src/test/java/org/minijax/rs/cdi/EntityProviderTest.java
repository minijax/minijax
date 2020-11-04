package org.minijax.rs.cdi;

import static jakarta.ws.rs.core.MediaType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.MessageBodyReader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class EntityProviderTest extends MinijaxTest {

    public static class UnknownType {
        public String foo;
    }

    public static class MyCustomType {
        public String foo;
    }

    public static class MyCustomReader implements MessageBodyReader<MyCustomType> {

        @Override
        public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
            return type == MyCustomType.class;
        }

        @Override
        public MyCustomType readFrom(final Class<MyCustomType> type, final Type genericType, final Annotation[] annotations,
                final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream)
                throws IOException, WebApplicationException {
            return null;
        }
    }

    public static class MyExplodingType {
        public String foo;
    }

    public static class MyExplodingReader implements MessageBodyReader<MyExplodingType> {

        @Override
        public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
            return type == MyExplodingType.class;
        }

        @Override
        public MyExplodingType readFrom(final Class<MyExplodingType> type, final Type genericType, final Annotation[] annotations,
                final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream)
                throws IOException, WebApplicationException {
            throw new IOException("boom");
        }
    }

    @Path("/entityprovider")
    public static class EntityProviderResource {

        @Path("/unknowntype")
        @POST
        @Consumes(TEXT_PLAIN)
        public String handleUnknownType(final UnknownType unknown) {
            return "unknown";
        }

        @POST
        @Path("/customtype")
        public String handleCustomType(final MyCustomType custom) {
            return "custom";
        }

        @POST
        @Path("/explodingtype")
        public String handleExplodingType(final MyExplodingType exploding) {
            return "exploding";
        }
    }

    @BeforeAll
    public static void setUpEntityProviderTest() {
        resetServer();
        register(MyCustomReader.class);
        register(MyExplodingReader.class);
        register(EntityProviderResource.class);
    }

    @Test
    void testUnknownType() {
        final Response response = target("/entityprovider/unknowntype").request().post(Entity.entity("blah blah blah", TEXT_PLAIN));
        assertNotNull(response);
        assertEquals(500, response.getStatus());
    }

    @Test
    void testCustomType() {
        final Response response = target("/entityprovider/customtype").request().post(Entity.entity("blah blah blah", TEXT_PLAIN));
        assertNotNull(response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testExplodingType() {
        final Response response = target("/entityprovider/explodingtype").request().post(Entity.entity("blah blah blah", TEXT_PLAIN));
        assertNotNull(response);
        assertEquals(500, response.getStatus());
    }
}
