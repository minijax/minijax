package org.minijax.bench;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class SunBench {

    public static class Handler implements HttpHandler {
        @Override
        public void handle(final HttpExchange e) throws IOException {
            e.getResponseHeaders().add("Content-Type", "text/plain;charset=utf-8");
            e.getResponseHeaders().add("Server", "Sun");

            final String str = "Hello World\r\n";

            e.sendResponseHeaders(200, str.length());

            final OutputStream out = e.getResponseBody();
            out.write(str.getBytes());
            out.flush();

            e.close();
        }
    }

    public static void main(final String[] args) throws Exception {
        System.setProperty("sun.net.httpserver.nodelay", "true");

        final HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new Handler());
        server.start();
    }
}
