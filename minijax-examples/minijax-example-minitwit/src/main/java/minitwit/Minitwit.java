package minitwit;

import static org.apache.commons.codec.digest.DigestUtils.*;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.annotation.security.*;
import javax.inject.*;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.minijax.*;
import org.minijax.dao.*;
import org.minijax.mustache.*;
import org.minijax.security.*;
import org.minijax.view.*;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class Minitwit {

    @Entity(name = "User")
    public static class User extends SecurityUser {
        private static final long serialVersionUID = 1L;
        @OneToMany Set<User> following = new HashSet<>();

        public String gravatarUrl() throws IOException {
            return String.format(
                    "https://www.gravatar.com/avatar/%s?d=identicon&s=80",
                    md5Hex(getEmail().getBytes("CP1252")));
        }
    }

    @Entity(name = "Message")
    public static class Message extends DefaultBaseEntity {
        private static final long serialVersionUID = 1L;
        @ManyToOne User user;
        String text;
    }

    public static class Dao extends DefaultBaseDao implements SecurityDao {
    }

    @Inject
    private Security<User> security;

    @Inject
    private User currentUser;

    @Inject
    private Dao dao;

    private Response renderTimeline(final List<Message> messages) {
        final View view = new View("timeline");
        view.getModel().put("messages", messages);
        if (currentUser != null) {
            view.getModel().put("user", currentUser);
            view.getModel().put("csrf", security.getSessionToken());
        }
        return Response.ok(view, MediaType.TEXT_HTML).build();
    }

    @GET
    public Response timeline() {
        if (currentUser == null) {
            return Response.seeOther(URI.create("/public")).build();
        }
        final List<Message> messages = dao.getEntityManager()
                .createQuery("SELECT m FROM Message m WHERE m.user IN :following ORDER BY m.id DESC", Message.class)
                .setParameter("following", currentUser.following)
                .getResultList();
        return renderTimeline(messages);
    }

    @GET
    @Path("/public")
    public Response publicTimeline() {
        final List<Message> messages = dao.getEntityManager()
                .createQuery("SELECT m FROM Message m ORDER BY m.id DESC", Message.class)
                .getResultList();
        return renderTimeline(messages);
    }

    @GET
    @Path("/{handle}")
    public Response userTimeline(@PathParam("handle") final String handle) {
        final User user = dao.readByHandle(User.class, handle);
        final List<Message> messages = dao.getEntityManager()
                .createQuery("SELECT m FROM Message m WHERE m.user = :user ORDER BY m.id DESC", Message.class)
                .setParameter("user", user)
                .getResultList();
        return renderTimeline(messages);
    }

    @GET
    @Path("/{handle}/follow")
    @RolesAllowed("user")
    public Response followUser(@PathParam("handle") final String handle) {
        currentUser.following.add(dao.readByHandle(User.class, handle));
        dao.update(currentUser);
        return Response.seeOther(URI.create("/")).build();
    }

    @POST
    @Path("/addmessage")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed("user")
    public Response addMessage(@FormParam("text") final String text) {
        final Message msg = new Message();
        msg.text = text;
        msg.user = currentUser;
        dao.create(msg);
        return Response.seeOther(URI.create("/")).build();
    }

    @GET
    @Path("/login")
    public View login() {
        return new View("login");
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(
            @FormParam("email") final String email,
            @FormParam("password") final String password) {

        final LoginResult result = security.login(email, password);
        if (result.getStatus() == LoginResult.Status.SUCCESS) {
            return Response.seeOther(URI.create("/")).cookie(result.getCookie()).build();
        } else {
            final View view = new View("login");
            view.getModel().put("error", result.getStatus());
            return Response.ok(view, MediaType.TEXT_HTML).build();
        }
    }

    @GET
    @Path("/logout")
    public Response logout() {
        final NewCookie cookie = security.logout();
        return Response.seeOther(URI.create("/")).cookie(cookie).build();
    }

    @GET
    @Path("/register")
    public View register() {
        return new View("register");
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response register(
            @FormParam("handle") final String handle,
            @FormParam("email") final String email,
            @FormParam("password") final String password) {

        final User user = new User();
        user.setName(handle);
        user.setHandle(handle);
        user.setEmail(email);
        user.setRoles("user");
        user.setPassword(password);
        user.following.add(user);
        dao.create(user);
        final NewCookie cookie = security.loginAs(user);
        return Response.seeOther(URI.create("/")).cookie(cookie).build();
    }

    public static void main(final String[] args) {
        new Minijax()
                .staticDirectories("static")
                .register(PersistenceFeature.class)
                .register(MustacheFeature.class)
                .register(new SecurityFeature(User.class, Dao.class))
                .register(Minitwit.class)
                .start();
    }
}
