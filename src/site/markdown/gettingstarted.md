
Getting Started Tutorial
========================

In this tutorial we will do the following:

* Create a new Minijax "Hello World" project
* Run the application and test it with your web browser

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
