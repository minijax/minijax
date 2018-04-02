minijax-cloudwatch
==================

The minijax-cloudwatch library provides a [SLF4J](https://www.slf4j.org/) `Appender` that logs to [AWS CloudWatch Logs](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/WhatIsCloudWatchLogs.html).

SLF4J is the de facto standard for Java logging.  Minijax uses SLF4J for all internal logging.

AWS CloudWatch Logs is a sub-feature of [AWS CloudWatch].  CloudWatch Logs provides a unified view for logs.

Unlike most Minijax extensions, minijax-cloudwatch does not have any dependencies on minijax-core.  It can be used completely standalone.

Usage
-----

Prerequisites: You need an AWS account and configured credentials.  Follow the instructions [here](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html).

Step 1: Add the `minijax-cloudwatch` Maven dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-cloudwatch</artifactId>
    <version>${minijax.version}</version>
</dependency>
```

Step 2: Add a CloudWatch appender to your `logback.xml` file.  You can use SLF4J variables in the various configuration settings.

```xml
<appender name="CLOUDWATCH" class="org.minijax.cloudwatch.CloudWatchAppender">
	<logGroupName>my-log-group</logGroupName>
	<logStreamName>my-log-stream-${HOSTNAME}</logStreamName>
	<layout>
		<pattern>[%thread] %-5level %logger{35} - %msg %n</pattern>
	</layout>
</appender>
<root level="INFO">
    <appender-ref ref="CLOUDWATCH" />
</root>
```

That's it!  Now you can use SLF4J as normal, and logs will be added to your CloudWatch account.

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}
```
