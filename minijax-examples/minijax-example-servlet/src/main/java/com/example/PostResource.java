package com.example;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/post")
public class PostResource {

    @POST
    public String handlePost(final String contentBody) {
        return "You posted: " + contentBody;
    }
}
