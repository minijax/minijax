package com.example.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.example.view.Page;

public class DefaultResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Page getDefaultPage() {
        return new Page("home");
    }
}
