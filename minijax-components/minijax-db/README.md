minijax-db
==========

The minijax-db extension adds [Eclipselink](http://www.eclipse.org/eclipselink/) for JPA, ORM, connection pooling, and cluster coordination.

There are three tiers of functionality in this library:
1. Basic - Setup Eclipselink, read "persistence.xml", enable `@PersistenceContext` and `EntityManager`
2. Converters - JPA `AttributeConverter` classes for common types such as `java.util.UUID` and `java.time` classes
3. Entity framework - Provides `BaseDao` and `BaseEntity` classes for common database functionality

Basic Usage
-----------

Step 1: Add the `minijax-db` Maven dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-db</artifactId>
    <version>${minijax.version}</version>
</dependency>
```

Step 2: Register the `PersistenceFeature.class` when you create the `Minijax` server:

```java
new Minijax()
        .register(PersistenceFeature.class)
        .register(Hello.class)
        .start();
```

That's it!  Now you can use JPA features in your resources:

```java
@Entity
public class Widget {
    @Id
    int id;
    String value;
}

@Path("/widgets")
public class WidgetResource {
    @PersistenceContext
    private EntityManager em;

    @GET
    @Path("/{id}")
    public Widget readWidget(@PathParam("id") int id) {
        return em.find(Widget.class, id);
    }
}
```

Converters Usage
----------------

By default, entity attributes can be Java primitives such as `int` or `String`.  Often you will want to use other Java types embedded into a single column.

The minijax-db library provides several of these `AttributeConverter` utility classes:

| Converter               | Attribute Type                 | Column Type         | Strategy               |
| ----------------------- | ------------------------------ | ------------------- | ---------------------- |
| InstantConverter        | java.time.Instant              | java.sql.Timestamp  | Direct conversion      |
| JsonMapConverter        | java.util.Map<String, Object>  | java.lang.String    | JSON encoding          |
| LocalDateConverter      | java.time.LocalDate            | java.sql.Date       | Direct conversion      |
| StringListConverter     | java.util.List<String>         | java.lang.String    | Comma delimited        |
| UriConverter            | java.net.URI                   | java.lang.String    | String conversion      |
| UrlConverter            | java.net.URL                   | java.lang.String    | String conversion      |
| UrlEncodedMapConverter  | java.util.Map<String, String>  | java.lang.String    | URL encoded key value  |
| UuidConverter           | java.util.UUID                 | byte[]              | Direct conversion      |

To use one of the converters, use the `@Convert` annotation: 

```java

@Entity
public class Widget {
    @Id
    int id;

    String value;

    @Convert(converter = InstantConverter.class)
    Instant createdTime;   
}
```

Entity Framework Usage
----------------------

The final tier of the minijax-db library is the entity framework.  Entity classes have certain common requirements:
* Unified ID strategy
* Date/time fields for created, updated, and deleted
* CRUD operations (create, read, update, delete)

The following classes and interfaces can be used to that end:
* `BaseEntity` / `DefaultBaseEntity` - Defines a base entity class with UUID id, Instant created, updated, and deleted
* `NamedEntity` / `DefaultNamedEntity` - Defines an entity sub-class with String name, String handle, and image URL
* `BaseDao` / `DefaultBaseDao` - Defines a DAO class with standard CRUD operations create, read, update, and delete

Examples
--------

The following examples use minijax-db:
* [minijax-example-minitwit](https://github.com/minijax/minijax/blob/master/minijax-examples/minijax-example-minitwit)
* [minijax-example-petclinic](https://github.com/minijax/minijax/blob/master/minijax-examples/minijax-example-petclinic)

