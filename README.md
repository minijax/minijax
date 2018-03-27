
[![Build status](https://travis-ci.org/minijax/minijax.svg?branch=master)](https://travis-ci.org/minijax/minijax) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=org.minijax:minijax)](https://sonarcloud.io/dashboard/index/org.minijax:minijax)

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
    <artifactId>minijax-core</artifactId>
    <version>0.1.2</version>
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

Learn more
----------

* [Motivation](https://github.com/minijax/minijax/wiki/Motivation) - Why use Minijax?
* [Getting Started Tutorial](https://github.com/minijax/minijax/wiki/Getting-Started) - 5 minute tutorial
* [Sample Applications](minijax-examples/) - Demonstrating Minijax capabilities
* [Supported Java EE standards](https://github.com/minijax/minijax/wiki/Java-EE-Standards) - Overview of supported API's
