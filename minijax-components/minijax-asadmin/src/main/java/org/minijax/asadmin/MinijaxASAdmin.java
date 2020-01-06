package org.minijax.asadmin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.minijax.Minijax;

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
