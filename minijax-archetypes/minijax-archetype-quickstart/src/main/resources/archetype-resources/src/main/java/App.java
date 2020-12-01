package $package;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.minijax.Minijax;

@Path("/")
public class App {

    @GET
    public static String hello() {
        return "Hello world!";
    }

    public static void main(final String[] args) {
        new Minijax().register(App.class).start();
    }
}
