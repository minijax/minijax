package org.minijax.swagger.resources;

import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.minijax.multipart.Part;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("test")
@Api(value = "test", description = "test routes", produces = "application/json")
public class ResourceWithFormData {

    @POST
    @Path("/document/{documentName}.json")
    @ApiOperation(value = "uploadAttachAndParseUserDocument", notes = "Uploads, parses, and attaches the document to the user's job application.", position = 509)
    public String uploadAttachAndParseUserDocument(
            @PathParam("documentName") final String documentName,
            @FormParam("document") final Part detail,
            @FormParam("document2") final Part bodyPart,
            @FormParam("input") final InputStream input,
            @FormParam("id") final Integer id) throws Exception {
        return "";
    }
}
