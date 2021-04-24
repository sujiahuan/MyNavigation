package org.jiahuan.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
        Integer deviceId = Client.ctxIdDeviceIdMap.get(ctx.channel().id()).getId();
        Client.deviceIdChannelMap.remove(deviceId);
        Client.ctxIdDeviceIdMap.remove(ctx.channel().id());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        Integer deviceId = Client.ctxIdDeviceIdMap.get(ctx.channel().id()).getId();
        Client.deviceIdChannelMap.remove(deviceId);
        Client.ctxIdDeviceIdMap.remove(ctx.channel().id());
    }


}
