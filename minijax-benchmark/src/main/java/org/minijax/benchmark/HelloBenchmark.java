package org.minijax.benchmark;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.Response;

import org.minijax.Minijax;
import org.minijax.test.MinijaxTestWebTarget;

public class HelloBenchmark {

    public static void main(final String[] args) throws IOException {
        System.out.println("Press enter to start...");
        System.in.read();

        final Minijax server = new Minijax();
        server.register(Hello.class);

        final URI uri = URI.create("/");

        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100_000_000; i++) {
            final Response response = new MinijaxTestWebTarget(server, uri)
                    .request()
                    .get();

            if (response.getStatus() != 200) {
                System.out.println("fail");
                return;
            }
        }

        final long endTime = System.currentTimeMillis();
        final long duration = endTime - startTime;
        System.out.println("duration = " + duration);
    }
}
