
SNS Example
===========

This example demonstrates connecting a Minijax/JAX-RS endpoint with [AWS SNS]() to handle requests for subscriptions and notifications.

The example uses the AWS SNS Java SDK:

```xml
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-java-sdk-sns</artifactId>
    <version>${aws.version}</version>
</dependency>
```

The `@Post` resource method passes the `InputStream` to an AWS `SnsMessageManager`:

```java
@Inject
private SnsMessageManager sns;

@POST
public Response handle(final InputStream inputStream) {
    final SnsMessage message = sns.parseMessage(inputStream);
```

Calling `SnsMessageManager.parseMessage` does the following:

* Parses the content body as JSON
* Extracts the message signature
* Extracts the message signature certificate URL
* Downloads the certificate URL
* Verifies the message signature

Once we have a `SnsMessage`, we dispatch to the various method handlers:

```java
if (message instanceof SnsSubscriptionConfirmation) {
    return handleSubscribe((SnsSubscriptionConfirmation) message);

} else if (message instanceof SnsUnsubscribeConfirmation) {
    return handleUnsubscribe((SnsUnsubscribeConfirmation) message);

} else if (message instanceof SnsNotification) {
    return handleNotification((SnsNotification) message);

} else {
    throw new BadRequestException("Unrecognized SNS message");
}
```

Testing
-------

This example also demonstrates high test coverage using Mockito.

There is one interesting challenge when mocking the AWS SNS client library.  Several of the classes are `final`, which means that Mockito cannot mock them by default.

However, Mockito is incubating a new engine that *does* support mocking `final` classes.  We enable the new engine using the `org.mockito.plugins.MockMaker` file.  Learn more:

* [What's New in Mockito 2](https://github.com/mockito/mockito/wiki/What's-new-in-Mockito-2#mock-the-unmockable-opt-in-mocking-of-final-classesmethods)
* [Stack Overflow example](https://stackoverflow.com/a/40018295/2051724)

With the new Mockito engine, we can write tests that mock the AWS SNS client library:

```java
@Test
public void testPostNotification() {
    final SnsNotification message = mock(SnsNotification.class);
    when(message.getTopicArn()).thenReturn("Test Topic");
    when(message.getMessageId()).thenReturn("Test ID");
    when(message.getTimestamp()).thenReturn(new Date());
    when(message.getSubject()).thenReturn("Test Subject");
    when(message.getMessage()).thenReturn("Hello World");

    final SnsMessageManager sns = mock(SnsMessageManager.class);
    when(sns.parseMessage(any())).thenReturn(message);

    resetServer();
    register(sns, SnsMessageManager.class);
    register(SnsExample.class);

    assertEquals(200, target("/snsexample").request().post(Entity.text("")).getStatus());
}
```
