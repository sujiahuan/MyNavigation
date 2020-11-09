package org.jiahuan.service.coun.impl;

import org.jiahuan.entity.coun.CounDevice;
import org.jiahuan.service.coun.IConnectionObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ConnectionObjImpl implements IConnectionObj {

    private Map<Integer, Socket> socketPool = new HashMap<>();
    private Map<Integer, BufferedReader> buffReaderPoll = new HashMap<>();
    private Map<Integer, OutputStream> outputPoll = new HashMap<>();
    private static Set<Integer> connetionStatusPoll = new HashSet<>();

    /**
     * 获取socket
     *
     * @param counDevice
     * @return
     * @throws IOException
     */
    public Socket getSocket(CounDevice counDevice) throws IOException {
        startConnetion(counDevice);
        return socketPool.get(counDevice.getId());
    }

    /**
     * 获取输入流
     *
     * @param counDevice
     * @return
     * @throws IOException
     */
    public BufferedReader getBuffReader(CounDevice counDevice) throws IOException {
        startConnetion(counDevice);
        return buffReaderPoll.get(counDevice.getId());
    }

    /**
     * 获取输出流
     *
     * @param counDevice
     * @return
     * @throws IOException
     */
    public OutputStream getOutputStream(CounDevice counDevice) throws IOException {
        startConnetion(counDevice);
        return outputPoll.get(counDevice.getId());
    }

    public Set<Integer> getConnetionStatusPoll(){
        return connetionStatusPoll;
    }

    /**
     * 进行连接
     *
     * @param counDevice 设备对象
     * @throws IOException
     */
    private void startConnetion(CounDevice counDevice) throws IOException {
        if (!socketPool.containsKey(counDevice.getId())) {
            Socket socket = new Socket(counDevice.getIp(), counDevice.getPort());
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            socketPool.put(counDevice.getId(), socket);
            buffReaderPoll.put(counDevice.getId(), bufferedReader);
            outputPoll.put(counDevice.getId(), outputStream);
            return;
        }
        Socket socket = socketPool.get(counDevice.getId());
        try {
            socket.sendUrgentData(0xFF);
        } catch (Exception e) {
            cleanConnetion(counDevice.getId(),true);
            throw e;
        }
    }

    /**
     * 关闭连接
     *
     * @param deviceId 设备对象
     * @param isAll 是否清除所有，否只关闭远程反控
     * @throws IOException
     */
    public void cleanConnetion(Integer deviceId, boolean isAll) throws IOException {
        if (connetionStatusPoll.contains(deviceId)) {
            connetionStatusPoll.remove(deviceId);
        }

        if (!isAll) {
            return;
        }

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
    }


}

