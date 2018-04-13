package org.minijax.multipart;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.minijax.util.IOUtils;

public class PartTest {

    @Test
    public void testKeyValue() throws IOException {
        final String mockContent =
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"key\"\n" +
                "\n" +
                "myvalue1\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ--";

        final List<Part> parts = Part.parseAll(mockContent);
        assertNotNull(parts);
        assertEquals(1, parts.size());

        final Part part = parts.get(0);
        assertEquals("key", part.getName());
        assertEquals("myvalue1", IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8));
    }

    @Test
    public void testMultipleLines() throws IOException {
        final String mockContent =
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"key\"\n" +
                "\n" +
                "myvalue1\n" +
                "myvalue1\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ--";

        final List<Part> parts = Part.parseAll(mockContent);
        assertNotNull(parts);
        assertEquals(1, parts.size());

        final Part part = parts.get(0);
        assertEquals("key", part.getName());
        assertEquals("myvalue1" + "\n" + "myvalue1", IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8));
    }

    @Test
    public void testMultipleParts() throws IOException {
        final String mockContent =
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"key1\"\n" +
                "\n" +
                "myvalue1\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"key2\"\n" +
                "\n" +
                "myvalue2\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ--";

        final List<Part> parts = Part.parseAll(mockContent);
        assertNotNull(parts);
        assertEquals(2, parts.size());

        final Part p1 = parts.get(0);
        assertEquals("key1", p1.getName());
        assertEquals("myvalue1", IOUtils.toString(p1.getInputStream(), StandardCharsets.UTF_8));

        final Part p2 = parts.get(1);
        assertEquals("key2", p2.getName());
        assertEquals("myvalue2", IOUtils.toString(p2.getInputStream(), StandardCharsets.UTF_8));
    }

    @Test
    public void testFile() throws IOException {
        final String mockContent =
                "------WebKitFormBoundaryoyr33TDQmcUAD5A7\n" +
                "Content-Disposition: form-data; name=\"attachdoc\"; filename=\"hello.txt\"\n" +
                "Content-Type: text/plain\n" +
                "X-Other-Header: foo\n" +
                "X-Other-Header: bar\n" +
                "\n" +
                "Hello world\n" +
                "------WebKitFormBoundaryoyr33TDQmcUAD5A7--";

        final List<Part> parts = Part.parseAll(mockContent);
        assertNotNull(parts);
        assertEquals(1, parts.size());

        final Part part = parts.get(0);
        assertEquals("attachdoc", part.getName());
        assertEquals("text/plain", part.getContentType());
        assertEquals("hello.txt", part.getSubmittedFileName());
        assertEquals(11, part.getSize());
        assertEquals("Hello world", IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8));
        assertEquals("Hello world", part.getValue());

        assertEquals(
                new HashSet<>(Arrays.asList("Content-Disposition", "Content-Type", "X-Other-Header")),
                new HashSet<>(part.getHeaderNames()));

        assertEquals(Arrays.asList("foo", "bar"), part.getHeaders("X-Other-Header"));
    }
}
