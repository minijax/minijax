package org.minijax.multipart;

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

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class Part {
    private final MultivaluedMap<String, String> headers;
    private String name;
    private String value;
    private String submittedFileName;
    private InputStream inputStream;
    private File file;

    public Part() {
        headers = new MultivaluedHashMap<>();
    }

    public Part(final String name, final String value) {
        this();
        this.name = name;
        this.value = value;
    }

    public Part(final String name, final File file) {
        this();
        this.name = name;
        this.file = file;
        submittedFileName = file.getName();
    }

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

    public String getSubmittedFileName() {
        return submittedFileName;
    }

    private void setSubmittedFileName(final String submittedFileName) {
        this.submittedFileName = submittedFileName;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public String getContentType() {
        return getHeader("Content-Type");
    }

    public long getSize() {
        return value.length();
    }

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

    private void addHeader(final String name, final String value) {
        headers.add(name, value);
    }

    public String getHeader(final String name) {
        return headers.getFirst(name);
    }

    public Collection<String> getHeaders(final String name) {
        return headers.get(name);
    }

    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    public static List<Part> parseAll(final String str) throws IOException {
        final int index = str.indexOf('\n');
        if (index < 0) {
            return Collections.emptyList();
        }

        final String boundary = str.substring(0, index);
        final String endBoundary = boundary + "--";
        final int endIndex = str.indexOf(endBoundary);
        final String content;
        if (endIndex > 0) {
            content = str.substring(index + 1, endIndex);
        } else {
            content = str.substring(index + 1);
        }

        final String separator = boundary + "\n";
        final String[] strParts = content.split(separator, 0);
        final List<Part> parts = new ArrayList<>(strParts.length);

        for (final String strPart : strParts) {
            parts.add(Part.parse(strPart));
        }

        return parts;
    }

    private static Part parse(final String str) throws IOException {
        final Part part = new Part();
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

    private static void parseHeader(final Part part, final String line) {
        final int index = line.indexOf(':');
        final String key = line.substring(0, index).trim();
        final String value = line.substring(index + 1).trim();
        part.addHeader(key, value);

        if (key.equals("Content-Disposition")) {
            parseContentDisposition(part, value);
        }
    }

    private static void parseContentDisposition(final Part part, final String contentDisposition) {
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
