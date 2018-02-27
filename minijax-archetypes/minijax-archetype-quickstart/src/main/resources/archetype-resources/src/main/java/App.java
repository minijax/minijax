package $package;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.minijax.Minijax;

public class App {

    @GET
    @Path("/")
    public static String hello() {
        return "Hello world!";
    }

    public static void main(final String[] args) {
        new Minijax().register(App.class).start();
    }
}
