package org.minijax.netty;

import java.util.concurrent.ScheduledExecutorService;

import org.minijax.Minijax;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private final Minijax minijax;
    private final ScheduledExecutorService service;

    ServerInitializer(final Minijax minijax, final ScheduledExecutorService service) {
        this.minijax = minijax;
        this.service = service;
    }

    @Override
    public void initChannel(final SocketChannel ch) {
        ch.pipeline()
                .addLast("encoder", new HttpResponseEncoder())
                .addLast("decoder", new HttpRequestDecoder(4096, 8192, 8192, false))
                .addLast("aggregator", new HttpObjectAggregator(1048576))
                .addLast("handler", new ServerHandler(minijax, service));
    }
}
