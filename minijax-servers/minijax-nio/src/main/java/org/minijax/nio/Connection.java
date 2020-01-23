package org.minijax.nio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.minijax.Minijax;
import org.minijax.commons.MinijaxException;
import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxUriInfo;
import org.minijax.rs.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Connection {
    private static final Logger LOG = LoggerFactory.getLogger(Connection.class);
    private static final byte[] HTTP_PREFIX = "HTTP/".getBytes();
    private static final byte[] DATE_HEADER = "Date: ".getBytes();
    private static final byte[] CONTENT_TYPE_HEADER = "Content-Type: ".getBytes();
    private static final byte[] CONTENT_LENGTH_HEADER = "Content-Length: ".getBytes();
    private static final byte[] CRLF = "\r\n".getBytes();
    private final Minijax minijax;
    private final ByteChannel channel;
    private final ByteBuffer buffer;
    private URI uri;
    private String method;
    private String protocol;
    private String version;
    private boolean onlyHeader;
    private boolean keepAlive;
    private MultivaluedMap<String, String> requestHeaders;
    private InputStream requestEntityStream;
    private Response response;
    private ByteArrayOutputStream bufferedOutputStream;

    public Connection(final Minijax minijax, final ByteChannel channel, final ByteBuffer buffer) {
        this.minijax = minijax;
        this.channel = channel;
        this.buffer = buffer;
    }

    Connection(final Minijax minijax, final ByteChannel channel) {
        this(minijax, channel, ByteBuffer.allocate(1000));
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public boolean handle() throws IOException {
        if (!read()) {
            return false;
        }
        process();
        return write();
    }

    private boolean read() {
        // Clears the buffer back to blank slate.
        buffer.clear();

        // Read the channel into the buffer.
        // TODO: Handle request size greater than buffer size.
        try {
            channel.read(buffer);
        } catch (final IOException ex) {
            // Read error
            // Usually "Connection reset by peer"
            // Cancel the request
            LOG.debug("Read exception: {}", ex.getMessage(), ex);
            return false;
        }

        // Flip the buffer so we can read the request contents.
        buffer.flip();

        // Read the request line.
        final String requestLine = readLine();
        if (requestLine == null) {
            // When a connection closes, a "read" event is emitted
            // But no content is available
            return false;
        }

        // Parse the request line.
        // Format: VERB URI PROTOCOL/VERSION
        // Example: GET /users HTTP/1.1
        final int index1 = requestLine.indexOf(' ');
        final int index2 = requestLine.indexOf(' ', index1 + 1);
        final int index3 = requestLine.indexOf('/', index2 + 1);
        if (index1 < 0 || index2 < 0 || index3 < 0) {
            return false;
        }

        method = requestLine.substring(0, index1);
        uri = URI.create(requestLine.substring(index1 + 1, index2));
        protocol = requestLine.substring(index2 + 1, index3);
        version = requestLine.substring(index3 + 1);
        LOG.debug("{} {} {} {}", method, uri, protocol, version);

        if (!version.equals("1.0") && !version.equals("1.1")) {
            return false;
        }

        requestHeaders = readHeaders();
        requestEntityStream = readContentBody();
        onlyHeader = method.equals("HEAD");
        keepAlive = shouldKeepAlive();
        return true;
    }


    private void process() throws IOException {
        final MinijaxApplicationContext application = minijax.getDefaultApplication();
        try (final MinijaxNioRequestContext ctx = new MinijaxNioRequestContext(
                application,
                method,
                new MinijaxUriInfo(uri),
                new MinijaxNioHttpHeaders(requestHeaders),
                requestEntityStream)) {

            response = application.handle(ctx);

            if (onlyHeader) {
                bufferedOutputStream = null;
            } else {
                bufferedOutputStream = new ByteArrayOutputStream();
                EntityUtils.writeEntity(response.getEntity(), response.getMediaType(), application, bufferedOutputStream);
            }
        }
    }

    private boolean write() throws IOException {
        // Switch to writing
        buffer.clear();

        // Write the response status line
        buffer.put(HTTP_PREFIX);
        buffer.put(version.getBytes());
        buffer.put((byte) ' ');
        buffer.put(Integer.toString(response.getStatus()).getBytes());
        buffer.put(CRLF);

        // Write the "Date" header
        buffer.put(DATE_HEADER);
        buffer.put(DateHeader.get());
        buffer.put(CRLF);

        // Write the "Content-Type" header
        final MediaType mediaType = response.getMediaType();
        if (mediaType != null) {
            buffer.put(CONTENT_TYPE_HEADER);
            buffer.put(mediaType.toString().getBytes());
            buffer.put(CRLF);
        }

        // Write the "Content-Length" header
        if (bufferedOutputStream != null) {
            buffer.put(CONTENT_LENGTH_HEADER);
            buffer.put(Integer.toString(bufferedOutputStream.size()).getBytes());
            buffer.put(CRLF);
        }

        // Write additional headers
        for (final Entry<String, List<Object>> entry : response.getHeaders().entrySet()) {
            final byte[] keyBytes = entry.getKey().getBytes();
            for (final Object value : entry.getValue()) {
                if (value != null) {
                    buffer.put(keyBytes);
                    buffer.put(": ".getBytes());
                    buffer.put(value.toString().getBytes());
                    buffer.put(CRLF);
                }
            }
        }

        // Write the "Connection" header
        if (keepAlive && "1.0".equals(version)) {
            buffer.put("Connection: keep-alive\r\n".getBytes());
        } else if (!keepAlive && "1.1".equals(version)) {
            buffer.put("Connection: close\r\n".getBytes());
        }

        // Write a blank line to separate content
        buffer.put(CRLF);

        if (!onlyHeader && bufferedOutputStream != null && bufferedOutputStream.size() > 0) {
            // Push the content to the channel
            final byte[] content = bufferedOutputStream.toByteArray();
            int index = 0;
            while (index < content.length) {
                final int length = Math.min(content.length - index, buffer.remaining());
                buffer.put(content, index, length);
                index += length;
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }
        } else {
          // Push only the headers to the channel
          buffer.flip();
          channel.write(buffer);
        }

        return keepAlive;
    }

    // Additional helpers

    private String readLine() {
        // TODO: Handle channel size greater than buffer size
        final StringBuilder b = new StringBuilder();
        while (buffer.hasRemaining()) {
            final int curr = buffer.get();
            if (curr == '\r') {
                if (buffer.get() != '\n') {
                    throw new MinijaxException("Unexpected CRLF pattern");
                }
                break;
            }
            b.append((char) curr);
        }
        return b.toString();
    }

    private MultivaluedMap<String, String> readHeaders() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        String line;
        while ((line = readLine()) != null && line.length() > 0) {
            final int colonIndex = line.indexOf(':');
            if (colonIndex >= 0) {
                final String key = line.substring(0, colonIndex).trim();
                final String value = line.substring(colonIndex + 1).trim();
                headers.add(key, value);
            }
        }

        return headers;
    }

    private InputStream readContentBody() {
        final String lenStr = requestHeaders.getFirst("Content-Length");
        if (lenStr == null) {
            return null;
        }

        final int len = Integer.parseInt(lenStr);
        if (len <= 0) {
            return null;
        }

        final byte[] contentBody = new byte[len];
        buffer.get(contentBody);
        return new ByteArrayInputStream(contentBody);
    }

    private boolean shouldKeepAlive() {
        final String str = requestHeaders.getFirst("Connection");
        final boolean keepAlive = str != null && str.contains("keep-alive");
        final boolean close = str != null && str.contains("close");
        return (version.equals("1.0") && keepAlive) || (version.equals("1.1") && !close);
    }
}
