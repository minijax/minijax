package org.minijax.swagger;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.minijax.swagger.resources.ResourceWithBeanParams;
import org.minijax.swagger.resources.ResourceWithComplexBodyInputType;
import org.minijax.swagger.resources.ResourceWithExtensions;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.DefaultReaderConfig;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;

public class ScannerTest {

    @Test
    public void scanSimpleResource() {
        final Swagger swagger = getSwagger(ResourceWithBeanParams.class);
        final List<Parameter> params = getParameters(swagger, "/{id}");

        final Parameter skip = params.get(0);
        assertEquals("skip", skip.getName());
        assertEquals("number of records to skip", skip.getDescription());

        final Parameter limit = params.get(1);
        assertEquals("limit", limit.getName());
        assertEquals("maximum number of records to return", limit.getDescription());
    }

    @Test
    public void scanAnotherResource() {
        final Swagger swagger = getSwagger(ResourceWithComplexBodyInputType.class);

        final Operation post = swagger.getPaths().get("/myapi/testPostWithBody").getPost();
        assertNotNull(post);

        assertNotNull(swagger.getDefinitions());
        assertNotNull(swagger.getDefinitions().get("ClassWithString"));
    }

    @Test
    public void scanResourceWithExtensions() throws JsonProcessingException {
        final Swagger swagger = getSwagger(ResourceWithExtensions.class);
        assertNotNull( swagger );

        final Map<String, Object> infoExtensions = swagger.getInfo().getVendorExtensions();
        assertEquals(infoExtensions.get("x-accessLevel"), "private");
        final Map<String, Object> operationExtensions = swagger.getPath("/rest/test").getGet().getVendorExtensions();
        assertEquals(operationExtensions.get("x-externalPath"), "/hello-world/v1/");
    }

    @Test
    public void scanBeanParamResource() {
        final Swagger swagger = getSwagger(ResourceWithBeanParams.class);
        final List<Parameter> params = getParameters(swagger, "/bean/{id}");

        final HeaderParameter headerParam1 = (HeaderParameter) params.get(0);
        assertEquals(1, headerParam1.getDefaultValue());
        assertEquals("test order annotation 1", headerParam1.getName());

        final HeaderParameter headerParam2 = (HeaderParameter) params.get(1);
        assertEquals(2, headerParam2.getDefaultValue());
        assertEquals("test order annotation 2", headerParam2.getName());

        final QueryParameter priority1 = (QueryParameter) params.get(2);
        assertNull(priority1.getDefaultValue());
        assertEquals("test priority 1", priority1.getName());

        final QueryParameter priority2 = (QueryParameter) params.get(3);
        assertEquals(4, priority2.getDefaultValue());
        assertEquals("test priority 2", priority2.getName());

        final ModelImpl bodyParam1 = (ModelImpl) ((BodyParameter) params.get(4)).getSchema();
        assertEquals("bodyParam", bodyParam1.getDefaultValue());
    }

    private List<Parameter> getParameters(final Swagger swagger, final String path) {
        return swagger.getPaths().get(path).getGet().getParameters();
    }

    private Swagger getSwagger(final Class<?> clas) {
        final DefaultReaderConfig config = new DefaultReaderConfig();
        config.setScanAllResources(true);
        return new Reader(new Swagger(), config).read(clas);
    }
}
