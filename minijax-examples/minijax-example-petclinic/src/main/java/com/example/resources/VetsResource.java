package com.example.resources;

import static javax.ws.rs.core.MediaType.*;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.minijax.view.View;

import com.example.model.Vet;
import com.example.services.Dao;

@Path("/vets")
@Produces(TEXT_HTML)
public class VetsResource {

    @Inject
    private Dao dao;

    @GET
    public View getVets() {
        final View page = new View("vets");
        page.getModel().put("vets", dao.readPage(Vet.class, 0, 1000));
        return page;
    }
}
