package org.minijax;

class MinijaxStaticResource {
    private final String resourceBase;
    private final String pathSpec;

    /**
     * Creates a new static resource.
     *
     * @param resourceBase The path that will be passed as "resourceBase" to DefaultServlet.
     * @param pathSpec The servlet path spec.
     */
    public MinijaxStaticResource(final String resourceBase, final String pathSpec) {
        this.resourceBase = resourceBase;
        this.pathSpec = pathSpec;
    }

    public String getResourceBase() {
        return resourceBase;
    }

    public String getPathSpec() {
        return pathSpec;
    }
}
