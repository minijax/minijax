package demo

import org.minijax.Minijax
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/")
class Hello {
    @GET
    fun get(): String = "Hello world!"
}

fun main(args : Array<String>) {
    Minijax().register(Hello::class.java).start()
}

