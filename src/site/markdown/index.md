
[![Build status](https://travis-ci.org/minijax/minijax.svg?branch=master)](https://travis-ci.org/minijax/minijax) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=org.minijax:minijax)](https://sonarcloud.io/dashboard/index/org.minijax:minijax)

Lightweight subset of Java EE standards

* Minimal dependencies, setup, configuration, and hassle
* Code to standard Java and Java EE API's

Quick Start
-----------

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-core</artifactId>
    <version>0.0.1</version>
</dependency>
```

```java
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.minijax.Minijax;

public class Hello {

    @GET
    @Path("/")
    public static String hello() {
        return "Hello world!";
    }

    public static void main(String[] args) {
        new Minijax()
                .register(Hello.class)
                .run(8080);
    }
}
```

Learn More
----------

* [Motivation](motivation.html) - Why use Minijax?
* [Getting Started Tutorial](gettingstarted.html) - 5 minute tutorial
* [Sample Applications](minijax-examples/index.html) - Demonstrating Minijax capabilities
* [Supported Java EE standards](javaee.html) - Overview of supported API's
