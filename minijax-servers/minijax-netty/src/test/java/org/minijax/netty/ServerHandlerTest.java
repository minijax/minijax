package org.minijax.netty;

import static org.mockito.Mockito.*;

import java.util.concurrent.ScheduledExecutorService;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

import io.netty.channel.ChannelHandlerContext;

class ServerHandlerTest {

    @Test
    void testExceptionCaught() throws Exception {
        final Minijax minijax = new Minijax();
        final ScheduledExecutorService executor = mock(ScheduledExecutorService.class);
        final ServerHandler handler = new ServerHandler(minijax, executor);

        final ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);

        handler.exceptionCaught(ctx, null);

        verify(ctx).close();
    }

    @Test
    void testChannelReadComplete() throws Exception {
        final Minijax minijax = new Minijax();
        final ScheduledExecutorService executor = mock(ScheduledExecutorService.class);
        final ServerHandler handler = new ServerHandler(minijax, executor);

        final ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);

        handler.channelReadComplete(ctx);

        verify(ctx).flush();
    }
}
