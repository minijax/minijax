package org.minijax.swagger;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Part;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;

import org.junit.Test;
import org.minijax.swagger.MinijaxSwaggerExtension;
import org.minijax.swagger.models.TestEnum;
import org.minijax.swagger.params.BaseBean;
import org.minijax.swagger.params.ChildBean;
import org.minijax.swagger.params.EnumBean;
import org.minijax.swagger.params.RefBean;
import org.minijax.swagger.resources.Resource2031;
import org.minijax.swagger.resources.ResourceWithFormData;
import org.minijax.swagger.resources.ResourceWithJacksonBean;
import org.minijax.swagger.resources.ResourceWithKnownInjections;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import io.swagger.jaxrs.DefaultParameterExtension;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;

public class SwaggerJersey2JaxrsTest {

    // Here so that we can get the params with the @BeanParam annotation instantiated properly
    void testRoute(@BeanParam final BaseBean baseBean, @BeanParam final ChildBean childBean, @BeanParam final RefBean refBean,
                   @BeanParam final EnumBean enumBean, final Integer nonBean) {
    }

    void testFormDataParamRoute(@FormParam("file") final InputStream uploadedInputStream,
                                @FormParam("file") final Part fileDetail) {
    }

    @Test
    public void testAllTypes() {
        for (final Class cls : Arrays.asList(BaseBean.class, ChildBean.class, RefBean.class)) {
            final Set<Type> typesToSkip = new java.util.HashSet<Type>();
            new MinijaxSwaggerExtension().extractParameters(new ArrayList<Annotation>(), cls, typesToSkip, SwaggerExtensions.chain());
            assertEquals(typesToSkip.size(), 0);
        }
    }

    @Test
    public void returnProperBeanParam() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("testRoute", BaseBean.class, ChildBean.class, RefBean.class, EnumBean.class, Integer.class);
        final List<Pair<Type, Annotation[]>> parameters = getParameters(method.getGenericParameterTypes(), method.getParameterAnnotations());

