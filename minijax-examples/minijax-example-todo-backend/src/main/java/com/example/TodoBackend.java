package com.example;

import static jakarta.ws.rs.core.MediaType.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.minijax.Minijax;
import org.minijax.json.JsonFeature;

@Path("/")
@Produces(APPLICATION_JSON)
@Singleton
public class TodoBackend {

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
                .start();
    }
}
