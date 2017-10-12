
Getting Started Tutorial
========================

In this tutorial we will do the following:

* Create a new Minijax project
* Add a new REST endpoint
* Run the application and test it with your web browser

First we'll give the Minijax pitch.  Or you can [jump to the fun part](#overview).

<a id="overview"></a>

Overview
--------

Minijax is a lightweight subset of Java EE standards such as JAX-RS (REST), dependency injection, JSON, WebSockets, and more.

**Why Java?**  Java continues to be one of the most popular programming languages in existence.  It is hardened and battle tested.  It is fast.  It supports virtually all databases.  Most 3rd party services provide a Java SDK.

**Why not just use Java EE?**  Java EE application servers are big and unwieldy.  Deploying JBoss is not a trivial task.  There is a growing movement of using Java SE + a tasteful subset of Java EE API's.  You can then build a "fat jar" and run ```public static void main()```.

**Why Java EE API's?** Despite the weight and hassle of Java EE, the API's themselves can be quite elegant.  Plus, by using standard API's, you inherit the benefits of standardized documentation and engineers who are familiar with the design.  By sticking to standard API's, you can future proof your project.

**How lightweight?** We have been vigilant with dependencies.  Here is the current "Hello" example dependency tree:

```bash
$ mvn dependency:tree
[INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ minijax-example-hello ---
[INFO] org.minijax:minijax-example-hello:jar:0.0.8-SNAPSHOT
[INFO] +- junit:junit:jar:4.12:test
[INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO] \- org.minijax:minijax-core:jar:0.0.8-SNAPSHOT:compile
[INFO]    +- javax.annotation:jsr250-api:jar:1.0:compile
[INFO]    +- javax.inject:javax.inject:jar:1:compile
[INFO]    +- javax.ws.rs:javax.ws.rs-api:jar:2.1:compile
[INFO]    +- org.eclipse.jetty:jetty-server:jar:9.4.7.v20170914:compile
[INFO]    |  +- javax.servlet:javax.servlet-api:jar:3.1.0:compile
[INFO]    |  +- org.eclipse.jetty:jetty-http:jar:9.4.7.v20170914:compile
[INFO]    |  |  \- org.eclipse.jetty:jetty-util:jar:9.4.7.v20170914:compile
[INFO]    |  \- org.eclipse.jetty:jetty-io:jar:9.4.7.v20170914:compile
[INFO]    +- org.eclipse.jetty:jetty-servlet:jar:9.4.7.v20170914:compile
[INFO]    |  \- org.eclipse.jetty:jetty-security:jar:9.4.7.v20170914:compile
[INFO]    \- org.slf4j:slf4j-api:jar:1.7.25:compile
```

The hello "fat jar" clocks in at ~1.8 MB, with Jetty accounting for 818 out of 1121 classes (73%).  Minijax "core" itself only has 35 classes (3%).

<a id="archetype"></a>

Generating a Minijax Archetype
------------------------------

**Step 1:** Create a new project.  We will use Maven archetype to create a new project called "minijax-tutorial.

```bash
$ mvn archetype:generate \
    -DarchetypeGroupId=org.minijax \
    -DarchetypeArtifactId=minijax-archetype \
    -DarchetypeVersion=0.0.8-SNAPSHOT \
    -DgroupId=com.example \
    -DartifactId=minijax-tutorial
```

You will be prompted for a few values.  The default values are all ok, so just hit Enter a couple times.

```bash
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

We can run the application now:

```bash
$ mvn exec:java -Dexec.mainClass="com.example.App"
```

Open [http://localhost:8080/](http://localhost:8080/) and you should see "Hello world".

Nice.

