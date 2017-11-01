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
        assertEquals(skip.getName(), "skip");
        assertEquals(skip.getDescription(), "number of records to skip");

        final Parameter limit = params.get(1);
        assertEquals(limit.getName(), "limit");
        assertEquals(limit.getDescription(), "maximum number of records to return");
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
        assertEquals("private", infoExtensions.get("x-accessLevel"));
        final Map<String, Object> operationExtensions = swagger.getPath("/rest/test").getGet().getVendorExtensions();
        assertEquals("/hello-world/v1/", operationExtensions.get("x-externalPath"));
    }

    @Test
    public void scanBeanParamResource() {
        final Swagger swagger = getSwagger(ResourceWithBeanParams.class);
        final List<Parameter> params = getParameters(swagger, "/bean/{id}");

        final HeaderParameter headerParam1 = (HeaderParameter) params.get(0);
        assertEquals(headerParam1.getDefaultValue(), 1);
        assertEquals(headerParam1.getName(), "test order annotation 1");

        final HeaderParameter headerParam2 = (HeaderParameter) params.get(1);
        assertEquals(headerParam2.getDefaultValue(), 2);
        assertEquals(headerParam2.getName(), "test order annotation 2");

        final QueryParameter priority1 = (QueryParameter) params.get(2);
        assertNull(priority1.getDefaultValue());
        assertEquals(priority1.getName(), "test priority 1");

        final QueryParameter priority2 = (QueryParameter) params.get(3);
        assertEquals(priority2.getDefaultValue(), 4);
        assertEquals(priority2.getName(), "test priority 2");

        final ModelImpl bodyParam1 = (ModelImpl) ((BodyParameter) params.get(4)).getSchema();
        assertEquals(bodyParam1.getDefaultValue(), "bodyParam");
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
