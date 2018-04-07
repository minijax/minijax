package org.minijax.swagger.resources;

import static javax.ws.rs.core.MediaType.*;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("myapi")
@Api(value = "myapi", description = "myapi routes", produces = "application/json")
public class ResourceWithComplexBodyInputType {

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("testPostWithBody")
    @ApiOperation(response = String.class, value = "Returns string with provided parameter")
    public String testPostWithBody(@ApiParam(name = "body", value = "input parameters in json form") final
                                   List<ClassWithString> items) {

        final List<String> result = Lists.newArrayList();
        return String.format("Given parameters are %s", Arrays.toString(result.toArray(new String[result.size()])));
    }

}