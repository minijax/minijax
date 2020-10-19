package com.example;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/post")
public class PostResource {

    @POST
    public String handlePost(final String contentBody) {
        return "You posted: " + contentBody;
    }
}
