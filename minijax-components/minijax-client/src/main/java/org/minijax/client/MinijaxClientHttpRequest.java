package org.minijax.client;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class MinijaxClientHttpRequest extends HttpEntityEnclosingRequestBase  {
    private String method;

    @Override
    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }
}
