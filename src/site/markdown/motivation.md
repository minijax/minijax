
Motivation
==========

Minijax is a lightweight subset of Java EE standards such as JAX-RS (REST), dependency injection, JSON, WebSockets, and more.

Why does Minijax exist?

**Why Java?**  Java continues to be one of the most popular programming languages in existence.  It is hardened and battle tested.  It is fast.  It supports virtually all databases.  Most 3rd party services provide a Java SDK.

**Why not just use Java EE?**  Java EE application servers are big and unwieldy.  Deploying JBoss is not a trivial task.  There is a growing movement of using Java SE + a tasteful subset of Java EE API's.  You can then build a "fat jar" and run ```public static void main()```.

**Why Java EE API's?** Despite the weight and hassle of Java EE, the API's themselves can be quite elegant.  Plus, by using standard API's, you inherit the benefits of standardized documentation and engineers who are familiar with the design.  By sticking to standard API's, you can future proof your project.

**How lightweight?** We have been vigilant with dependencies.  Check out the ["Hello World" dependencies](https://minijax.org/minijax-examples/minijax-example-hello/dependencies.html)

What's next?
------------

* [Getting Started Tutorial](getting-started.html) - 5 minute tutorial
* [Sample Applications](minijax-examples/index.html) - Demonstrating Minijax capabilities
* [Supported Java EE standards](javaee.html) - Overview of which standards are supported
