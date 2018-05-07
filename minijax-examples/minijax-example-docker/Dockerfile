FROM openjdk:9-jre-slim
ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/minijax/minijax-example-docker.jar"]
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/minijax/minijax-example-docker.jar
