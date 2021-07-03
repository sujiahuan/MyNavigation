package org.jiahuan.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.jiahuan.entity.sys.SysDevice;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NettyClient {

    /**
     * 设备id与通道绑定map
     */
    public static Map<Integer, Channel> deviceIdChannelMap = new HashMap<>();
    /**
     * 通道id与设备id绑定map
     */
    public static Map<ChannelId, SysDevice> ctxIdDeviceIdMap = new HashMap<>();
    /**
     * netty连接对象
     */
    private Bootstrap b = new Bootstrap();

    /**
     * 初始化
     */
    public void init() {
        EventLoopGroup group = new NioEventLoopGroup();
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
    }

    /**
     * 建立连接
     *
     * @param device 设备对象
     * @throws InterruptedException
     */
    public boolean connection(SysDevice device) {
        if(deviceIdChannelMap.containsKey(device.getId())){
            this.closeConnection(device.getId());
        }
        Channel channel = b.connect(device.getIp(), device.getPort()).channel();
        synchronized (channel){
            try {
                channel.wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (channel.isActive()) {
            deviceIdChannelMap.put(device.getId(), channel);
            ctxIdDeviceIdMap.put(channel.id(),device);
            return true;
        }
        return false;
    }

    /**
     * 判断连接状态
     *
     * @param deviceId 设备id
     * @return true 连接 / false 断开
     */
    public boolean isConnection(Integer deviceId) {
        return deviceIdChannelMap.containsKey(deviceId);
    }

    /**
     * 关闭连接
     * @param deviceId 设备id
     */
    public void closeConnection(Integer deviceId) {
        if (deviceIdChannelMap.containsKey(deviceId)) {
            Channel channel = deviceIdChannelMap.get(deviceId);
            channel.close();
            synchronized (channel){
                try {
                    channel.wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送消息
     *
     * @param deviceId 设备id
     * @param message  需发送的消息
     * @return 成功true，否则false
     */
    public boolean sendMessage(Integer deviceId, String message) {
        if (deviceIdChannelMap.containsKey(deviceId)) {
            deviceIdChannelMap.get(deviceId).writeAndFlush(message);
            return true;
        }
        return false;
    }


}
