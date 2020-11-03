
[![Build status](https://github.com/minijax/minijax/workflows/Build/badge.svg)](https://github.com/minijax/minijax/actions?query=workflow%3ABuild) [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=org.minijax%3Aminijax&metric=alert_status)](https://sonarcloud.io/dashboard/index/org.minijax:minijax)

Minijax
=======

Lightweight ~~Java EE~~ Jakarta EE

* Fast and simple JAX-RS, JSON, WebSockets, JPA, dependency injection
* Code to standard Java and Java EE API's as much as possible
* Minimal dependencies, setup, configuration, and hassle

Getting Started
---------------

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-undertow</artifactId>
    <version>0.5.2</version>
</dependency>
```

```java
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.minijax.Minijax;

@Path("/")
public class Hello {

    @GET
    public static String hello() {
        return "Hello world!";
    }

    public static void main(String[] args) {
        new Minijax()
                .register(Hello.class)
                .start();
    }
}
```

Dependency Injection
--------------------

Minijax uses the JSR-330 standard for dependency injection.  It recognizes standard annotations such as `@Provider` and `@Inject`.

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

Testing
-------

Minijax provides a rich set of testing features that integrate with standard testing libraries such as [JUnit](https://junit.org/) and [Mockito](http://site.mockito.org/).

```java
package com.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

import com.example.HelloInjection.MyResource;
import com.example.HelloInjection.MyService;

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

Learn more
----------

* [Motivation](https://github.com/minijax/minijax/wiki/Motivation) - Why use Minijax?
* [Getting Started Tutorial](https://github.com/minijax/minijax/wiki/Getting-Started) - 5 minute tutorial
* [Sample Applications](minijax-examples/) - Demonstrating Minijax capabilities
* [Supported Java EE standards](https://github.com/minijax/minijax/wiki/Java-EE-Standards) - Overview of supported API's
