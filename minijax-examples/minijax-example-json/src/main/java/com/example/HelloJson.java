package com.example;

import static jakarta.ws.rs.core.MediaType.*;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.minijax.Minijax;
import org.minijax.json.JsonFeature;

@Path("/widgets")
@Produces(APPLICATION_JSON)
public class HelloJson {

    public static class Widget {
        String id;
        String value;

        public Widget() {
        }

        public Widget(final String id, final String value) {
            this.id = id;
            this.value = value;
        }
    }

    static final Map<String, Widget> WIDGETS = new HashMap<>();

    @GET
    public static Collection<Widget> read() {
        return WIDGETS.values();
    }

    @GET
    @Path("/{id}")
    public static Widget read(@PathParam("id") final String id) {
        return WIDGETS.get(id);
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public static Response create(final Widget widget) {
        WIDGETS.put(widget.id, widget);
        return Response.created(URI.create("/widgets/" + widget.id)).build();
    }

    public static void main(final String[] args) {
        new Minijax()
                .register(JsonFeature.class)
                .register(HelloJson.class)
                .start();
    }
}
