package com.example.resources;

import static jakarta.ws.rs.core.MediaType.*;

import java.net.URI;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.minijax.view.View;

import com.example.model.Owner;
import com.example.services.Dao;

@Path("/owners")
@Produces(TEXT_HTML)
public class OwnersResource {

    @Inject
    private Dao dao;

    @GET
    @Path("/search")
    public View getSearchPage() {
        return new View("search");
    }

    @GET
    public View getSearchResults(@QueryParam("q") final String q) {
        final View page = new View("owners");
        page.getModel().put("owners", dao.findOwners(q));
        return page;
    }

    @GET
    @Path("/new")
    public View getNewOwnerPage() {
        return new View("newowner");
    }

    @POST
    @Path("/new")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response handleSubmit(
            @FormParam("name") final String name,
            @FormParam("address") final String address,
            @FormParam("city") final String city,
            @FormParam("telephone") final String telephone) {

        final Owner owner = new Owner();
        owner.setName(name);
        owner.setAddress(address);
        owner.setCity(city);
        owner.setTelephone(telephone);
        dao.create(owner);
        return Response.seeOther(URI.create(owner.getUrl())).build();
    }

    @GET
    @Path("/{id}")
    public View getOwnerPage(@PathParam("id") final UUID id) {
        final Owner owner = dao.read(Owner.class, id);
        if (owner == null) {
            throw new NotFoundException();
        }

        final View page = new View("owner");
        page.getModel().put("owner", owner);
        return page;
    }

    @GET
    @Path("/{id}/edit")
    public View getEditOwnerPage(@PathParam("id") final UUID id) {
        final Owner owner = dao.read(Owner.class, id);
        if (owner == null) {
            throw new NotFoundException();
        }

        final View page = new View("editowner");
        page.getModel().put("owner", owner);
        return page;
    }

    @POST
    @Path("/{id}/edit")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response handleSubmit(
            @PathParam("id") final UUID id,
            @FormParam("name") final String name,
            @FormParam("address") final String address,
            @FormParam("city") final String city,
            @FormParam("telephone") final String telephone) {

        final Owner owner = dao.read(Owner.class, id);
        if (owner == null) {
            throw new NotFoundException();
        }

        owner.setName(name);
        owner.setAddress(address);
        owner.setCity(city);
        owner.setTelephone(telephone);
        dao.update(owner);
        return Response.seeOther(URI.create(owner.getUrl())).build();
    }
}
