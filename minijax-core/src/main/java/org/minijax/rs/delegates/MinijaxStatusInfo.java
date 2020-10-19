package org.minijax.rs.delegates;

import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.Response.Status.Family;
import jakarta.ws.rs.core.Response.StatusType;

public class MinijaxStatusInfo implements StatusType {
    private int statusCode;
    private String reasonPhrase;

    public MinijaxStatusInfo() {
        this(Status.OK);
    }

    public MinijaxStatusInfo(final StatusType other) {
        setStatusInfo(other);
    }

    public MinijaxStatusInfo(final int statusCode, final String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public Family getFamily() {
        return Status.Family.familyOf(statusCode);
    }

    @Override
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(final String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public void setStatusInfo(final StatusType statusInfo) {
        if (statusInfo != null) {
            statusCode = statusInfo.getStatusCode();
            reasonPhrase = statusInfo.getReasonPhrase();
        } else {
            setStatusInfo(Status.OK);
        }
    }

    public void setStatusInfo(final int statusCode) {
        final Status status = Status.fromStatusCode(statusCode);
        if (status != null) {
            setStatusInfo(status);
        } else {
            setStatusCode(statusCode);
            setReasonPhrase(null);
        }
    }

    public void setStatusInfo(final int statusCode, final String reasonPhrase) {
        setStatusInfo(statusCode);
        setReasonPhrase(reasonPhrase);
    }
}
