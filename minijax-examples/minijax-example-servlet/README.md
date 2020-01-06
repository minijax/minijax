
Hello Servlet
=============

This example demonstrates how to use Minijax with a servlet container -- both Jetty and Tomcat.

The source code is available at [minijax-examples/minijax-example-servlet](https://github.com/minijax/minijax/tree/master/minijax-examples/minijax-example-servlet)

For documentation on how to use the Jetty Maven plugin:
https://www.eclipse.org/jetty/documentation/9.4.x/jetty-maven-plugin.html

For documentation on how to use the Tomcat Maven plugin:

To run the application using Jetty ([jetty-maven-plugin](https://www.eclipse.org/jetty/documentation/9.4.x/jetty-maven-plugin.html)):
```
mvn jetty:run
```

To run the application using Tomcat ([tomcat7-maven-plugin](http://tomcat.apache.org/maven-plugin-2.2/)):
```
mvn tomcat7:run-war
```

Then, with either Jetty or Tomcat, you can view the running app at:  http://localhost:8080

pom.xml
-------

To use the Jetty Maven Plugin:

```xml
<project>
    <!-- ... -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.eclipse.jetty</groupId>
                <artifactId>jetty-maven-plugin</artifactId>
                <version>9.4.20.v20190813</version>
            </plugin>
        </plugins>
    </build>
</project>
```

To use the Tomcat Maven Plugin:

```xml
<project>
    <!-- ... -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <version>2.2</version>
                <configuration>
                    <path>/</path>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

web.xml
-------

In normal embedded Minijax, a `web.xml` file is not required.  However, when using a servlet container, you will need a `web.xml` file.

The `web.xml` file goes in the `src/main/webapp/WEB-INF/` directory.

Example contents:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.4" xmlns="http://java.sun.com/xml/ns/j2ee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
	<servlet>
		<servlet-name>minijax-servlet</servlet-name>
		<servlet-class>org.minijax.servlet.MinijaxServlet</servlet-class>
		<init-param>
			<param-name>javax.ws.rs.Application</param-name>
			<param-value>com.example.HelloApplication</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>minijax-servlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
</web-app>
```

This instructs the servlet container to initialize Minijax.  For your own app, you will need to change `com.example.HelloApplication` to your own application class.

HelloApplication.java
=====================

Your app will need at least one instance of `javax.ws.rs.core.Application`.

For example, here is our `HelloApplication`:

```java
package com.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class HelloApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(
                HelloResource.class,
                PostResource.class,
                ShoutResource.class));
    }
}
```

See [the docs for `Application`](https://docs.oracle.com/javaee/7/api/javax/ws/rs/core/Application.html) for more information on how to use this class.

After that, you can continue to use Minijax normally.

Next
----

* [JSON Example](../minijax-example-json) - Learn how to read/write JSON from resource methods
* [Mustache Example](../minijax-example-mustache) - Learn how to render Mustache templates
* [Websocket Example](../minijax-example-websocket) - Learn how to enable websocket endpoints
