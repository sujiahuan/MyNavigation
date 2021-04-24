package org.jiahuan.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.jiahuan.entity.analog.AnalogRemoteCounteraccusation;
import org.jiahuan.entity.sys.SysDevice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Client {

    /**
     * 设备id与通道绑定map
     */
    public static Map<Integer, Channel> deviceIdChannelMap = new HashMap<>();
    /**
     * 通道id与设备id绑定map
     */
    public static Map<ChannelId,SysDevice> ctxIdDeviceIdMap = new HashMap<>();
    /**
     * 开启反控map
     */
    private Map<Integer, AnalogRemoteCounteraccusation> controlConnetionMap = new HashMap<>();
    /**
     * netty连接对象
     */
    private Bootstrap b = new Bootstrap();

    /**
     * 初始化
     */
    public void init() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new ClientHandler());
                        }
                    });
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * 建立连接
     * @param device 设备对象
     * @throws InterruptedException
     */
    public boolean connection(SysDevice device) {
        Channel channel = b.connect(device.getIp(),device.getPort()).channel();
        if(channel.isActive()){
            deviceIdChannelMap.put(device.getId(),channel);
            ctxIdDeviceIdMap.put(channel.id(),device);
            return true;
        }
        return false;
    }

    /**
     * 发送消息
     * @param deviceId 设备id
     * @param message 需发送的消息
     * @return 成功true，否则false
     */
    public boolean sendMessage(Integer deviceId,String message){
        if(deviceIdChannelMap.containsKey(deviceId)){
            deviceIdChannelMap.get(deviceId).writeAndFlush(message);
            return true;
        }
        return false;
    }

    /**
     * 判断连接状态
     * @param deviceId 设备id
     * @param type 1 netty状态/2 反控状态
     * @return true 连接 / false 断开
     */
    public boolean isConnection(Integer deviceId,Integer type){
        switch (type){
            case 1:
                return deviceIdChannelMap.containsKey(deviceId);
            case 2:
                return controlConnetionMap.containsKey(deviceId);
        }
        return false;
    }

}
