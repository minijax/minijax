
Motivation
==========

Minijax is a lightweight subset of Java EE standards such as JAX-RS (REST), dependency injection, JSON, WebSockets, and more.

Why does Minijax exist?

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

**Why Jetty?**

**Why not Undertow (or Netty or Grizzly or ...)?**

**[Why not Spring?](why-not-spring.html)**

**Why not Dropwizard?**

What's next?
------------

* [Getting Started Tutorial](getting-started.html) - 5 minute tutorial
* [Sample Applications](minijax-examples/index.html) - Demonstrating Minijax capabilities
* [Supported Java EE standards](javaee.html) - Overview of which standards are supported
