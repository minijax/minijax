package com.example.resources;

import static jakarta.ws.rs.core.MediaType.*;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

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
