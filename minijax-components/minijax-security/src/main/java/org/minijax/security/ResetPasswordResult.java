package org.minijax.security;

import jakarta.ws.rs.core.NewCookie;

public class ResetPasswordResult {
    public static final ResetPasswordResult NOT_FOUND = new ResetPasswordResult(Status.NOT_FOUND, null);
    public static final ResetPasswordResult EXPIRED = new ResetPasswordResult(Status.EXPIRED, null);
    public static final ResetPasswordResult MISMATCH = new ResetPasswordResult(Status.MISMATCH, null);
    public static final ResetPasswordResult TOO_SHORT = new ResetPasswordResult(Status.TOO_SHORT, null);
    private final Status status;
    private final NewCookie cookie;

    public ResetPasswordResult(final NewCookie cookie) {
        this(Status.SUCCESS, cookie);
    }

    private ResetPasswordResult(final Status status, final NewCookie cookie) {
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
        EXPIRED,
        MISMATCH,
        TOO_SHORT,
    }
}
