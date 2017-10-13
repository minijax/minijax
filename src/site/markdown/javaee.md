
Supported Standards
-------------------

Minijax supports a lightweight subset of Java EE standards.  This page reviews exactly which standards are implemented.

## [JSR 370](https://www.jcp.org/en/jsr/detail?id=370) - JAX-RS

Annotations:

* ```@Provider```
* ```@Path```
* ```@GET```, ```@POST```, ```@PUT```, ```@DELETE```, ```@OPTIONS```, ```@HEAD```
* ```@CookieParam```, ```@FormParam```, ```@HeaderParam```, ```@PathParam```
* ```@Consumes```, ```@Produces```

## [JSR 330](https://www.jcp.org/en/jsr/detail?id=330) - Dependency Injection

Annotations:

* ```@Inject```
* ```@Singleton```

## [JSR 356](https://www.jcp.org/en/jsr/detail?id=356) - WebSockets

Annotations:

* ```@ServerEndpoint```
* ```@OnOpen```, ```@OnMessage```, ```@OnClose```, ```@OnError```

## [JSR 250](https://www.jcp.org/en/jsr/detail?id=250) - Security

Annotations:

* ```@PermitAll```, ```@DenyAll```, ```@RolesAllowed```

## [JSR 367](https://www.jcp.org/en/jsr/detail?id=367) - JSON-B

TODO
