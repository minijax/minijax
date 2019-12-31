minijax-mustache
================

The minijax-mustache extension adds [mustache.java](https://github.com/spullara/mustache.java) for Mustache template rendering.

Usage
-----

Step 1: Add the `minijax-mustache` Maven dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-mustache</artifactId>
    <version>${minijax.version}</version>
</dependency>
```

Step 2: Register the `MustacheFeature.class` when you create the `Minijax` server:

```java
new Minijax()
        .register(MustacheFeature.class)
        .register(HelloMustache.class)
        .start();
```

Step 3: Add mustache templates to the `templates` directory in your resources folder.  By default, that should be `src/main/resources`.

```mustache
Hello {{name}}
You have just won {{value}} dollars!
{{#in_ca}}
Well, {{taxed_value}} dollars, after taxes.
{{/in_ca}}
```

Step 4: Reference a template by name.  If the template name was `demo.mustache`, then create a `View` object with `"demo"`:

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

See [minijax-example-mustache](https://github.com/minijax/minijax/blob/master/minijax-examples/minijax-example-mustache) for a full example using `minijax-mustache`.
