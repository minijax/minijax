package minitwit;

import static org.apache.commons.codec.digest.DigestUtils.*;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.minijax.Minijax;
import org.minijax.mustache.*;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class Minitwit {

    @Entity(name = "User")
    public static class User {
        @Id @GeneratedValue int id;
        String username;
        String email;
        String pwHash;
        @OneToMany Set<User> following = new HashSet<>();

        public String gravatarUrl() throws IOException {
            return String.format(
                    "https://www.gravatar.com/avatar/%s?d=identicon&s=80",
                    md5Hex(email.getBytes("CP1252")));
        }
    }

    @Entity(name = "Message")
    public static class Message {
        @Id @GeneratedValue int id;
        @ManyToOne User user;
        String text;
    }

    @Context
    private HttpServletRequest request;

    @PersistenceContext
    private EntityManager em;

    public Response render(String templateName, Map<String, Object> properties) {
        View view = new View(templateName);
        view.getProps().put("user", request.getSession().getAttribute("user"));
        view.getProps().putAll(properties);
        return Response.ok(view, MediaType.TEXT_HTML).build();
    }

    @GET
    public Response timeline() {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return Response.seeOther(URI.create("/public")).build();
        }
        List<Message> messages = em.createQuery("SELECT m FROM Message m WHERE m.user = :user OR m.user IN :following ORDER BY m.id DESC", Message.class)
                .setParameter("user", user)
                .setParameter("following", user.following)
                .getResultList();
        return render("timeline", Map.of("messages", messages));
    }

    @GET
    @Path("/public")
    public Response publicTimeline(@PathParam("username") String username) {
        List<Message> messages = em.createQuery("SELECT m FROM Message m ORDER BY m.id DESC", Message.class)
                .getResultList();
        return render("timeline", Map.of("messages", messages));
    }

    @GET
    @Path("/u/{username}")
    public Response userTimeline(@PathParam("username") String username) {
        List<Message> messages = em.createQuery("SELECT m FROM Message m WHERE m.user.username = :username ORDER BY m.id DESC", Message.class)
                .setParameter("username", username)
                .getResultList();
        return render("timeline", Map.of("messages", messages));
    }

    @GET
    @Path("/u/{username}/follow")
    public Response followUser(@PathParam("username") String username) {
        User follower = (User) request.getSession().getAttribute("user");
        User following = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class).setParameter("username", username).getSingleResult();
        follower.following.add(following);
        em.getTransaction().begin();
        em.merge(follower);
        em.getTransaction().commit();
        return Response.seeOther(URI.create("/")).build();
    }

    @POST
    @Path("/addmessage")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addMessage(@FormParam("text") String text) {
        Message msg = new Message();
        msg.text = text;
        msg.user = (User) request.getSession().getAttribute("user");
        em.getTransaction().begin();
        em.persist(msg);
        em.getTransaction().commit();
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
            @FormParam("username") String username,
            @FormParam("password") String password) {

        User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
        request.getSession().setAttribute("user", user);
        return Response.seeOther(URI.create("/")).build();
    }

    @GET
    @Path("/logout")
    public Response logout() {
        request.getSession().removeAttribute("user");
        return Response.seeOther(URI.create("/")).build();
    }

    @GET
    @Path("/register")
    public View register() {
        View view = new View("register");
        return view;
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response register(
            @FormParam("username") String username,
            @FormParam("email") String email,
            @FormParam("password") String password) {

        User user = new User();
        user.username = username;
        user.email = email;
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        request.getSession().setAttribute("user", user);
        return Response.seeOther(URI.create("/")).build();
    }

    public static void main(String[] args) {
        new Minijax()
                .registerPersistence()
                .register(MinijaxMustacheFeature.class)
                .addStaticDirectory("static")
                .register(Minitwit.class)
                .run(8080);
    }
}
