package org.minijax.netty;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;
import org.minijax.Minijax;
import org.mockito.ArgumentCaptor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class ServerInitializerTest {

    @Test
    public void testPipeline() throws Exception {
        final Minijax minijax = new Minijax();
        final ScheduledExecutorService executor = mock(ScheduledExecutorService.class);

        final ChannelPipeline pipeline = mock(ChannelPipeline.class);
        when(pipeline.addLast(anyString(), any())).thenReturn(pipeline);

        final SocketChannel ch = mock(SocketChannel.class);
        when(ch.pipeline()).thenReturn(pipeline);

        final ServerInitializer initializer = new ServerInitializer(minijax, executor);
        initializer.initChannel(ch);

        final ArgumentCaptor<ChannelHandler> argument = ArgumentCaptor.forClass(ChannelHandler.class);
        verify(pipeline, times(4)).addLast(anyString(), argument.capture());

        final List<ChannelHandler> handlers = argument.getAllValues();
        assertEquals(HttpResponseEncoder.class, handlers.get(0).getClass());
        assertEquals(HttpRequestDecoder.class, handlers.get(1).getClass());
        assertEquals(HttpObjectAggregator.class, handlers.get(2).getClass());
        assertEquals(ServerHandler.class, handlers.get(3).getClass());
    }
}
