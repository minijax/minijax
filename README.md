
[![Build status](https://travis-ci.org/minijax/minijax.svg?branch=master)](https://travis-ci.org/minijax/minijax) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=org.minijax:minijax)](https://sonarcloud.io/dashboard/index/org.minijax:minijax) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.minijax/minijax/badge.svg)](http://mvnrepository.com/artifact/org.minijax)

Minijax
=======

Lightweight subset of Java EE standards

* Minimal dependencies, setup, configuration, and hassle
* Code to standard Java and Java EE API's as much as possible

Getting Started
---------------

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-core</artifactId>
    <version>0.0.35</version>
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
                .run();
    }
}
```

Learn more
----------

* [Motivation](https://github.com/minijax/minijax/wiki/Motivation) - Why use Minijax?
* [Getting Started Tutorial](https://github.com/minijax/minijax/wiki/Getting-Started) - 5 minute tutorial
* [Sample Applications](minijax-examples/index.html) - Demonstrating Minijax capabilities
* [Supported Java EE standards](https://github.com/minijax/minijax/wiki/Java-EE-Standards) - Overview of supported API's
