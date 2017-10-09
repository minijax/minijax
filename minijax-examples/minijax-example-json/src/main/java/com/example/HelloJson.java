package com.example;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.minijax.Minijax;
import org.minijax.json.MinijaxJsonFeature;

public class HelloJson {


    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
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


    private static final Map<String, Widget> widgets;
    static {
        widgets = new HashMap<>();
        widgets.put("123", new Widget("123", "foo"));
        widgets.put("456", new Widget("456", "bar"));
        widgets.put("789", new Widget("789", "baz"));
    }


    @GET
    @Path("/widgets")
    @Produces(MediaType.APPLICATION_JSON)
    public static Collection<Widget> read() {
        return widgets.values();
    }


    @GET
    @Path("/widgets/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Widget read(@PathParam("id") final String id) {
        return widgets.get(id);
    }


    @POST
    @Path("/widgets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response create(final Widget widget) {
        widgets.put(widget.id, widget);
        return Response.created(URI.create("/widgets/" + widget.id)).build();
    }


    public static void main(final String[] args) {
        new Minijax()
                .register(MinijaxJsonFeature.class)
                .register(HelloJson.class)
                .run(8081);
    }
}
