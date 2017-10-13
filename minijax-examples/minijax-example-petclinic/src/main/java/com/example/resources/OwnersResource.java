package com.example.resources;

import static javax.ws.rs.core.MediaType.*;

import java.net.URI;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.example.model.Owner;
import com.example.services.Dao;
import com.example.view.Page;

@Path("/owners")
@Produces(TEXT_HTML)
public class OwnersResource {

    @Inject
    private Dao dao;

    @GET
    @Path("/search")
    public Page getSearchPage() {
        return new Page("search");
    }

    @GET
    public Page getSearchResults(@QueryParam("q") final String q) {
        final Page page = new Page("owners");
        page.getProps().put("owners", dao.findOwners(q));
        return page;
    }

    @GET
    @Path("/new")
    public Page getNewOwnerPage() {
        return new Page("newowner");
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
        owner.generateHandle();
        dao.create(owner);
        return Response.seeOther(URI.create(owner.getUrl())).build();
    }

    @GET
    @Path("/{id}")
    public Page getOwnerPage(@PathParam("id") final UUID id) {
        final Owner owner = dao.read(Owner.class, id);
        if (owner == null) {
            throw new NotFoundException();
        }

        final Page page = new Page("owner");
        page.getProps().put("owner", owner);
        return page;
    }

    @GET
    @Path("/{id}/edit")
    public Page getEditOwnerPage(@PathParam("id") final UUID id) {
        final Owner owner = dao.read(Owner.class, id);
        if (owner == null) {
            throw new NotFoundException();
        }

        final Page page = new Page("editowner");
        page.getProps().put("owner", owner);
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
