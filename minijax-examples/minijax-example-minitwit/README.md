
MiniTwit
========

This is a port of [mitsuhiko](https://github.com/mitsuhiko)'s [MiniTwit](https://github.com/pallets/flask/tree/master/examples/minitwit) application.

MiniTwit is a mini Twitter clone using Minijax.

This example demonstrates:

* Using minijax-db database features
* Using minijax-security for login and logout
* Using minijax-mustache for HTML template rendering
* Slightly reckless coding for a short example

The source code is available at [minijax-examples/minijax-example-minitwit](https://github.com/minijax/minijax/tree/master/minijax-examples/minijax-example-minitwit)

Estimated reading time: 20 minutes

The core "Minitwit.java" is less than 200 lines of Java (comparable to the original Flask/Python example).  The total project lines of code:

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                             2             64              0            335
Mustache                         5              0              0            107
XML                              2              0              0             31
CSS                              1              4              0             19
-------------------------------------------------------------------------------
SUM:                            10             68              0            492
-------------------------------------------------------------------------------
```

pom.xml
-------

We pull in the following dependencies:
* minijax-undertow - Base Minijax
* minijax-db - Database, ORM, and entity framework
* minijax-security - User account, login, logout
* minijax-mustache - Mustache templates for HTML rendering
* h2 - Embedded database
* commons-codec - Apache commons utilities (for Gravatar)
* logback - Logging
* junit - Testing

persistence.xml
---------------

The "persistence.xml" file is the JPA configuration file.

```xml
<persistence xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd" version="2.0">
    <persistence-unit name="minitwit" transaction-type="RESOURCE_LOCAL">
        <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
        <class>minitwit.Minitwit$User</class>
        <class>minitwit.Minitwit$Follower</class>
        <class>minitwit.Minitwit$Message</class>
        <class>org.minijax.db.InstantConverter</class>
        <class>org.minijax.db.UuidConverter</class>
        <class>org.minijax.security.UserSession</class>
        <class>org.minijax.security.SecurityUser</class>
        <properties>
          <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
          <property name="javax.persistence.jdbc.url" value="jdbc:h2:~/.minijax/minitwit"/>
          <property name="javax.persistence.schema-generation.database.action" value="create" />
        </properties>
    </persistence-unit>
</persistence>
```

For a full description of "persistence.xml", check out one of these tutorials:
* <https://www.eclipse.org/webtools/dali/docs/3.2/user_guide/task_manage_persistence.htm>
* <https://www.objectdb.com/java/jpa/entity/persistence-unit>
* <https://examples.javacodegeeks.com/enterprise-java/jpa/java-persistence-xml-example/>

In our persistence.xml, we have the following highlights:
* `<class>` elements for our Minitwit classes and also the standard classes from minijax-db and minijax-security
* `<property>` elements to setup the H2 database and automatically generate the database schema

Minitwit.java
-------------

Minitwit.java includes all of the Java code for our application.

Some highlights...

Here is our entire `User` class.

```java
@Entity(name = "User")
public static class User extends SecurityUser {
    @OneToMany Set<User> following = new HashSet<>();

    public String gravatarUrl() throws IOException {
        return String.format(
                "https://www.gravatar.com/avatar/%s?d=identicon&s=80",
                md5Hex(getEmail().getBytes("CP1252")));
    }
}
```

Extending minijax-security's `SecurityUser` provides a ton of utility including:
* ID, created date, updated date, deleted date
* Name, handle, email
* Bcrypt-encrypted password hash
* Login, logout

We had to add 2 new pieces of functionality:
* "Following" - a set of users that this user is following
* "Gravatar URL" - a utility method to get the Gravatar image for the email address 

Here is our `Message` class, representing a tweet:

```java
@Entity(name = "Message")
public static class Message extends DefaultBaseEntity {
    @ManyToOne User user;
    String text;
}
```

Extending minijax-db's `DefaultBaseEntity` provides ID and CRUD functionality.  We just needed to add the `User` for the author and the `String` for the message contents.

Let's now look at the logic for the default page.  If the user is not logged in, they are redirected to "/public".  Otherwise, they are shown a collection of messages for all the users they are following.

```java
@GET
public Response timeline() {
    if (currentUser == null) {
        return Response.seeOther(URI.create("/public")).build();
    }
    List<Message> messages = dao.getEntityManager()
            .createQuery("SELECT m FROM Message m WHERE m.user IN :following ORDER BY m.id DESC", Message.class)
            .setParameter("following", currentUser.following)
            .getResultList();
    return renderTimeline(messages);
}
```

How cool is that?  JPA has built in support for an "IN" query using a `Set` of entities.  Not only is this query short and self-explanatory, it is also very efficient.  Eclipselink (the default JPA provider) can correctly convert this to a single SQL query.

To actually render a timeline of messages, we use the helper method `renderTimeline()`:

```java
private Response renderTimeline(final List<Message> messages) {
    View view = new View("timeline");
    view.getModel().put("messages", messages);
    if (currentUser != null) {
        view.getModel().put("user", currentUser);
        view.getModel().put("csrf", security.getSessionToken());
    }
    return Response.ok(view, TEXT_HTML).build();
}
```

Let's look at login and logout:

```java
@GET
@Path("/login")
public View login() {
    return new View("login");
}

@POST
@Path("/login")
@Consumes(APPLICATION_FORM_URLENCODED)
public Response login(
        @FormParam("email") String email,
        @FormParam("password") String password) {

    try {
        NewCookie cookie = security.login(email, password);
        return Response.seeOther(URI.create("/")).cookie(cookie).build();
    } catch (final BadRequestException ex) {
        View view = new View("login");
        view.getModel().put("error", ex.getMessage());
        return Response.ok(view, TEXT_HTML).build();
    }
}

@GET
@Path("/logout")
public Response logout() {
    NewCookie cookie = security.logout();
    return Response.seeOther(URI.create("/")).cookie(cookie).build();
}
```

When the user "GET"s the "/login" resource, we simply return the Mustache template for the login page.  Creating a `View` looks for the template of corresponding name.  So, `new View("login")` looks for "login.mustache" in "src/main/resources/templates/".

When the user "POST"s to the "/login" resource, we handle the form.  JAX-RS provides the `@FormParam` functionality to easily parse the form.

The minijax-security `Security` class does the heavy lifting for login.  It handles non-existing users, disabled/deleted users, incorrect passwords, etc.  On success, it returns a `NewCookie` with an opaque session ID token.  On error, it throws a `BadRequestException` with a user friendly error message.

When the user access the "/logout" resource, we always return a cookie that overwrites any existing session ID token.
