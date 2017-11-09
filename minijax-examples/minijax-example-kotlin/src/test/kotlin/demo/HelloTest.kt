package demo

import kotlin.test.assertEquals
import org.junit.Test
import org.minijax.test.MinijaxTest

class HelloTest : MinijaxTest() {

    @Test
    fun testAssert() : Unit {
        register(Hello::class.java)
        assertEquals("Hello world!", MinijaxTest.target("/").request().get(String::class.java))
    }
}
