package com.example;

import static javax.ws.rs.core.MediaType.*;

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
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.minijax.Minijax;
import org.minijax.json.JsonFeature;

@Path("/widgets")
@Produces(APPLICATION_JSON)
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
