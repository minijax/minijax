
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
    <version>0.0.20</version>
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

Learn more at [https://minijax.org](https://minijax.org)

Developers
----------

To build:

```bash
mvn clean install
```

To release:

```bash
mvn -B release:prepare release:perform
```

Release requirements:
* GPG keys to sign for Maven Central
* AWS credentials to update https://minijax.org
