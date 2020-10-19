package org.minijax.asadmin;

import org.minijax.Minijax;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/")
public class MinijaxASAdmin {

    @GET
    public static String hello() {
        return "Hello world!";
    }

    public static void main(final String[] args) throws Exception {
        //AutoDeployWatcher.go();
        new Thread(new AutoDeployWatcher()).start();

        new Minijax()
                .register(MinijaxASAdmin.class)
                .start();
    }
}