        for (final Pair<Type, Annotation[]> parameter : parameters) {
            final Type parameterType = parameter.first();
            final List<Parameter> swaggerParams = new MinijaxSwaggerExtension().extractParameters(Arrays.asList(parameter.second()),
                    parameterType, new HashSet<Type>(), SwaggerExtensions.chain());
            // Ensure proper number of parameters returned
            if (parameterType.equals(BaseBean.class)) {
                assertEquals(swaggerParams.size(), 2);
            } else if (parameterType.equals(ChildBean.class)) {
                assertEquals(swaggerParams.size(), 5);
            } else if (parameterType.equals(RefBean.class)) {
                assertEquals(swaggerParams.size(), 5);
            } else if (parameterType.equals(EnumBean.class)) {
                assertEquals(swaggerParams.size(), 1);
                final HeaderParameter enumParam = (HeaderParameter) swaggerParams.get(0);
                assertEquals(enumParam.getType(), "string");
                final List<String> enumValues = new ArrayList<>(Collections2.transform(Arrays.asList(TestEnum.values()), Functions.toStringFunction()));
                assertEquals(enumParam.getEnum(), enumValues);
            } else if (parameterType.equals(Integer.class)) {
                assertEquals(swaggerParams.size(), 0);
            } else {
                fail(String.format("Parameter of type %s was not expected", parameterType));
            }

            // Ensure the proper parameter type and name is returned (The rest is handled by pre-existing logic)
            for (final Parameter param : swaggerParams) {
                assertEquals(param.getName(), param.getClass().getSimpleName().replace("eter", ""));
            }
        }
    }

    @Test
    public void returnProperBeanParamWithDefaultParameterExtension() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("testRoute", BaseBean.class, ChildBean.class, RefBean.class, EnumBean.class, Integer.class);
        final List<Pair<Type, Annotation[]>> parameters = getParameters(method.getGenericParameterTypes(), method.getParameterAnnotations());

        for (final Pair<Type, Annotation[]> parameter : parameters) {
            final Type parameterType = parameter.first();
            final List<Parameter> swaggerParams = new DefaultParameterExtension().extractParameters(Arrays.asList(parameter.second()),
                    parameterType, new HashSet<Type>(), SwaggerExtensions.chain());
            // Ensure proper number of parameters returned
            if (parameterType.equals(BaseBean.class)) {
                assertEquals(swaggerParams.size(), 2);
            } else if (parameterType.equals(ChildBean.class)) {
                assertEquals(swaggerParams.size(), 5);
            } else if (parameterType.equals(RefBean.class)) {
                assertEquals(swaggerParams.size(), 5);
            } else if (parameterType.equals(EnumBean.class)) {
                assertEquals(swaggerParams.size(), 1);
                final HeaderParameter enumParam = (HeaderParameter) swaggerParams.get(0);
                assertEquals(enumParam.getType(), "string");
                final List<String> enumValues = new ArrayList<>(Collections2.transform(Arrays.asList(TestEnum.values()), Functions.toStringFunction()));
                assertEquals(enumParam.getEnum(), enumValues);
            } else if (parameterType.equals(Integer.class)) {
                assertEquals(swaggerParams.size(), 0);
            } else {
                fail(String.format("Parameter of type %s was not expected", parameterType));
            }

            // Ensure the proper parameter type and name is returned (The rest is handled by pre-existing logic)
            for (final Parameter param : swaggerParams) {
                assertEquals(param.getName(), param.getClass().getSimpleName().replace("eter", ""));
            }
        }
    }


    @Test
    public void returnProperFormDataParam() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("testFormDataParamRoute", InputStream.class, Part.class);
        final List<Pair<Type, Annotation[]>> parameters = getParameters(method.getGenericParameterTypes(), method.getParameterAnnotations());

        for (final Pair<Type, Annotation[]> parameter : parameters) {
            final Type parameterType = parameter.first();
            final List<Parameter> swaggerParams = new MinijaxSwaggerExtension().extractParameters(Arrays.asList(parameter.second()),
                    parameterType, new HashSet<Type>(), SwaggerExtensions.chain());
            assertEquals(((FormParameter) swaggerParams.get(0)).getType(), "file");
        }
    }

    private List<Pair<Type, Annotation[]>> getParameters(final Type[] type, final Annotation[][] annotations) {
        final Iterator<Type> typeIterator = Arrays.asList(type).iterator();
        final Iterator<Annotation[]> paramIterator = Arrays.asList(annotations).iterator();
        final List<Pair<Type, Annotation[]>> result = new ArrayList<Pair<Type, Annotation[]>>();
        while (paramIterator.hasNext() && typeIterator.hasNext()) {
            final Pair<Type, Annotation[]> pair = new Pair<Type, Annotation[]>(typeIterator.next(), paramIterator.next());
            result.add(pair);
        }
        return result;
    }

    @Test
    public void scanClassAnfFieldLevelAnnotations() {
        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithKnownInjections.class);
        final List<Parameter> resourceParameters = swagger.getPaths().get("/resource/{id}").getGet().getParameters();
        assertNotNull(resourceParameters);
        assertEquals(resourceParameters.size(), 4);
        assertEquals(getName(resourceParameters, 0), "fieldParam");
        assertEquals(getName(resourceParameters, 1), "skip");
        assertEquals(getName(resourceParameters, 2), "limit");
        assertEquals(getName(resourceParameters, 3), "methodParam");
    }

    @Test
    public void testFormDataBodyPart() {
        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithFormData.class);
        final List<Parameter> parameters = swagger.getPath("/test/document/{documentName}.json").getPost().getParameters();
        assertEquals(parameters.size(), 5);
        assertEquals(parameters.get(0).getName(), "documentName");
        assertEquals(parameters.get(1).getName(), "document");
        assertEquals(parameters.get(2).getName(), "document2");
        assertEquals(parameters.get(3).getName(), "input");
        assertEquals(parameters.get(4).getName(), "id");
    }

    @Test
    public void testJacksonFeatures() {
        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithJacksonBean.class);
        final Model o = swagger.getDefinitions().get("JacksonBean");

        assertEquals(o.getProperties().keySet(), Sets.newHashSet("identity", "bean", "code", "message",
                "precodesuf", "premessagesuf"));
    }

    @Test
    public void testIssue2031() {
        final Swagger swagger = new Reader(new Swagger()).read(Resource2031.class);
        assertNotNull(swagger);

    }

    private String getName(final List<Parameter> resourceParameters, final int i) {
        return resourceParameters.get(i).getName();
    }
}
