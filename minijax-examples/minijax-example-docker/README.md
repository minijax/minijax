
Hello Docker
============

This example is identical to the [Hello World](https://github.com/minijax/minijax/tree/master/minijax-examples/minijax-example-hello) example, except it also includes everything needed to build a [Docker](https://www.docker.com/) image.

The example includes:

* [Maven Shade plugin](http://maven.apache.org/plugins/maven-shade-plugin/) to build a "fat jar"
* Spotify's [Maven Dockerfile plugin](https://github.com/spotify/dockerfile-maven) to build the Docker image
* Dockerfile to define the Docker image

First, let's look at the Shade plugin.  It combines all dependencies into a single "fat jar".

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>${shade.version}</version>
    <configuration>
        <filters>
            <filter>
                <artifact>*:*</artifact>
                <excludes>
                    <exclude>META-INF/*.SF</exclude>
                    <exclude>META-INF/*.DSA</exclude>
                    <exclude>META-INF/*.RSA</exclude>
                </excludes>
            </filter>
        </filters>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>shade</goal>
            </goals>
            <phase>package</phase>
            <configuration>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>com.example.HelloDocker</mainClass>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Next, we use the Spotify Dockerfile plugin:

```xml
<plugin>
    <groupId>com.spotify</groupId>
    <artifactId>dockerfile-maven-plugin</artifactId>
    <version>${dockerfile.version}</version>
    <configuration>
        <repository>minijax/minijax-example-docker</repository>
        <tag>${project.version}</tag>
        <buildArgs>
            <JAR_FILE>${project.build.finalName}.jar</JAR_FILE>
        </buildArgs>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>javax.activation</groupId>
            <artifactId>javax.activation-api</artifactId>
            <version>1.2.0</version>
        </dependency>
        <dependency>
            <groupId>org.codehaus.plexus</groupId>
            <artifactId>plexus-archiver</artifactId>
            <version>3.4</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <id>default</id>
            <goals>
                <goal>build</goal>
                <goal>push</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

And finally, our Dockerfile:

```
FROM openjdk:9-jre-slim
ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/minijax/minijax-example-docker.jar"]
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/minijax/minijax-example-docker.jar
```

We can now build the Docker image:

```bash
mvn clean install
```

Maven invokes Docker to build and register the image.  You can verify that the image is now available:

```bash
$ docker images
REPOSITORY                       TAG                 IMAGE ID            CREATED             SIZE
minijax/minijax-example-docker   0.2.5               e2ef6e0a1248        2 minutes ago       291MB
```

We can now run the Docker image:

```bash
docker run -p 0.0.0.0:8080:8080 -it minijax/minijax-example-docker:0.2.5
```
