package org.jiahuan.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.util.TimeUtil;
import org.jiahuan.entity.analog.AnalogDataType;
import org.jiahuan.service.analog.IConnectionObj;
import org.jiahuan.service.analog.IAnalogDataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class TransmitterData {

    @Autowired
    private IAnalogDataTypeService iAnalogDataTypeService;
    @Autowired
    private IConnectionObj iConnectionObj;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Scheduled(cron = "0,30 * * * * ?")
    public void send2011Data() {
        log.info("实时数据运行啦：" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0));
        QueryWrapper<AnalogDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing", 1);
        queryWrapper.eq("data_type", 1);
        List<AnalogDataType> listCountDataType = iAnalogDataTypeService.list(queryWrapper);
        for (AnalogDataType analogDataType : listCountDataType) {
            executorService.submit(() -> {
                try {
                    iAnalogDataTypeService.sendRealTime(analogDataType.getDeviceId(), 1);
                } catch (ConnectException e) {
                    if (e.getMessage().equals("Connection timed out: connect") || e.getMessage().equals("Connection timed out (Connection timed out)")) {
                        log.warn(" 连接服务器超时，请检查");
                    } else if (e.getMessage().equals("Connection refused: connect") || e.getMessage().equals("Connection refused (Connection refused)")) {
                        log.warn(" 连接服务器被拒绝，请检查");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (SocketException e) {
                    if (e.getMessage().equals("Software caused connection abort: socket write error")) {
                        log.warn("连接已过时，请重发");
                    } else if (e.getMessage().equals("Connection reset by peer: send")) {
                        log.warn("连接已过时，请重发");
                    } else if (e.getMessage().equals("Software caused connection abort: send")) {
                        log.warn("连接已过时，请重发");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (SocketTimeoutException e) {
                    if (e.getMessage().equals("connect timed out")) {
                        log.warn("连接已超时，请检查该服务器是否能连上");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                }catch (Exception e) {
                    log.error(e.getMessage());
                }
            });
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void send2051Data() {
        log.info("分钟数据运行啦：" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0));
        QueryWrapper<AnalogDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing", 1);
        queryWrapper.eq("data_type", 2);
        List<AnalogDataType> listCountDataType = iAnalogDataTypeService.list(queryWrapper);
        for (AnalogDataType analogDataType : listCountDataType) {
            executorService.submit(() -> {
                try {
                    iAnalogDataTypeService.sendRealTime(analogDataType.getDeviceId(), 2);
                } catch (ConnectException e) {
                    if (e.getMessage().equals("Connection timed out: connect") || e.getMessage().equals("Connection timed out (Connection timed out)")) {
                        log.warn(" 连接服务器超时，请检查");
                    } else if (e.getMessage().equals("Connection refused: connect") || e.getMessage().equals("Connection refused (Connection refused)")) {
                        log.warn(" 连接服务器被拒绝，请检查");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (SocketException e) {
                    if (e.getMessage().equals("Software caused connection abort: socket write error")) {
                        log.warn("连接已过时，请重发");
                    } else if (e.getMessage().equals("Connection reset by peer: send")) {
                        log.warn("连接已过时，请重发");
                    } else if (e.getMessage().equals("Software caused connection abort: send")) {
                        log.warn("连接已过时，请重发");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (SocketTimeoutException e) {
                    if (e.getMessage().equals("connect timed out")) {
                        log.warn("连接已超时，请检查该服务器是否能连上");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            });
        }
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void send2061Data() {
        log.info("小时数据运行啦：" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0));
        QueryWrapper<AnalogDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing", 1);
        queryWrapper.eq("data_type", 3);
        List<AnalogDataType> listCountDataType = iAnalogDataTypeService.list(queryWrapper);
        for (AnalogDataType analogDataType : listCountDataType) {
            executorService.submit(() -> {
                try {
                    iAnalogDataTypeService.sendRealTime(analogDataType.getDeviceId(), 3);
                } catch (ConnectException e) {
                    if (e.getMessage().equals("Connection timed out: connect") || e.getMessage().equals("Connection timed out (Connection timed out)")) {
                        log.warn(" 连接服务器超时，请检查");
                    } else if (e.getMessage().equals("Connection refused: connect") || e.getMessage().equals("Connection refused (Connection refused)")) {
                        log.warn(" 连接服务器被拒绝，请检查");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (SocketException e) {
                    if (e.getMessage().equals("Software caused connection abort: socket write error")) {
                        log.warn("连接已过时，请重发");
                    } else if (e.getMessage().equals("Connection reset by peer: send")) {
                        log.warn("连接已过时，请重发");
                    } else if (e.getMessage().equals("Software caused connection abort: send")) {
                        log.warn("连接已过时，请重发");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (SocketTimeoutException e) {
                    if (e.getMessage().equals("connect timed out")) {
                        log.warn("连接已超时，请检查该服务器是否能连上");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            });
        }
    }

    @Scheduled(cron = "0 0 0 * * ? ")
    public void send2031Data() {
        log.info("日数据运行啦：" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0));
        QueryWrapper<AnalogDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing", 1);
        queryWrapper.eq("data_type", 4);
        List<AnalogDataType> listCountDataType = iAnalogDataTypeService.list(queryWrapper);
        for (AnalogDataType analogDataType : listCountDataType) {
            executorService.submit(() -> {
                try {
                    iAnalogDataTypeService.sendRealTime(analogDataType.getDeviceId(), 4);
                } catch (ConnectException e) {
                    if (e.getMessage().equals("Connection timed out: connect") || e.getMessage().equals("Connection timed out (Connection timed out)")) {
                        log.warn(" 连接服务器超时，请检查");
                    } else if (e.getMessage().equals("Connection refused: connect") || e.getMessage().equals("Connection refused (Connection refused)")) {
                        log.warn(" 连接服务器被拒绝，请检查");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (SocketException e) {
                    if (e.getMessage().equals("Software caused connection abort: socket write error")) {
                        log.warn("连接已过时，请重发");
                    } else if (e.getMessage().equals("Connection reset by peer: send")) {
                        log.warn("连接已过时，请重发");
                    } else if (e.getMessage().equals("Software caused connection abort: send")) {
                        log.warn("连接已过时，请重发");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (SocketTimeoutException e) {
                    if (e.getMessage().equals("connect timed out")) {
                        log.warn("连接已超时，请检查该服务器是否能连上");
                    } else {
                        log.error("没处理的异常：" + e.getMessage());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            });
        }
    }

    /**
     * 定时检查socket连接池里面的socket是否还活着
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void checkSocketConnection() {
        Map<Integer, Socket> socketPool = iConnectionObj.getSocketConnetionPoll();
        Set<Integer> integers = socketPool.keySet();
        Iterator<Integer> iterator = integers.iterator();
        LocalDateTime nowTime = LocalDateTime.now();
        Map<Integer, LocalDateTime> socketCommunicationTimePoll = iConnectionObj.getSocketCommunicationTimePoll();
        while (iterator.hasNext()) {
            Integer deviceId = iterator.next();
            if(nowTime.minusMinutes(9).compareTo(socketCommunicationTimePoll.get(deviceId))!=1){
                continue;
            }
            try {
                socketPool.get(deviceId).sendUrgentData(0xFF);
                socketCommunicationTimePoll.put(deviceId,nowTime);
            } catch (Exception e) {
                iConnectionObj.cleanConnetion(deviceId, true);
                log.warn("{}设备连接已过期，原因是：{}", deviceId, e.getMessage());
            }
        }
    }

}
