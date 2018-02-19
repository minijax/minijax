
Hello WebSockets
================

This example demonstrates:

* How to setup and use websockets
* How to serve static assets

The source code is available at [minijax-examples/minijax-example-websocket](https://github.com/minijax/minijax/tree/master/minijax-examples/minijax-example-websocket)

Estimated reading time: 10 minutes

Lines of code:

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                             2             23              0             77
JavaScript                       1             11              0             45
Maven                            1              0              0             42
HTML                             1              0              0             20
CSS                              1              4              0             19
XML                              1              0              0             13
-------------------------------------------------------------------------------
SUM:                             7             38              0            216
-------------------------------------------------------------------------------
```

pom.xml
-------

There is one new dependency in this example:

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-websocket</artifactId>
    <version>${project.version}</version>
</dependency>
```

The "minijax-websocket" dependency adds and configures Undertow's WebSocket features:

* Full support for [JSR 356](http://www.oracle.com/technetwork/articles/java/jsr356-1937161.html) WebSockets
* JAX-RS `@Path` routes are completely compatible with JSR 356 `@ServerEndpoint` annotations
* WebSocket creation is compatible with Minijax dependency injection

HelloWebSocket.java
-------------------

HelloMustache.java includes all of the Java code for our application.

Let's jump straight to the WebSocket:

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

We created a new POJO called `EchoEndpoint`.  We annotated the class with `@ServerEndpoint("/echo")`.

> In previous examples, we used `@Path` for endpoints.  What's the deal?  `@Path` is for normal HTTP requests such as GET and POST.  `@ServerEndpoint` is for WebSockets.

Each new connection will create a new instance of the `EchoEndpoint` class.  Therefore, we can store session-specific or user-specific information in class members.

There are four methods with self-explanatory names.  As discussed in the [JSR 356](http://www.oracle.com/technetwork/articles/java/jsr356-1937161.html) documentation, you can name the methods whatever you want.  The important part is the method annotations:

1. `@OnOpen` - called when a connection has been established
2. `@OnMessage` - called when the endpoint receives a message
3. `@OnClose` - called when the connection is closing
4. `@OnError` - called when an error is encountered

In our `EchoEndpoint`, we really only do two things:

1. Send "Connection Established" when a connection is established
2. Echo any message received back to the client

Next let's look at the `main()` function to see how to configure the WebSocket:

```java
public static void main(final String[] args) {
    new Minijax()
            .register(EchoEndpoint.class)
            .staticFile("static/index.html", "/")
            .staticDirectories("static")
            .start(8080);
}
```

Quite similar to previous examples, we create a new `Minijax` instance and register the resource.

New to this example is `staticFile` and `staticDirectories`.

`staticFile()` adds a static file.  The file can optionally be mounted at a specific path, which is what we did in this example.  The static file is a resource file located at `src/main/resources/static/index.html`.  We reference the file by its resource name, which is `static/index.html`.  We want that file to be the default file, so we mount it at the root path "/".

`staticDirectories()` adds directories of static files.  The `static` directory includes a CSS file and a JavaScript file.  We could have added them separately using multiple calls to `addStaticFile()`, but it's more convenient to simply mount the entire directory.

Speaking of static assets, let's take a look...

index.html
----------

The `index.html` file contains all of the HTML content for the application:

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.1, maximum-scale=10.0"/>
    <title>WebSocket Test</title>
    <link rel="stylesheet" href="/static/ws.css">
    <script src="/static/ws.js"></script>
  </head>
  <body>
    <div class="container">
      <div class="output"></div>
      <div class="input">
        <input type="text" name="input">
        <button>Submit</button>
      </div>
    </div>
  </body>
</html>
```

This is very standard HTML content.  The only mildly interesting aspect is that we have included the CSS and JS using the "/static/ws.css" and "/static/ws.js" files as previously discussed.

ws.js
-----

The `ws.js` file contains all of the JavaScript content for the application:

```javascript
var output;
var input;
var button;
var ws;

function init() {
    output = document.querySelector('.output');
    input = document.querySelector('input');
    button = document.querySelector('button');
    
    input.onkeydown = function(e) {
        if (event.keyCode === 13) {
            submit();
        }
    }   
    
    button.onclick = function(e) {
        submit();
    };

    ws = new WebSocket('ws://localhost:8080/echo');

    ws.onopen = function(e) {
        log('WebSocket opened');
    };
    
    ws.onclose = function(e) {
        log('WebSocket closed');
    };
    
    ws.onmessage = function(e) {
        log('<span style="color:blue">RESPONSE:</span> ' + e.data);
    };

    ws.onerror = function(e) {
        log('<span style="color:red">ERROR:</span> ' + e.data);
    };
}
```

After setting up the DOM elements and DOM event handlers, we create the WebSocket.  You will probably notice that the structure is remarkably similar to the Java WebSocket.  Both implementations have the same four methods: `onOpen`, `onMessage`, `onClose`, `onError`.

When the user presses 'Enter' (keyCode === 13) or clicks on the submit button, we call the `submit()` function:

```javascript
function submit() {
    var message = input.value;
    log('<span style="color:green">SENT:</span> ' + message);
    ws.send(message);
    input.value = '';
    input.focus();
}
```

The `submit()` function logs the content, sends it to the WebSocket, and resets the input form.

Ok, enough review, let's run the application:

```bash
mvn exec:java -Dexec.mainClass="com.example.HelloMustache"
```

And open the application in your web browser:

> <http://localhost:8080>

Submit a few messages.  For each message you should see the following:

1. "SENT: " in the web browser output box, representing that the message was sent
2. "Sending message: " in the Java console, representing that the message was received and echoed
3. "RESPONSE: " in the web browser output, representing that the echo was received

If you use Google Chrome, you can see the WebSocket network traffic live.  Open "Developer Tools", then go to the "Network" tab, then click on the "echo" request, then go to the "Frames" tab:

[![Screenshot](https://minijax.org/minijax-examples/minijax-example-websocket/screenshot.png)](https://minijax.org/minijax-examples/minijax-example-websocket/screenshot.png)

Next
----

* [Minitwit](https://minijax.org/minijax-examples/minijax-example-minitwit/index.html) - Build a miniature Twitter clone
* [Pet Clinic](https://minijax.org/minijax-examples/minijax-example-petclinic/index.html) - Build a more full-featured Pet Clinic management application
