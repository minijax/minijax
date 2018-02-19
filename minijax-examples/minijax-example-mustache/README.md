
Hello Mustache
==============

A Minijax implementation of the Mustache example on the [mustache(5)](https://mustache.github.io/mustache.5.html) man page.

The source code is available at [minijax-examples/minijax-example-mustache](https://github.com/minijax/minijax/tree/master/minijax-examples/minijax-example-mustache)

Estimated reading time: 10 minutes

Lines of code:

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                             2             15              0             61
Maven                            1              0              0             31
Mustache                         1              0              0              5
-------------------------------------------------------------------------------
SUM:                             4             15              0             97
-------------------------------------------------------------------------------
```

pom.xml
-------

There is one new dependency in this example:

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-mustache</artifactId>
    <version>${project.version}</version>
</dependency>
```

The "minijax-mustache" dependency is quite small.  It does the following:

* Includes the excellent [Mustache.java](https://github.com/spullara/mustache.java) library
* Includes a `MessageBodyWriter` and `ExceptionMapper` for writing Mustache templates
* Includes a general-purpose `View` class for representing views
* Exports a `MustacheFeature` that we will use shortly

demo.mustache
-------------

New to this example is a resource file.  The `src/main/resources/templates` directory includes the `demo.mustache` file.  The contents are taken verbatim from the mustache(5) man page:

```mustache
Hello {{name}}
You have just won {{value}} dollars!
{{#in_ca}}
Well, {{taxed_value}} dollars, after taxes.
{{/in_ca}}
```

If you are new to Mustache templates, I recommend reading the [man page](https://mustache.github.io/mustache.5.html).  It is short, concise, and well written.

Resource files are quite banal, but there is one important aspect to highlight.  Note that the template is in `src/main/resources` and not `src/main/webapp`.

In traditional Java EE, you bundle everything into a WAR file.  In the [Maven standard directory layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html),
web application resources belong in `src/main/webapp`.

However, we are not building a WAR.  We are building a JAR.  Therefore, we do not want or need a `src/main/webapp` directory.  Instead, we put all of our resources in `src/main/resources`.

HelloMustache.java
------------------

HelloMustache.java includes all of the Java code for our application.

Key points:

```java
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class HelloMustache {
```

As in the previous examples, our resource class includes a `@Path` annotation.

In Hello World, we did not use a `@Produces` annotation.  In Hello JSON, we used `@Produces(MediaType.APPLICATION_JSON)`.  Here, because we are producing HTML, we use `@Produces(MediaType.TEXT_HTML)`.

Next, let's actually render a Mustache template:

```java
@GET
public static View hello() {
    final Map<String, Object> model = new HashMap<>();
    model.put("name", "Chris");
    model.put("value", 10000);
    model.put("taxed_value", 10000 - (10000 * 0.4));
    model.put("in_ca", true);

    return new View("demo", model);
}
```

This is a **Resource Method** that responds to `GET` requests.

We build a model using a standard `HashMap`.

We build a view which is a combination of the template name ("demo") and the model.  The template name maps to the Mustache file name ("demo.mustache").

> Language snobs will snicker at the verbosity of creating the model.  If that's you, here are a couple of other options:
>
> When we upgrade to Java 9, we will use the new `Map.of` syntax:
>
> ```java
> Map<String, Object> model = Map.of(
>         "name", "Chris",
>         "value", 10000,
>         "taxed_value", 10000 - (10000 * 0.4),
>         "in_ca", true);
> ```
>
> If you are a Kotlin user, you can use `mapOf`:
>
> ```kotlin
> val model = mapOf(
>         "name" to "Chris",
>         "value" to 10000,
>         "taxed_value" to 10000 - (10000 * 0.4),
>         "in_ca" to true)
> ```

And finally, we must register the Mustache feature using the aforementioned `MustacheFeature` class:

```java
new Minijax()
        .register(MustacheFeature.class)
        .register(HelloMustache.class)
        .start(8080);
```

Now you can run the example:

```bash
mvn exec:java -Dexec.mainClass="com.example.HelloMustache"
```

Now you can view the results:

> <http://localhost:8080>


HelloMustacheTest.java
----------------------

There is one test to demonstrate rendering the Mustache template:

```java
@Test
public void testMustacheTemplate() {
    final Response response = target("/").request().get();
    assertNotNull(response);
    assertEquals(200, response.getStatus());

    final View view = (View) response.getEntity();
    assertNotNull(view);
    assertEquals("demo", view.getTemplateName());
    assertEquals("Chris", view.getModel().get("name"));

    final String str = response.readEntity(String.class);
    assertNotNull(str);
    assertEquals(
            "Hello Chris\n" +
            "You have just won 10000 dollars!\n" +
            "Well, 6000.0 dollars, after taxes.\n",
            str);
}
```

Key points:

`response.getEntity()` returns the resource method return value.  In our example, that is the `View` instance.

`response.readEntity(String.class)` returns the serialized value.  In our example, that is the fully rendered output.

> If you are coming from Jersey, there are a couple differences between `MinijaxTest` and `JerseyTest` to be aware of.
>
> First, you will notice that we never included a test dependency.  The `minijax-core` module includes all of the test scaffolding.  `MinijaxTest` executes simulated requests completely in-memory, much like `jersey-test-framework-provider-inmemory`.
>
> Second, the behavior regarding "entities" is somewhat different.
>
> `getEntity()` returns the resource method return value directly.
>
> `readEntity()` can be called with one of three possible values:
>
> If called with the original entity class, then the entity is returned directly (identical to `getEntity()`).
>
> If called with `InputStream.class`, then the entity is serialized to a byte array using the standard JAX-RS serialization flow.
>
> If called with `String.class`, then the entity is serialized to a `String` using the the standard JAX-RS serialization flow.

Next
----

* [JSON Example](https://minijax.org/minijax-examples/minijax-example-json/index.html) - Learn how to read/write JSON from resource methods
* [Websocket Example](https://minijax.org/minijax-examples/minijax-example-websocket/index.html) - Learn how to enable websocket endpoints
