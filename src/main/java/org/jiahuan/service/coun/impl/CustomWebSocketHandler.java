package org.jiahuan.service.coun.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Service
public class CustomWebSocketHandler extends TextWebSocketHandler implements WebSocketHandler {
    private Logger logger = LoggerFactory.getLogger(CustomWebSocketHandler.class);
    // 在线用户列表
    private static final Map<String, Set<WebSocketSession>> users;
    // 用户标识
    private static final String CLIENT_ID = "mchNo";

    static {
        users = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("已与后台建立socket连接");
        String mchNo = getMchNo(session);
        if (StringUtils.isNotEmpty(mchNo)) {
            if (!users.containsKey(mchNo)) {
                HashSet<WebSocketSession> webSocketSessions = new HashSet<>();
                webSocketSessions.add(session);
                users.put(mchNo, webSocketSessions);
            } else {
                Set<WebSocketSession> webSocketSessions = users.get(mchNo);
                webSocketSessions.add(session);
            }

//            session.sendMessage(new TextMessage("成功建立websocket-spring连接\r\n"));
            logger.info("用户标识：{}，Session：{}", mchNo, session.toString());
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("收到客户端消息：{}", message.getPayload());
        String to = getMchNo(session);
        String msg = message.getPayload();
        WebSocketMessage<?> webSocketMessageServer = new TextMessage("服务器收到客户端发的消息内容:" +message.getPayload());
        try {
            session.sendMessage(webSocketMessageServer);
        } catch (IOException e) {
            logger.info("handleTextMessage method error：{}", e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        logger.info("连接出错");
        Set<WebSocketSession> webSocketSessions = users.get(getMchNo(session));
        webSocketSessions.remove(session);
        if (webSocketSessions.size() == 0) {
            users.remove(getMchNo(session));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("连接已关闭：" + status);
        Set<WebSocketSession> webSocketSessions = users.get(getMchNo(session));
        webSocketSessions.remove(session);
        if (webSocketSessions.size() == 0) {
            users.remove(getMchNo(session));
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessage(String jsonData) {
        logger.info("收到客户端消息sendMessage：{}", jsonData);
        //{mchNo:3,to:"all",msg:"内容"}
        JSONObject msgJson = JSONObject.parseObject(jsonData);
        String mchNo = StringUtils.isEmpty(msgJson.getString(CLIENT_ID)) ? "陌生人" : msgJson.getString(CLIENT_ID);
        String to = msgJson.getString("to");
        String msg = msgJson.getString("msg");
        if ("all".equals(to.toLowerCase())) {
            sendMessageToAllUsers(new TextMessage(mchNo + ":" + msg));
        } else {
            sendMessageToUser(to, new TextMessage(mchNo + ":" + msg));
        }
    }

    /**
     * 发送信息给指定用户
     *
     * @param mchNo 设备id
     * @param message
     * @return
     * @Title: sendMessageToUser
     * @Description: TODO
     * @Date 2018年8月21日 上午11:01:08
     * @author OnlyMate
     */
    public boolean sendMessageToUser(String mchNo, TextMessage message) {
        if (users.get(mchNo) == null)
            return false;
        Set<WebSocketSession> webSocketSessions = users.get(mchNo);
        Iterator<WebSocketSession> webSocketSessionIterator = webSocketSessions.iterator();
        try {
            while (webSocketSessionIterator.hasNext()) {
                WebSocketSession webSocketSession = webSocketSessionIterator.next();
                if (!webSocketSession.isOpen()) {
                    logger.warn("客户端:{},已断开连接，发送消息失败", mchNo);
                    webSocketSessionIterator.remove();
                    continue;
                }
                webSocketSession.sendMessage(message);
            }
        } catch (IOException e) {
            logger.info("sendMessageToUser method error：{}", e);
            return false;
        }
        return true;
    }

    /**
     * 广播信息
     *
     * @param message
     * @return
     * @Title: sendMessageToAllUsers
     * @Description: TODO
     * @Date 2018年8月21日 上午11:01:14
     * @author OnlyMate
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<String> mchNos = users.keySet();
        for (String mchNo : mchNos) {
            try {
                Set<WebSocketSession> webSocketSessions = users.get(mchNo);
                Iterator<WebSocketSession> iterator = webSocketSessions.iterator();
                while (iterator.hasNext()) {
                    WebSocketSession webSocketSession = iterator.next();
                    if (!webSocketSession.isOpen()) {
                        logger.info("客户端:{},已断开连接，发送消息失败", mchNo);
                        iterator.remove();
                        continue;
                    }
                    webSocketSession.sendMessage(message);
                }
            } catch (IOException e) {
                logger.info("sendMessageToAllUsers method error：{}", e);
                allSendSuccess = false;
            }
        }
        return allSendSuccess;
    }

    /**
     * 获取用户标识
     *
     * @param session
     * @return
     * @Title: getMchNo
     * @Description: TODO
     * @Date 2018年8月21日 上午11:01:01
     * @author OnlyMate
     */
    private String getMchNo(WebSocketSession session) {
        try {
            String mchNo = session.getAttributes().get(CLIENT_ID).toString();
            return mchNo;
        } catch (Exception e) {
            return null;
        }
    }
}
