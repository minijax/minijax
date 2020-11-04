package $package;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

class AppTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(App.class);
    }

    @Test
    void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }
}
