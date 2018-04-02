minijax-websocket
=================

The minijax-websocket extension configures Undertow for JSR 356 compliant websockets.

The websocket configuration includes support for CDI and JAX-RS injections.  For example, this allows your application to inject `@Context`, `@PathParam`, `@CookieParam`, etc in your websocket.

Usage
-----

Step 1: Add the `minijax-websocket` Maven dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-websocket</artifactId>
    <version>${minijax.version}</version>
</dependency>
```

That's it!  Now you can use JSR 356 `@ServerEndpoint` websockets.

```java
@ServerEndpoint("/echo")
public static class EchoEndpoint {

    @OnOpen
    public void onOpen(final Session session) throws IOException {
        LOG.info("[Session {}] Session has been opened.", session.getId());
        session.getBasicRemote().sendText("Connection Established");
    }

    @OnMessage
    public String onMessage(final String message, final Session session) {
        LOG.info("[Session {}] Sending message: {}", session.getId(), message);
        return message;
    }

    @OnClose
    public void onClose(final Session session) {
        LOG.info("[Session {}] Session has been closed.", session.getId());
    }

    @OnError
    public void onError(final Session session, final Throwable t) {
        LOG.info("[Session {}] An error has been detected: {}.", session.getId(), t.getMessage());
    }
}
```

See [minijax-example-websocket](https://github.com/minijax/minijax/blob/master/minijax-examples/minijax-example-websocket) for a full example using `minijax-websocket`.
