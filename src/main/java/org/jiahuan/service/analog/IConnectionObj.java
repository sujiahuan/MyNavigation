package org.jiahuan.service.analog;

import org.jiahuan.entity.analog.AnalogRemoteCounteraccusation;
import org.jiahuan.entity.sys.SysDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;

public interface IConnectionObj {

    boolean isSocketConnetion(Integer deviceId);

    boolean isControlConnetion(Integer deviceId);

    void openConnetion(SysDevice sysDevice) throws IOException;

    Socket getSocket(Integer deviceId) throws Exception;

    BufferedReader getBuffReader(Integer deviceId) throws Exception;

    OutputStream getOutputStream(Integer deviceId) throws Exception;

    AnalogRemoteCounteraccusation getControlConnetion(Integer deviceId);

    Map<Integer,Socket>  getSocketConnetionPoll();

    Map<Integer, LocalDateTime> getSocketCommunicationTimePoll();

    Map<Integer, AnalogRemoteCounteraccusation> getControlConnetionPoll();

    void setControlConnetionPoll(AnalogRemoteCounteraccusation counteraccusation);

    void cleanConnetion(Integer deviceId, boolean isAll);
}
