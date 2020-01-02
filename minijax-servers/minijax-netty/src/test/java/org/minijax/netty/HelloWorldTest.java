package org.minijax.netty;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledExecutorService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.minijax.Minijax;
import org.mockito.ArgumentCaptor;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

public class HelloWorldTest {

    @Path("/")
    public static class HelloResource {

        @GET
        public static Response hello() {
            return Response.ok("Hello world!", MediaType.TEXT_PLAIN)
                    .header("X-foo", "bar")
                    .build();
        }

        @POST
        @Consumes(MediaType.TEXT_PLAIN)
        public static Response echo(final String content) {
            return Response.ok("You said: " + content, MediaType.TEXT_PLAIN).build();
        }
    }

    @Test
    public void testHello() throws Exception {
        final Minijax minijax = new Minijax().register(HelloResource.class);

        final ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);

        final ServerHandler server = new ServerHandler(minijax, executorService);

        final ChannelFuture channelFuture = mock(ChannelFuture.class);

        final ChannelHandlerContext nettyCtx = mock(ChannelHandlerContext.class);
        when(nettyCtx.write(any())).thenReturn(channelFuture);

        final FullHttpRequest request = mock(FullHttpRequest.class);
        when(request.uri()).thenReturn("/");
        when(request.method()).thenReturn(HttpMethod.GET);

        try (final MinijaxNettyRequestContext ctx = new MinijaxNettyRequestContext(minijax.getDefaultApplication(), request)) {
            server.channelRead(nettyCtx, request);
        }

        final ArgumentCaptor<DefaultFullHttpResponse> argument = ArgumentCaptor.forClass(DefaultFullHttpResponse.class);
        verify(nettyCtx).write(argument.capture());
        assertEquals(HttpResponseStatus.OK, argument.getValue().status());
        assertEquals("Hello world!", argument.getValue().content().toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testEcho() throws Exception {
        final Minijax minijax = new Minijax().register(HelloResource.class);

        final ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);

        final ServerHandler server = new ServerHandler(minijax, executorService);

        final ChannelFuture channelFuture = mock(ChannelFuture.class);

        final ChannelHandlerContext nettyCtx = mock(ChannelHandlerContext.class);
        when(nettyCtx.write(any())).thenReturn(channelFuture);

        final FullHttpRequest request = mock(FullHttpRequest.class);
        when(request.uri()).thenReturn("/");
        when(request.method()).thenReturn(HttpMethod.POST);
        when(request.content()).thenReturn(Unpooled.wrappedBuffer("xyz".getBytes()));

        try (final MinijaxNettyRequestContext ctx = new MinijaxNettyRequestContext(minijax.getDefaultApplication(), request)) {
            server.channelRead(nettyCtx, request);
        }

        final ArgumentCaptor<DefaultFullHttpResponse> argument = ArgumentCaptor.forClass(DefaultFullHttpResponse.class);
        verify(nettyCtx).write(argument.capture());
        assertEquals(HttpResponseStatus.OK, argument.getValue().status());
        assertEquals("You said: xyz", argument.getValue().content().toString(StandardCharsets.UTF_8));
    }
}
