package org.minijax.nio;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.minijax.Minijax;
import org.minijax.MinijaxApplicationContext;
import org.minijax.MinijaxServer;
import org.minijax.MinijaxUriInfo;
import org.minijax.util.CloseUtils;
import org.minijax.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxNioServer implements MinijaxServer, Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxNioServer.class);
    private static final SocketAddress ENDPOINT = new InetSocketAddress(8020);
    private static final int KEEP_ALIVE_TIMEOUT = 20000;
    private final Minijax minijax;
    private final ExecutorService executor;
    private final ExecutorCompletionService<SocketChannel> keepAliveChannels;
    ServerSocketChannel serverChannel;
    ServerSocket serverSocket;
    Selector selector;
    volatile boolean running;

    public MinijaxNioServer(final Minijax minijax) {
        this.minijax = minijax;
        this.executor = Executors.newFixedThreadPool(2);
        this.keepAliveChannels = new ExecutorCompletionService<SocketChannel>(executor);
    }

    @Override
    public void start() {
        try {
            initSocket();

            long lastCleanup = System.currentTimeMillis();
            while (running) {
                registerKeepAliveChannels();
                lastCleanup = cleanupKeepAliveChannels(lastCleanup, selector.keys());

                if (selector.select(10) == 0) {
                    continue;
                }

                for (final SelectionKey key : selector.selectedKeys()) {
                    if (key.isAcceptable()) {
                        // First connection.
                        final SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(true);
                        schedule(clientChannel);
                        key.cancel();
                    } else if (key.isReadable()) {
                        // Subsequent connection (after keep alive).
                        final SocketChannel clientChannel = (SocketChannel) key.channel();
                        key.cancel();
                        clientChannel.configureBlocking(true);
                        schedule(clientChannel);
                    }
                }

                selector.selectNow();
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            }

        } catch (final InterruptedException ex) {
            LOG.error("Interrupted: {}", ex.getMessage(), ex);
            Thread.currentThread().interrupt();

        } catch (IOException | ExecutionException ex) {
            LOG.error("Unexpected: {}", ex.getMessage(), ex);

        } finally {
            close();
        }
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void close() {
        running = false;
        CloseUtils.closeQuietly(selector);
        CloseUtils.closeQuietly(serverSocket);
        CloseUtils.closeQuietly(serverChannel);
    }

    /**
     * Initializes the server socket and selector.
     * Package private so tests can override.
     */
    void initSocket() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        serverSocket = serverChannel.socket();
        serverSocket.bind(ENDPOINT);

        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        running = true;
    }

    /**
     * Check the pool for completion of one or more of its task and in case it's
     * keep-alive register the connection with the selector.
     */
    private void registerKeepAliveChannels()
            throws IOException, InterruptedException, ExecutionException {

        final long now = System.currentTimeMillis();
        Future<SocketChannel> alive;
        while ((alive = keepAliveChannels.poll()) != null) {
            final SocketChannel channel = alive.get();
            if (channel != null) {
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ, now);
            }
        }
    }

    /**
     * Each x seconds check all registrations if they have timed-out in which case
     * the corresponding channel is closed.
     */
    private long cleanupKeepAliveChannels(final long lastCleanup, final Set<SelectionKey> keys)
            throws IOException {

        final long now = System.currentTimeMillis();
        if (now - lastCleanup > 5000) {
            for (final SelectionKey key : keys) {
                if ((key.interestOps() & SelectionKey.OP_READ) != 0
                        && ((Long) key.attachment()) + KEEP_ALIVE_TIMEOUT < now) {
                    LOG.debug("Closing connection to {}", key.channel());
                    key.channel().close();
                }
            }
            return now;
        } else {
            return lastCleanup;
        }
    }

    private void schedule(final SocketChannel clientChannel) {
        keepAliveChannels.submit(new Callable<SocketChannel>() {
            @Override
            public SocketChannel call() throws Exception {
                final Socket client = clientChannel.socket();
                final boolean keepAlive = handleRequest(client);

                if (keepAlive) {
                    return clientChannel;
                }

                if (!client.isClosed()) {
                    client.close();
                }

                return null;
            }
        });
    }

    boolean handleRequest(final Socket client) throws IOException {
        final InputStream is = client.getInputStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final String requestLine = reader.readLine();

        // Format: VERB URI PROTOCOL/VERSION
        // Example: GET /users HTTP/1.1
        final int index1 = requestLine.indexOf(' ');
        final int index2 = requestLine.indexOf(' ', index1 + 1);
        final int index3 = requestLine.indexOf('/', index2 + 1);
        if (index1 < 0 || index2 < 0 || index3 < 0) {
            return false;
        }

        final String method = requestLine.substring(0, index1);
        final String uri = requestLine.substring(index1 + 1, index2);
        final String protocol = requestLine.substring(index2 + 1, index3);
        final String version = requestLine.substring(index3 + 1);
        LOG.debug("{} {} {} {}", method, uri, protocol, version);

        final MinijaxUriInfo uriInfo = new MinijaxUriInfo(URI.create(uri));
        final MinijaxNioHttpHeaders headers = readHeaders(reader);
        final InputStream entityStream = readContentBody(reader, headers.getLength());

        final MinijaxApplicationContext application = minijax.getDefaultApplication();
        try (final MinijaxNioRequestContext ctx = new MinijaxNioRequestContext(
                application,
                method,
                uriInfo,
                headers,
                entityStream)) {

            final Response response = application.handle(ctx);

            // Write content body to a temp buffer
            // Necessary to get the content length?
            final ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream();
            EntityUtils.writeEntity(response.getEntity(), response.getMediaType(), application, bufferedOutputStream);
            response.getHeaders().putSingle("Content-Length", Integer.toString(bufferedOutputStream.size()));

            // Write the result
            final boolean onlyHeader = "HEAD".equals(method);
            final boolean keepAlive = shouldKeepAlive(version, headers);

            final OutputStream outputStream = client.getOutputStream();
            final Writer writer = new OutputStreamWriter(outputStream);

            if (!version.equals("1.0") && !version.equals("1.1")) {
                fail(writer, "501 Version not implemented " + version);
                return false;
            }

            writer.append("HTTP/")
                    .append(version)
                    .append(' ')
                    .append(Integer.toString(response.getStatus()))
                    .append("\r\n");

            for (final Entry<String, List<Object>> entry : response.getHeaders().entrySet()) {
                final String name = entry.getKey();
                for (final Object value : entry.getValue()) {
                    writer.append(name).append(": ").append(value.toString()).append("\r\n");
                }
            }

            if (keepAlive && "1.0".equals(version)) {
                writer.append("Connection: keep-alive\r\n");
            } else if (!keepAlive && "1.1".equals(version)) {
                writer.append("Connection: close\r\n");
            }

            writer.append("\r\n");
            writer.flush();

            if (!onlyHeader) {
                bufferedOutputStream.writeTo(outputStream);
            }

            return keepAlive;
        }
    }

    private MinijaxNioHttpHeaders readHeaders(final BufferedReader reader) throws IOException {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        String line;
        while ((line = reader.readLine()) != null && line.length() > 0) {
            final int colonIndex = line.indexOf(':');
            if (colonIndex >= 0) {
                final String key = line.substring(0, colonIndex).trim();
                final String value = line.substring(colonIndex + 1).trim();
                headers.add(key, value);
            }
        }

        return new MinijaxNioHttpHeaders(headers);
    }

    private InputStream readContentBody(final BufferedReader reader, final int len) throws IOException {
        if (len <= 0) {
            return null;
        }

        final byte[] contentBody = new byte[len];
        for (int i = 0; i < len; i++) {
            contentBody[i] = (byte) reader.read();
        }
        return new ByteArrayInputStream(contentBody);
    }

    private boolean shouldKeepAlive(final String version, final MinijaxNioHttpHeaders headers) {
        final String str = headers.getHeaderString("Connection");
        final boolean keepAlive = str != null && str.contains("keep-alive");
        final boolean close = str != null && str.contains("close");
        return (version.equals("1.0") && keepAlive) || (version.equals("1.1") && !close);
    }

    private void fail(final Writer writer, final String code) throws IOException {
        writer.append("HTTP/1.0 ").append(code).append("\r\n");
    }
}
