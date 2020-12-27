package org.jiahuan.service.analog;

import org.jiahuan.entity.sys.SysDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public interface IConnectionObj {

    boolean isConnetion(Integer deviceId);

    void openConnetion(SysDevice sysDevice) throws IOException;

    Socket getSocket(Integer deviceId) throws Exception;

    BufferedReader getBuffReader(Integer deviceId) throws Exception;

    OutputStream getOutputStream(Integer deviceId) throws Exception;

    Map<Integer,Socket>  getSocketConnetionPoll();

    Map<Integer, LocalDateTime> getSocketCommunicationTimePoll();

    Set<Integer> getControlConnetionPoll();

    void cleanConnetion(Integer deviceId, boolean isAll);
}
