
Hello JSON
===========

This example builds on [Hello World](http://minijax.org/minijax-examples/minijax-example-hello/index.html) and demonstrates:

* Multiple HTTP verbs such as GET and POST
* Compound paths
* Using path parameters
* Reading and writing JSON content

The source code is available at <https://github.com/minijax/minijax/tree/master/minijax-examples/minijax-example-json>

Estimated reading time: 15 minutes

Lines of code:

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                             2             36              0            107
Maven                            1              0              0             31
-------------------------------------------------------------------------------
SUM:                             3             36              0            138
-------------------------------------------------------------------------------
```

pom.xml
-------

As with the Hello World example, let's start with the Maven pom.xml.

In addition to "minijax-core", we add one new dependency:

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-json</artifactId>
    <version>${project.version}</version>
</dependency>
```

The "minijax-json" dependency includes everything we need for reading and writing JSON content.  Behind the scenes, it includes and configures [Jackson](https://github.com/FasterXML/jackson).

HelloJson.java
--------------

HelloJson.java includes all of the Java code for our application.

Key points:

```java
@Path("/widgets")
@Produces(MediaType.APPLICATION_JSON)
public class HelloJson {
```

We define a **Resource Class** called `HelloJson` at the path "/widgets".

The `@Produces` annotation declares that, by default, resource methods produce JSON content.

Next, let's declare an entity type:

```java
@XmlRootElement
public static class Widget {
    public String id;
    public String value;

    public Widget() {
    }

    public Widget(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
```

The `Widget` class is a simple POJO ("plain old Java object").  It has two properties: `id` and `value`.  Getters and setters were omitted for demonstration purposes, but they would normally be a good idea.

We also include a new annotation: `@XmlRootElement`.  This is a [JAXB](http://www.oracle.com/technetwork/articles/javase/index-140168.html) annotation, which defines rules for serializing the Java class to XML or JSON.

While the name includes "XML", JAXB is the *de facto* standard for specifying JSON conversion rules.  It is supported by major JSON libraries such as Jackson and [MOXy](http://www.eclipse.org/eclipselink/#moxy).

The `@XmlRootElement` annotation declares that the `Widget` class can be used as a root type when serializing and deserializing.

Next let's declare a data store for widgets:

```java
static final Map<String, Widget> WIDGETS = new HashMap<>();
```

As you probably discerned, this is a grossly oversimplified implementation.  In most real world applications, you would use a database or some persistent storage.  For this example, a hashtable will suffice.

Next let's declare some resource methods:

```java
@GET
public static Collection<Widget> read() {
    return WIDGETS.values();
}

@GET
@Path("/{id}")
public static Widget read(@PathParam("id") final String id) {
    return WIDGETS.get(id);
}
```

The first method is a simple "get everything" method.  Note that we're returning a `java.util.Collection`.  Jackson has built-in support for Java collection types such as `Collection`, `List`, and `Map`.

The second method demonstrates a few new features.

* There is a `@Path` annotation on the method, similar to the `@Path` annotation on the class.  These annotations are combined so the effective path is "/widgets/{id}".
* The path includes a curly brace syntax "{id}" which specifies a path parameter.
* The method takes an argument annotated with `@PathParam` which connects to the path variable by name "id"

Ok, we have now demonstrated how to "GET" content.  How do we create content?  One more resource method:

```java
@POST
@Consumes(MediaType.APPLICATION_JSON)
public static Response create(final Widget widget) {
    WIDGETS.put(widget.id, widget);
    return Response.created(URI.create("/widgets/" + widget.id)).build();
}
```

Several new features:

* The `@POST` annotation declares that this method should be called on HTTP POST requests
* The `@Consumes` annotation declares the HTTP Content-Type that the method consumes
* The single method argument connects to the `@Consumes` annotation
* The `Response` class contains rich functionality for all kinds of HTTP response capabilities

One last thing before testing:  We need to `register()` the JSON feature:

```java
new Minijax()
        .register(JsonFeature.class)
        .register(HelloJson.class)
        .run(8080);
```

In addition to the setup from "Hello World", we now include `register(JsonFeature.class)`.  That call does the following:

* Initializes Jackson
* Adds JSON-enabled `MessageBodyReader` and `MessageBodyWriter`
* Adds a JSON-aware `ExceptionMapper` for REST endpoints

At this point, it's time to run the application and start testing it out.

Let's run the application using Maven:

```bash
mvn exec:java -Dexec.mainClass="com.example.HelloJson"
```

You can view the results in your web browser:

> <http://localhost:8080/widgets>

You should see an empty collection:  "[]"

For working with JSON and REST endpoints, the `curl` command line tool tends to be more useful, so let's use that.

`curl` for the empty collection:

```bash
$ curl http://localhost:8080/widgets
[]
```

Create a new widget:

```bash
$ curl -d '{"id":"1","value":"Hello"}' http://localhost:8080/widgets
```

And then confirm that the widget was actually created:

```bash
$ curl http://localhost:8080/widgets
[{"id":"1","value":"Hello"}]
```

Let's take a step back.  When we created the widget, there was not any output.  Let's create another widget and look at the HTTP headers with the "-i" option:

```bash
$ curl -i -d '{"id":"2","value":"World"}' http://localhost:8080/widgets
HTTP/1.1 201 Created
Date: Sat, 18 Nov 2017 00:30:40 GMT
Location: /widgets/2
Content-Length: 0
Server: Jetty(9.4.7.v20170914)
```

Notice the status is "201 Created", meaning that the widget was successfully created.  It also includes a "Location" header with the URI of the new entity.  You may recall that we returned these values with `Response.created()`.

Let's get the new entity directly:

```bash
$ curl http://localhost:8080/widgets/2
{"id":"2","value":"World"}
```

There is our new entity.  Notice that this is a single entity (curly braces, not square braces).

And finally, let's get the full collection again:

```bash
$ curl http://localhost:8080/widgets
[{"id":"1","value":"Hello"},{"id":"2","value":"World"}]
```

Sure enough, there are the two entities.

HelloJsonTest.java
------------------

The unit tests look quite similar to the Hello World unit tests.  There are two new features that are worth noting.

First, let's look at how to "get" a complex type:

```java
final Collection<Widget> widgets = target("/widgets")
        .request()
        .get(new GenericType<Collection<Widget>>() {});

assertNotNull(widgets);
assertEquals(1, widgets.size());
```

What is this crazy `GenericType`?

In a normal simple case, if you want to read a non-generic class, you can use the simple syntax:

```java
get(Widget.class)
```

If you want to read a generic complex type, you might try (and fail) to use this invalid syntax:

```java
get(Collection<Widget>.class)
```

Unfortunately, due to limitations of the Java language, you cannot do that.  

Instead, we use the clever syntax of `GenericType<T>`.  Now the `get()` method returns a `Collection<Widget>` as expected.

Second, let's look at how to "post" content:

```java
final String json = "{\"id\":\"2\",\"value\":\"World\"}";

final Response response = target("/widgets")
        .request()
        .post(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));

assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
assertEquals("/widgets/2", response.getLocation().toString());
```

Declaring the JSON content in a string is pretty straightforward.

Posting the content requires wrapping the JSON in a `Entity` using `Entity.entity(Object, MediaType)`.

This has poor discoverability, but it is worth committing to memory.  As you write tests, you find yourself using the `Entity` creators quite frequently.

Next
----

* [Todo Backend](https://minijax.org/minijax-examples/minijax-example-todo-backend/index.html) - See a more elaborate JSON example
* [Websocket Example](https://minijax.org/minijax-examples/minijax-example-websocket/index.html) - Learn how to enable websocket endpoints
