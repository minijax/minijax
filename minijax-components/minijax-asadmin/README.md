
Hello World
===========

This example demonstrates:

* Basic intro to Minijax
* Setting up a Maven project
* Creating a **Resource Class**
* Creating a **Resource Method**
* Testing

The source code is available at [minijax-examples/minijax-example-hello](https://github.com/minijax/minijax/tree/master/minijax-examples/minijax-example-hello)

Estimated reading time: 5 minutes

Lines of code:

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                             2             10              0             29
Maven                            1              0              0             26
-------------------------------------------------------------------------------
SUM:                             3             10              0             55
-------------------------------------------------------------------------------
```

pom.xml
-------

Let's start with the Maven pom.xml.  If you're not familiar with Maven, I strongly encourage you to [start there](https://maven.apache.org/pom.html).

The pom is short and sweet with only two dependencies:

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>${junit.version}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-undertow</artifactId>
    <version>${project.version}</version>
</dependency>
```

The "minijax-undertow" dependency includes everything we need for a simple demonstration:

* Embedded Undertow for web server
* JAX-RS implementation for routing

Hello.java
----------

Hello.java includes all of the Java code for our application:

```java
package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.minijax.Minijax;

@Path("/")
public class Hello {

    @GET
    public static String hello() {
        return "Hello world!";
    }

    public static void main(final String[] args) {
        new Minijax().register(Hello.class).start();
    }
}
```

Key points:

```java
@Path("/")
public class Hello {
```

A **Resource Class** must have a `@Path` annotation.

```java
@GET
public static String hello() {
    return "Hello world!";
}
```

A **Resource Method** must have an HTTP verb annotation such as `@GET` or `@POST`.

This is a tiny preview of JAX-RS capabilities.  For a deeper review, I recommend Oracle's [Chapter 13 Building RESTful Web Services with JAX-RS and Jersey](https://docs.oracle.com/cd/E19226-01/820-7627/6nisfjmk8/index.html).  While that documentation is for Jersey, nearly all JAX-RS features are available in Minijax.

Back to Hello.java:

```java
public static void main(final String[] args) {
    new Minijax().register(Hello.class).start();
}
```

Obviously the `main` function is the application entry point.

`new Minijax()` creates a new Minijax container.  

`register(Hello.class)` registers our **Resource Class** and **Resource Method**.

`start()` runs the container on port 8080.  The `start` method starts Undertow.  Undertow runs in the background, which keeps the application alive indefinitely.

You can run the Hello World example:

```bash
mvn exec:java -Dexec.mainClass="com.example.Hello"
```

You can view the results in your web browser:

> <http://localhost:8080>

Notice that we did not have to manually install Tomcat or an Application Server.  Notice that Maven did pretty much everything for us.

In a traditional Java EE application, you would not include a `main` function.  Instead, you would bundle as a WAR and run in an Application Server such as JBoss, Wildfly, Weblogic, etc.

In modern Java web development, there is a clear trend *away* from Application Servers.  The new model is to create a "fat jar" and simply run `java -jar myjar.jar`.

The fat jar model has many benefits:

* Simple to build and deploy
* No installation step for Application Server
* Consistent environment because Maven handles everything
* Development is faster because IDE does not have to rebuild WAR for every run

Onward...

HelloTest.java
--------------

You write tests, right? :)

Testing is very straightforward in Minijax.

```java
package com.example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class HelloTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(Hello.class);
    }

    @Test
    public void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }
}
```

Key points:

```java
public class HelloTest extends MinijaxTest {
```

Extending `MinijaxTest` provides a handful of convenient setup steps.  Notably, it starts a test server and provides `register()` and `target()` methods.

Before running the test, we have to register our **Resource Class** `Hello`:

```java
@Before
public void setUp() {
    register(Hello.class);
}
```

And now we can test:

```java
@Test
public void testHello() {
    assertEquals("Hello world!", target("/").request().get(String.class));
}
```

Here we execute a simulated GET request to the "/" endpoint, and assert that the result equals "Hello world!".

There's a lot to unpack here, so let's take it one step at a time:

`target("/")` returns a `WebTarget`.  Think of this as a reference to the endpoint.

`request()` starts a new request.

`get(String.class)` executes the GET request, and expects a `String` result.

As you might have guessed, we could have called `post()`, `put()`, or `delete()` instead.  We will see examples of those in future articles.

Next
----

* [JSON Example](../minijax-example-json) - Learn how to read/write JSON from resource methods
* [Mustache Example](../minijax-example-mustache) - Learn how to render Mustache templates
* [Websocket Example](../minijax-example-websocket) - Learn how to enable websocket endpoints
