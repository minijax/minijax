package org.minijax.swagger.params;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Extension of {@link BaseBean} to ensure we process hierarchies properly.
 */
public class ChildBean extends BaseBean {

    @HeaderParam("HeaderParam")
    private String headerParam;
    public String getHeaderParam() {
        return headerParam;
    }
    public void setHeaderParam(final String headerParam) {
        this.headerParam = headerParam;
    }

    @PathParam("PathParam")
    private String pathParam;
    public void setPathParam(final String pathParam) {
        this.pathParam = pathParam;
    }
    public String getPathParam() {
        return pathParam;
    }

    @QueryParam("QueryParam")
    private String queryParam;
    public String getQueryParam() {
        return queryParam;
    }
    public void setQueryParam(final String queryParam) {
        this.queryParam = queryParam;
    }
}
