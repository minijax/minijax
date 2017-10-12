package org.minijax;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class MinijaxStatusInfo implements StatusType {
    private int statusCode;
    private Family family;
    private String reasonPhrase;

    public MinijaxStatusInfo() {
        this(Status.OK);
    }

    public MinijaxStatusInfo(final StatusType other) {
        setStatusInfo(other);
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
        return family;
    }

    public void setFamily(final Family family) {
        this.family = family;
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
            family = statusInfo.getFamily();
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
            setFamily(Status.Family.OTHER);
            setReasonPhrase(null);
        }
    }

    public void setStatusInfo(final int statusCode, final String reasonPhrase) {
        setStatusInfo(statusCode);
        setReasonPhrase(reasonPhrase);
    }
}
