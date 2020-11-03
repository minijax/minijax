
Hello World
===========

This example demonstrates:

* Setting up **Dependency Injection**
* Classpath scanning
* Testing using a mock

The source code is available at [minijax-examples/minijax-example-inject](https://github.com/minijax/minijax/tree/master/minijax-examples/minijax-example-inject)

Estimated reading time: 5 minutes

pom.xml
-------

Building on the [minijax-example-hello](https://github.com/minijax/minijax/tree/master/minijax-examples/minijax-example-hello), there is one new dependency:  [Mockito](http://site.mockito.org/), one of the most popular "mocking" frameworks for Java.

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>${mockito.version}</version>
    <scope>test</scope>
</dependency>
```

HelloInjection.java
-------------------

HelloInjection.java includes all of the Java code for our application:

1. A service interface
2. A service implementation
3. A resource endpoint

```java
package com.example;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ext.Provider;

import org.minijax.Minijax;

public class HelloInjection {

    public interface MyService {

        String shout(String str);
    }

    @Provider
    public class MyServiceImpl implements MyService {

        @Override
        public String shout(final String str) {
            return str.toUpperCase();
        }
    }

    @Path("/")
    public class MyResource {

        @Inject
        private MyService service;

        @GET
        public String get(@QueryParam("name") @DefaultValue("friend") final String name) {
            return "Hello " + service.shout(name);
        }
    }

    public static void main(final String[] args) {
        new Minijax()
                .packages("com.example")
                .start();
    }
}
```

Key points:

1. A service is defined using a simple Java `interface`
2. A service implementation can be "auto registered" if it implements the interface and has the `@Provider` annotation
3. All providers and resources can be auto registered using `Minijax.packages()`

HelloInjectionTest.java
-----------------------

While the default service implementation `MyServiceImpl` is ridiculously simple, let's mock it for demonstration purposes.

```java
public class HelloInjectionTest extends MinijaxTest {

    @Before
    public void setUp() {
        final MyService mockService = mock(MyService.class);
        when(mockService.shout(eq("friend"))).thenReturn("FRIEND");
        when(mockService.shout(eq("cody"))).thenReturn("CODY");

        register(mockService, MyService.class);
        register(MyResource.class);
    }

    @Test
    public void testDefault() {
        assertEquals("Hello FRIEND", target("/").request().get(String.class));
    }

    @Test
    public void testQueryString() {
        assertEquals("Hello CODY", target("/?name=cody").request().get(String.class));
    }
}
```

Next
----

* [JSON Example](../minijax-example-json) - Learn how to read/write JSON from resource methods
* [Mustache Example](../minijax-example-mustache) - Learn how to render Mustache templates
* [Websocket Example](../minijax-example-websocket) - Learn how to enable websocket endpoints
