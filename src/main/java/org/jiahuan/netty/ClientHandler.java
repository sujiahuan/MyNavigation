package org.jiahuan.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.service.analog.IAnDataTypeService;
import org.jiahuan.service.analog.IAnRemoteControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static ClientHandler clientHandler;

    @PostConstruct
    public void init() {
        clientHandler = this;
    }

    @Autowired
    private IAnRemoteControlService iAnRemoteControlService;
    @Autowired
    private IAnDataTypeService iAnDataTypeService;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx){
        System.out.println("开始注册");
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("注册失败");
        synchronized (ctx.channel()){
            ctx.channel().notifyAll();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("注册成功");
        synchronized (ctx.channel()){
            ctx.channel().notifyAll();
        }
    }

    /**
     * 收到服务器返回的消息
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        SysDevice sysDevice = NettyClient.ctxIdDeviceIdMap.get(ctx.channel().id());
        clientHandler.iAnRemoteControlService.processMessage(sysDevice.getId(), msg.toString());
    }

    /**
     * 正常关闭连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
        Integer deviceId = NettyClient.ctxIdDeviceIdMap.get(ctx.channel().id()).getId();
        clientHandler.iAnRemoteControlService.colseControlConnection(deviceId);
        clientHandler.iAnDataTypeService.setSupplyAgainStatus(deviceId, false);
        NettyClient.deviceIdChannelMap.remove(deviceId);
        NettyClient.ctxIdDeviceIdMap.remove(ctx.channel().id());
    }

    /**
     * 异常关闭连接
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        Integer deviceId = NettyClient.ctxIdDeviceIdMap.get(ctx.channel().id()).getId();
        clientHandler.iAnRemoteControlService.colseControlConnection(deviceId);
        clientHandler.iAnDataTypeService.setSupplyAgainStatus(deviceId, false);
        NettyClient.deviceIdChannelMap.remove(deviceId);
        NettyClient.ctxIdDeviceIdMap.remove(ctx.channel().id());
    }


}
