package org.minijax.netty;

import java.net.InetSocketAddress;

import org.minijax.Minijax;
import org.minijax.MinijaxServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.unix.UnixChannelOption;

public class MinijaxNettyServer implements MinijaxServer {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxNettyServer.class);
    private final Minijax minijax;
    private final EventLoopGroup loopGroup;
    private final Class<? extends ServerChannel> channelClass;
    private Channel channel;

    public MinijaxNettyServer(final Minijax minijax) {
        this.minijax = minijax;

        if (Epoll.isAvailable()) {
            loopGroup = new EpollEventLoopGroup();
            channelClass = EpollServerSocketChannel.class;
        } else {
            loopGroup = new NioEventLoopGroup();
            channelClass = NioServerSocketChannel.class;
        }
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void start() {
        LOG.info("Minijax starting on port {}", minijax.getPort());
        try {
            final InetSocketAddress inet = new InetSocketAddress(minijax.getPort());

            final ServerBootstrap b = new ServerBootstrap();
            b.option(UnixChannelOption.SO_REUSEPORT, true);
            b.option(ChannelOption.SO_BACKLOG, 8192);
            b.option(ChannelOption.SO_REUSEADDR, true);
            b.group(loopGroup).channel(channelClass).childHandler(new ServerInitializer(minijax, loopGroup.next()));
            b.childOption(ChannelOption.SO_REUSEADDR, true);

            channel = b.bind(inet).sync().channel();
            LOG.info("Netty started on {}", inet);

        } catch (final InterruptedException ex) {
            LOG.error(ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void stop() {
        LOG.info("Minijax stopping...");
        try {
            loopGroup.shutdownGracefully().sync();

        } catch (final InterruptedException ex) {
            LOG.error(ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        }
    }
}
