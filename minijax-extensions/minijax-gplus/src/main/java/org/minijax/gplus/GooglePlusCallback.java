package org.minijax.gplus;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.minijax.avatar.AvatarService;
import org.minijax.db.Avatar;
import org.minijax.security.Security;
import org.minijax.security.SecurityDao;
import org.minijax.security.SecurityUser;
import org.minijax.util.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

/**
 * Handles Google OAuth callback.
 *
 * Based on:
 * https://github.com/google/google-api-java-client-samples
 * plus-preview-appengine-sample/src/main/java/com/google/api/services/samples/plus/PlusSampleAuthCallbackServlet.java
 */
@Path("/googlecallback")
@RequestScoped
public class GooglePlusCallback {
    private static final Logger LOG = LoggerFactory.getLogger(GooglePlusCallback.class);
    private static final URI ERROR_URI = URI.create("/docs/google-error");

    @Inject
    private Security<SecurityUser> security;

    @Inject
    private SecurityDao dao;

    @Inject
    private AvatarService avatarService;

    @Inject
    private GooglePlusService gplusService;

    @GET
    public Response handleCallback(
            @QueryParam("code") final String code,
            @QueryParam("state") final String state)
                    throws IOException {

        if (code == null) {
            return Response.seeOther(ERROR_URI).build();
        }

        // Note that this implementation does not handle the user denying authorization.
        final GoogleAuthorizationCodeFlow authFlow = gplusService.initializeFlow();

        // Exchange authorization code for user credentials.
        final GoogleTokenResponse tokenResponse = authFlow.newTokenRequest(code)
                .setRedirectUri(gplusService.getRedirectUrl())
                .execute();

        final UUID tempId = IdUtils.create();
        final Credential credential = authFlow.createAndStoreCredential(tokenResponse, tempId.toString());
        final Plus plus = gplusService.getPlus(credential);
        final Person person = plus.people().get("me").execute();

        if (person == null || person.getEmails() == null || person.getEmails().isEmpty()) {
            return Response.seeOther(ERROR_URI).build();
        }

        SecurityUser user;
        NewCookie cookie = null;

        if (security.isLoggedIn()) {
            // User is already authenticated
            user = security.getUserPrincipal();

        } else {
            // User is not yet authenticated
            // Need to check if this is a valid user
            final String email = person.getEmails().get(0).getValue();
            user = dao.findUserByEmail(security.getUserClass(), email);
            if (user == null) {
                // This email address is not registered in the database
                // In the future, we may consider auto-creating new user accounts
                // For now, show an error message that we are in private beta.
                LOG.info("Attempted login from {}", email);
                return Response.seeOther(URI.create("/docs/beta")).build();
            }

            cookie = security.loginAs(user);
        }

        if (user.getAvatar() == null || user.getAvatar().getImageType() == Avatar.IMAGE_TYPE_DEFAULT) {
            tryGooglePlus(user, person);
        }

        ((GooglePlusUser) user).setGoogleCredentials(gplusService.extractUserCredential(tempId));
        dao.update(user);

        final String next = state != null ? state : "/";

        if (cookie != null) {
            return Response.seeOther(URI.create(next)).cookie(cookie).build();
        } else {
            return Response.seeOther(URI.create(next)).build();
        }
    }


    /**
     * Tries to fetch a remote Google Plus profile image.
     *
     * On success, downloads the image, creates thumbnails, uploads to S3, and updates the user object.
     *
     * @param user The user.
     * @param person The Google Plus person.
     * @return True on success; false otherwise.
     */
    private boolean tryGooglePlus(final SecurityUser user, final Person person) throws IOException {
        String url = person.getImage().getUrl();

        // By default, the Google+ profile URL has a "sz=50" query string param
        // Remove that param to get the full size picture
        url = url.replaceAll("\\?sz=\\d+", "");

        return avatarService.tryRemotePicture(user, url, Avatar.IMAGE_TYPE_GOOGLE);
    }
}
