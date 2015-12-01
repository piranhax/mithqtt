package com.github.longkerdandy.mithril.mqtt.broker.handler;

import com.github.longkerdandy.mithril.mqtt.api.metrics.MessageDirection;
import com.github.longkerdandy.mithril.mqtt.api.metrics.MetricsService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * Metrics Handler based on Bytes
 */
@SuppressWarnings("unused")
public class BytesMetricsHandler extends ChannelDuplexHandler {

    protected final MetricsService metrics;
    protected final String brokerId;

    public BytesMetricsHandler(MetricsService metrics, String brokerId) {
        this.metrics = metrics;
        this.brokerId = brokerId;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            this.metrics.measurement(this.brokerId, MessageDirection.IN, ((ByteBuf) msg).readableBytes());
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf) {
            this.metrics.measurement(this.brokerId, MessageDirection.OUT, ((ByteBuf) msg).writableBytes());
        }
        ctx.write(msg, promise);
    }
}
