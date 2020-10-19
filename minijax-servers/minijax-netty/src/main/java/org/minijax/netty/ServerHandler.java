package org.minijax.netty;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.minijax.Minijax;
import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AsciiString;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.FastThreadLocal;

class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(ServerHandler.class);
    private final Minijax minijax;

    private static final FastThreadLocal<DateFormat> FORMAT = new FastThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        }
    };

    private static final CharSequence SERVER_NAME = AsciiString.cached("Netty");

    private CharSequence date = new AsciiString(FORMAT.get().format(new Date()));

    ServerHandler(final Minijax minijax, final ScheduledExecutorService service) {
        this.minijax = minijax;

        service.scheduleWithFixedDelay(new Runnable() {
            private final DateFormat format = FORMAT.get();

            @Override
            public void run() {
                date = new AsciiString(format.format(new Date()));
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg)
            throws Exception { // NOSONAR

        if (msg instanceof FullHttpRequest) {
            try {
                final FullHttpRequest request = (FullHttpRequest) msg;
                process(ctx, request);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }
    }

    private void process(final ChannelHandlerContext nettyCtx, final FullHttpRequest request)
            throws Exception { // NOSONAR

        final MinijaxApplicationContext application = minijax.getDefaultApplication();

        try (final MinijaxRequestContext minijaxCtx = new MinijaxNettyRequestContext(application, request)) {
            final Response minijaxResponse = application.handle(minijaxCtx);

            final MediaType mediaType = minijaxResponse.getMediaType();
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            EntityUtils.writeEntity(minijaxResponse.getEntity(), mediaType, application, outputStream);

            final int contentLength = outputStream.size();
            final ByteBuf buf = Unpooled.wrappedBuffer(outputStream.toByteArray());

            final DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, buf, false);
            response.headers()
                    .set(SERVER, SERVER_NAME)
                    .set(DATE, date)
                    .set(CONTENT_LENGTH, contentLength);

            if (mediaType != null) {
                response.headers().set(CONTENT_TYPE, mediaType);
            }

            for (final Entry<String, List<Object>> entry : minijaxResponse.getHeaders().entrySet()) {
                final String name = entry.getKey();
                for (final Object value : entry.getValue()) {
                    response.headers().set(name, value);
                }
            }

            nettyCtx.write(response).addListener(ChannelFutureListener.CLOSE);

        } catch (final Exception ex) { // NOSONAR
            LOG.error("Unhandled exception: {}", ex.getMessage(), ex);
            throw ex; // NOSONAR
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
