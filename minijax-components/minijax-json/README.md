minijax-json
============

The minijax-json extension adds [Yasson](https://github.com/eclipse-ee4j/yasson) for JSON support.

Usage
-----

Step 1: Add the `minijax-json` Maven dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-json</artifactId>
    <version>${minijax.version}</version>
</dependency>
```

Step 2: Register the `JsonFeature.class` when you create the `Minijax` server:

```java
new Minijax()
        .register(JsonFeature.class)
        .register(HelloJson.class)
        .start();
```

That's it!  Now you can use JSON serialization features in your resources:

```java
public static class Widget {
    String id;
    String value;

    public Widget() {
    }

    public Widget(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}

@GET
@Produces(APPLICATION_JSON)
public static Collection<Widget> read() {
    return WIDGETS.values();
}
```

See [minijax-example-json](https://github.com/minijax/minijax/blob/master/minijax-examples/minijax-example-json) for a full example using `minijax-json`.
