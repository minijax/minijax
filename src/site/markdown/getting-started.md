
Getting Started Tutorial
========================

In this tutorial we will do the following:

* Create a new Minijax "Hello World" project
* Run the application and test it with your web browser

**Step 1:** Create a new project.  We will use Maven archetype to create a new project called "minijax-tutorial.

```bash
$ mvn archetype:generate \
    -DarchetypeGroupId=org.minijax \
    -DarchetypeArtifactId=minijax-archetype-quickstart \
    -DarchetypeVersion=0.0.20
```

You will be prompted for a few values.  The default values are all ok, so just hit Enter a couple times.

```bash
Define value for property 'groupId': com.example
Define value for property 'artifactId': minijax-tutorial
Define value for property 'version' 1.0-SNAPSHOT: :
Define value for property 'package' com.example: :
Confirm properties configuration:
groupId: com.example
artifactId: minijax-tutorial
version: 1.0-SNAPSHOT
package: com.example

 Y: :
```

Congrats, you now have a project!

Let's explore:

```bash
$ tree minijax-tutorial
.
├── pom.xml
└── src
    ├── main
    │   └── java
    │       └── com
    │           └── example
    │               └── App.java
    └── test
        └── java
            └── com
                └── example
                    └── AppTest.java

9 directories, 3 files
```

Pretty simple and standard:

* A Maven pom.xml that declares dependencies
* A main App.java file with core logic
* A test AppTest.java file with unit tests

The main Java file is a minimal "Hello World" application:

```java
package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.minijax.Minijax;

public class App {

    @GET
    @Path("/")
    public static String hello() {
        return "Hello world!";
    }

    public static void main(final String[] args) {
        new Minijax().register(App.class).run(8080);
    }
}
```

The unit test uses the JAX-RS client API to test the endpoint:

```java
package com.example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class AppTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(App.class);
    }

    @Test
    public void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }
}
```

**Step 2:** We can run the application now:

```bash
$ mvn exec:java -Dexec.mainClass="com.example.App"
```

Open [http://localhost:8080/](http://localhost:8080/) and you should see "Hello world".

Congrats!

What's next?
------------

* [Sample Applications](minijax-examples/index.html) - Demonstrating Minijax capabilities
* [Supported Java EE standards](javaee.html) - Overview of which standards are supported
