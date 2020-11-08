package org.jiahuan.service.coun;

import org.jiahuan.entity.coun.CounDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;

public interface IConnectionObj {

    Socket getSocket(CounDevice counDevice) throws IOException;

    BufferedReader getBuffReader(CounDevice counDevice) throws IOException;

    OutputStream getOutputStream(CounDevice counDevice) throws IOException;

    Set<Integer> getConnetionStatusPoll();

    void cleanConnetion(Integer deviceId, boolean isAll) throws IOException;
}
