package com.example;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.minijax.Minijax;
import org.minijax.json.JsonFeature;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Api
public class HelloSwagger {

    @POST
    @Path("/shout")
    @Consumes(MediaType.TEXT_PLAIN)
    @ApiOperation("Shouts an input string")
    public static String shout(final String s) {
        return s.toUpperCase();
    }

    public static void main(final String[] args) {
        new Minijax()
                .register(io.swagger.jaxrs.listing.ApiListingResource.class)
                .register(io.swagger.jaxrs.listing.SwaggerSerializers.class)
                .register(JsonFeature.class)
                .register(HelloSwagger.class)
                .run(8080);
    }
}
