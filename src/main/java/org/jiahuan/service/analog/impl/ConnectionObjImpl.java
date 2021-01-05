package org.jiahuan.service.analog.impl;

import lombok.extern.slf4j.Slf4j;
import org.jiahuan.entity.analog.AnalogRemoteCounteraccusation;
import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.service.analog.IConnectionObj;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ConnectionObjImpl implements IConnectionObj {

    private Map<Integer, Socket> socketPool = new HashMap<>();
    private Map<Integer, LocalDateTime> socketCommunicationTime = new HashMap<>();
    private Map<Integer, BufferedReader> buffReaderPoll = new HashMap<>();
    private Map<Integer, OutputStream> outputPoll = new HashMap<>();
    private Map<Integer, AnalogRemoteCounteraccusation> controlConnetionPoll = new HashMap<>();

    @Override
    public boolean isSocketConnetion(Integer deviceId) {
        if (!socketPool.containsKey(deviceId)) {
            return false;
        }
        try {
            Socket socket = socketPool.get(deviceId);
            socket.sendUrgentData(0xFF);
            socketCommunicationTime.put(deviceId, LocalDateTime.now());
            return true;
        } catch (Exception e) {
            cleanConnetion(deviceId, true);
            return false;
        }
    }

    @Override
    public boolean isControlConnetion(Integer deviceId) {
        return controlConnetionPoll.containsKey(deviceId);
    }

    @Override
    public void openConnetion(SysDevice sysDevice) throws IOException {
        try {
            if (!isSocketConnetion(sysDevice.getId())) {
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(sysDevice.getIp(), sysDevice.getPort());
                socket.connect(socketAddress, 2000);
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                socketPool.put(sysDevice.getId(), socket);
                socketCommunicationTime.put(sysDevice.getId(), LocalDateTime.now());
                buffReaderPoll.put(sysDevice.getId(), bufferedReader);
                outputPoll.put(sysDevice.getId(), outputStream);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取单个设备socket
     *
     * @param deviceId
     * @return
     * @throws IOException
     */
    public Socket getSocket(Integer deviceId) throws Exception {
        if(socketPool.containsKey(deviceId)){
            socketCommunicationTime.put(deviceId, LocalDateTime.now());
            return socketPool.get(deviceId);
        }
        throw new Exception("连接已断开，请连接");
    }

    /**
     * 获取输入流
     *
     * @param deviceId
     * @return
     * @throws IOException
     */
    public BufferedReader getBuffReader(Integer deviceId) throws Exception {
        if(buffReaderPoll.containsKey(deviceId)){
            socketCommunicationTime.put(deviceId, LocalDateTime.now());
        return buffReaderPoll.get(deviceId);
        }
        throw new Exception("连接已断开，请连接");
    }

    /**
     * 获取输出流
     *
     * @param deviceId
     * @return
     * @throws IOException
     */
    public OutputStream getOutputStream(Integer deviceId) throws Exception {
        if(outputPoll.containsKey(deviceId)){
            socketCommunicationTime.put(deviceId, LocalDateTime.now());
        return outputPoll.get(deviceId);
    }
        throw new Exception("连接已断开，请连接");
    }

    @Override
    public AnalogRemoteCounteraccusation getControlConnetion(Integer deviceId){
        if(controlConnetionPoll.containsKey(deviceId)){
            return controlConnetionPoll.get(deviceId);
        }
        return null;
    }


    @Override
    public Map<Integer, Socket> getSocketConnetionPoll() {
        return new HashMap<>(socketPool);
    }

    @Override
    public Map<Integer, LocalDateTime> getSocketCommunicationTimePoll() {
        return new HashMap<>(socketCommunicationTime);
    }

    public Map<Integer, AnalogRemoteCounteraccusation> getControlConnetionPoll() {
        return new HashMap<>(controlConnetionPoll);
    }

    @Override
    public void setControlConnetionPoll(AnalogRemoteCounteraccusation counteraccusation) {
        controlConnetionPoll.put(counteraccusation.getDeviceId(),counteraccusation);
    }


    /**
     * 关闭连接
     *
     * @param deviceId 设备对象
     * @param isAll    是否清除所有，否只关闭远程反控
     * @throws IOException
     */
    public void cleanConnetion(Integer deviceId, boolean isAll) {
        if (controlConnetionPoll.containsKey(deviceId)) {
            controlConnetionPoll.remove(deviceId);
        }

        if (!isAll) {
            return;
        }
        if (socketCommunicationTime.containsKey(deviceId)) {
            socketCommunicationTime.remove(deviceId);
        }
        try {
            if (buffReaderPoll.containsKey(deviceId)) {
                buffReaderPoll.get(deviceId).close();
                buffReaderPoll.remove(deviceId);
            }
            if (outputPoll.containsKey(deviceId)) {
                outputPoll.get(deviceId).close();
                outputPoll.remove(deviceId);
            }
            if (socketPool.containsKey(deviceId)) {
                socketPool.get(deviceId).close();
                socketPool.remove(deviceId);
            }
        } catch (IOException e) {
            log.error("关闭失败：{}", e.getMessage());
        }
    }


}

