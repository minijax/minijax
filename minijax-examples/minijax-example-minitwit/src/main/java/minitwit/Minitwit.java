package minitwit;

import static org.apache.commons.codec.digest.DigestUtils.*;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.minijax.Minijax;
import org.minijax.db.*;
import org.minijax.mustache.*;
import org.minijax.security.*;

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

    public Response render(String templateName, Map<String, Object> properties) {
        View view = new View(templateName);
        if (currentUser != null) {
            view.getProps().put("user", currentUser);
            view.getProps().put("csrf", security.getSessionToken());
        }
        view.getProps().putAll(properties);
        return Response.ok(view, MediaType.TEXT_HTML).build();
    }

    @GET
    public Response timeline() {
        if (currentUser == null) {
            return Response.seeOther(URI.create("/public")).build();
        }
        List<Message> messages = dao.getEntityManager()
                .createQuery("SELECT m FROM Message m WHERE m.user IN :following ORDER BY m.id DESC", Message.class)
                .setParameter("following", currentUser.following)
                .getResultList();
        return render("timeline", Map.of("messages", messages));
    }

    @GET
    @Path("/public")
    public Response publicTimeline() {
        List<Message> messages = dao.getEntityManager()
                .createQuery("SELECT m FROM Message m ORDER BY m.id DESC", Message.class)
                .getResultList();
        return render("timeline", Map.of("messages", messages));
    }

    @GET
    @Path("/u/{handle}")
    public Response userTimeline(@PathParam("handle") String handle) {
        User user = dao.readByHandle(User.class, handle);
        List<Message> messages = dao.getEntityManager()
                .createQuery("SELECT m FROM Message m WHERE m.user = :user ORDER BY m.id DESC", Message.class)
                .setParameter("user", user)
                .getResultList();
        return render("timeline", Map.of("messages", messages));
    }

    @GET
    @Path("/u/{handle}/follow")
    @RolesAllowed("user")
    public Response followUser(@PathParam("handle") String handle) {
        currentUser.following.add(dao.readByHandle(User.class, handle));
        dao.update(currentUser);
        return Response.seeOther(URI.create("/")).build();
    }

    @POST
    @Path("/addmessage")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed("user")
    public Response addMessage(@FormParam("text") String text) {
        Message msg = new Message();
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
            @FormParam("handle") String handle,
            @FormParam("password") String password) {

        User user = dao.readByHandle(User.class, handle);
        NewCookie cookie = security.loginAs(user);
        return Response.seeOther(URI.create("/")).cookie(cookie).build();
    }

    @GET
    @Path("/logout")
    public Response logout() {
        NewCookie cookie = security.logout();
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
            @FormParam("handle") String handle,
            @FormParam("email") String email,
            @FormParam("password") String password) {

        User user = new User();
        user.setName(handle);
        user.setHandle(handle);
        user.setEmail(email);
        user.setRoles("user");
        user.following.add(user);
        dao.create(user);
        NewCookie cookie = security.loginAs(user);
        return Response.seeOther(URI.create("/")).cookie(cookie).build();
    }

    public static void main(String[] args) {
        new Minijax()
                .addStaticDirectory("static")
                .registerPersistence()
                .register(MinijaxMustacheFeature.class)
                .register(new SecurityFeature(User.class))
                .register(Dao.class, SecurityDao.class)
                .register(Minitwit.class)
                .run(8080);
    }
}
