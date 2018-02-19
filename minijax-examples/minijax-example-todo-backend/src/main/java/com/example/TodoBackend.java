package com.example;

import static javax.ws.rs.core.MediaType.*;

import java.util.*;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.annotation.*;

import org.minijax.Minijax;
import org.minijax.json.JsonFeature;

@Path("/")
@Produces(APPLICATION_JSON)
@Singleton
public class TodoBackend {

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Post {
        int id;
        String title;
        String value;
        String url;
        boolean completed;
        int order;
    }

    private final Map<Integer, Post> posts = new HashMap<>();
    private int nextId = 1;

    @GET
    public Collection<Post> read() {
        return posts.values();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response create(@Context final UriInfo uriInfo, final Post post) {
        post.id = nextId++;
        post.url = uriInfo.getRequestUri().toString() + post.id;
        posts.put(post.id, post);
        return Response.ok().entity(post).type(APPLICATION_JSON).build();
    }

    @DELETE
    public Response delete() {
        posts.clear();
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Post read(@PathParam("id") final int id) {
        return posts.get(id);
    }

    @PATCH
    @Path("/{id}")
    @Consumes(APPLICATION_JSON)
    public Post update(@PathParam("id") final int id, final Post updated) {
        final Post existing = posts.get(id);
        if (updated.title != null) {
            existing.title = updated.title;
        }
        if (updated.completed) {
            existing.completed = true;
        }
        if (updated.order != 0) {
            existing.order = updated.order;
        }
        return existing;
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        posts.remove(id);
        return Response.ok().build();
    }

    public static void main(final String[] args) {
        new Minijax()
                .register(JsonFeature.class)
                .register(TodoBackend.class)
                .allowCors("/")
                .start(8080);
    }
}
