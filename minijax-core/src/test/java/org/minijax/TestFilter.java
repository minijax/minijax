package org.minijax;

import java.io.IOException;

import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
public class TestFilter implements ContainerRequestFilter {


    public TestFilter () {
        System.out.println("Created a security filter!");
    }


    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty("filter", true);
    }
}
