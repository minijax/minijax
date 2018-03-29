package org.minijax.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Part;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class MockPart implements Part {
    private final MultivaluedMap<String, String> headers;
    private String name;
    private String value;
    private String submittedFileName;
    private InputStream inputStream;
    private File file;

    public MockPart() {
        headers = new MultivaluedHashMap<>();
    }

    public MockPart(final String name, final String value) {
        this();
        this.name = name;
        this.value = value;
    }

    public MockPart(final String name, final File file) {
        this();
        this.name = name;
        this.file = file;
        submittedFileName = file.getName();
    }

    @Override
    public String getName() {
        return name;
    }

    private void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    private void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String getSubmittedFileName() {
        return submittedFileName;
    }

    private void setSubmittedFileName(final String submittedFileName) {
        this.submittedFileName = submittedFileName;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    @Override
    public String getContentType() {
        return getHeader("Content-Type");
    }

    @Override
    public long getSize() {
        return value.length();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (inputStream == null) {
            if (file != null) {
                inputStream = new FileInputStream(file);
            } else if (value != null) {
                inputStream = new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
            } else {
                inputStream = new ByteArrayInputStream(new byte[0]);
            }
        }
        return inputStream;
    }

    @Override
    public void write(final String fileName) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() throws IOException {
        // No-op
    }

    private void addHeader(final String name, final String value) {
        headers.add(name, value);
    }

    @Override
    public String getHeader(final String name) {
        return headers.getFirst(name);
    }

    @Override
    public Collection<String> getHeaders(final String name) {
        return headers.get(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    public static List<Part> parseAll(final String str) throws IOException {
        final int index = str.indexOf('\n');
        final String boundary = str.substring(0, index) + "\n";
        if (str.length() <= 2 * boundary.length()) {
            return Collections.emptyList();
        }

        final String content = str.substring(index + 1, str.length() - boundary.length());
        final String[] strParts = content.split(boundary, 0);
        final List<Part> parts = new ArrayList<>(strParts.length);

        for (final String strPart : strParts) {
            parts.add(MockPart.parse(strPart));
        }

        return parts;
    }

    private static MockPart parse(final String str) throws IOException {
        final MockPart part = new MockPart();
        final StringBuilder valueBuilder = new StringBuilder();
        boolean headers = true;

        for (final String line : str.split("\n")) {
            if (headers) {
                if (line.isEmpty()) {
                    headers = false;
                } else {
                    parseHeader(part, line);
                }
            } else {
                if (valueBuilder.length() > 0) {
                    valueBuilder.append('\n');
                }
                valueBuilder.append(line);
            }
        }

        if (part.getHeader("Content-Transfer-Encoding") != null) {
            final File tmpFile = File.createTempFile("upload", ".tmp");
            Files.write(tmpFile.toPath(), Base64.getDecoder().decode(valueBuilder.toString()));
            part.setFile(tmpFile);
        } else {
            part.setValue(valueBuilder.toString());
        }

        return part;
    }

    private static void parseHeader(final MockPart part, final String line) {
        final int index = line.indexOf(':');
        final String key = line.substring(0, index).trim();
        final String value = line.substring(index + 1).trim();
        part.addHeader(key, value);

        if (key.equals("Content-Disposition")) {
            parseContentDisposition(part, value);
        }
    }

    private static void parseContentDisposition(final MockPart part, final String contentDisposition) {
        // Content-Disposition: form-data; name="key"
        // Content-Disposition: form-data; name="attachdoc"; filename="ajibot-256x256.png"
        for (final String str : contentDisposition.split(";\\s*")) {
            if (str.startsWith("name=\"")) {
                part.setName(str.substring(6, str.length() - 1));
            } else if (str.startsWith("filename=\"")) {
                part.setSubmittedFileName(str.substring(10, str.length() - 1));
            }
        }
    }
}
