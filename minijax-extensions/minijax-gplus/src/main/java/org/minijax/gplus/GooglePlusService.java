package org.minijax.gplus;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.minijax.security.Security;
import org.minijax.security.SecurityUser;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

/**
 * The GoogleService class provides helpers for connecting and interacting with Google services.
 */
@Provider
@RequestScoped
public class GooglePlusService {

    @Context
    private UriInfo uriInfo;

    @Inject
    private Security<SecurityUser> security;


    public String getLoginUrl() {
        try {
            return GooglePlusUtils.getLoginUrl(uriInfo.getRequestUri().toString());
        } catch (final IOException ex) {
            throw new WebApplicationException(ex);
        }
    }


    public String getConnectUrl() {
        try {
            return GooglePlusUtils.getConnectUrl(uriInfo.getRequestUri().toString());
        } catch (final IOException ex) {
            throw new WebApplicationException(ex);
        }
    }


    public Plus getPlus() {
        try {
            return GooglePlusUtils.getGooglePlus(GooglePlusUtils.getCredential((GooglePlusUser) security.getUserPrincipal()));
        } catch (final IOException ex) {
            throw new WebApplicationException(ex);
        }
    }


    public Person getProfile() throws IOException {
        final Plus plus = getPlus();
        return plus == null ? null : plus.people().get("me").execute();
    }
}
