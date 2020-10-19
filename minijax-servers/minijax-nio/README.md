minijax-nio
===========

The minijax-nio project is an experimental HTTP server for maximum performance.

WARNING: This is an experimental project!

Usage
-----

Step 1: Add the `minijax-nio` Maven dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-nio</artifactId>
    <version>${minijax.version}</version>
</dependency>
```

That's it!  Now you can use the `minijax-nio` server:

```java
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
