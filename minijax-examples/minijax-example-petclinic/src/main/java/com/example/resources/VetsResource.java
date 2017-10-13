package com.example.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.example.model.Vet;
import com.example.services.Dao;
import com.example.view.Page;

@Path("/vets")
@Produces(MediaType.TEXT_HTML)
public class VetsResource {

    @Inject
    private Dao dao;

    @GET
    public Page getVets() {
        final Page page = new Page("vets");
        page.getProps().put("vets", dao.readPage(Vet.class, 0, 1000));
        return page;
    }
}
