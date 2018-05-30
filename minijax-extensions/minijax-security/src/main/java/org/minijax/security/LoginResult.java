package org.minijax.security;

import javax.ws.rs.core.NewCookie;

public class LoginResult {
    public static final LoginResult NOT_FOUND = new LoginResult(Status.NOT_FOUND, null);
    public static final LoginResult INVALID = new LoginResult(Status.INVALID, null);
    public static final LoginResult INCORRECT = new LoginResult(Status.INCORRECT, null);
    private final Status status;
    private final NewCookie cookie;

    public LoginResult(final NewCookie cookie) {
        this(Status.SUCCESS, cookie);
    }

    private LoginResult(final Status status, final NewCookie cookie) {
        this.status = status;
        this.cookie = cookie;
    }

    public Status getStatus() {
        return status;
    }

    public NewCookie getCookie() {
        return cookie;
    }

    public enum Status {
        SUCCESS,
        NOT_FOUND,
        INVALID,
        INCORRECT
    }
}
