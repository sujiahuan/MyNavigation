package org.jiahuan.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.util.TimeUtil;
import org.jiahuan.entity.coun.CounCountercharge;
import org.jiahuan.entity.coun.CounDataType;
import org.jiahuan.entity.coun.CounDevice;
import org.jiahuan.service.coun.IConnectionObj;
import org.jiahuan.service.coun.ICounCounterchargeService;
import org.jiahuan.service.coun.ICounDataTypeService;
import org.jiahuan.service.coun.ICounDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class TransmitterData {

    @Autowired
    private ICounDataTypeService iCounDataTypeService;
    @Autowired
    private ICounDeviceService iCounDeviceService;
    @Autowired
    private IConnectionObj iConnectionObj;

    @Scheduled(cron="0,30 * * * * ?")
    public void send2011Data() {
        System.out.println("实时数据运行啦："+ TimeUtil.getFormatCurrentTime("millisecond",0));
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing",1);
        queryWrapper.eq("data_type",1);
        List<CounDataType> listCountDataType = iCounDataTypeService.list(queryWrapper);
        for (CounDataType counDataType : listCountDataType) {
            CounDevice counDevice = iCounDeviceService.getById(counDataType.getDeviceId());
            try {
                iCounDataTypeService.sendRealTime(counDataType.getDeviceId(),counDevice.getAgreement(),1);
            }catch (ConnectException e) {
                if(e.getMessage().equals("Connection timed out: connect")||e.getMessage().equals("Connection timed out (Connection timed out)")){
                    log.warn(" 连接服务器超时，请检查");
                }else if(e.getMessage().equals("Connection refused: connect")||e.getMessage().equals("Connection refused (Connection refused)")){
                    log.warn(" 连接服务器被拒绝，请检查");
                }else{
                    log.error("没处理的异常："+e.getMessage());
                }
            }catch (SocketException e) {
                if(e.getMessage().equals("Software caused connection abort: socket write error")){
                    log.warn("连接已过时，请重发");
                }else if(e.getMessage().equals("Connection reset by peer: send")){
                    log.warn("连接已过时，请重发");
                }else if(e.getMessage().equals("Software caused connection abort: send")){
                    log.warn("连接已过时，请重发");
                }else{
                    log.error("没处理的异常："+e.getMessage());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Scheduled(cron="0 0/10 * * * ?")
    public void send2051Data() {
        System.out.println("分钟数据运行啦："+ TimeUtil.getFormatCurrentTime("millisecond",0));
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing",1);
        queryWrapper.eq("data_type",2);
        List<CounDataType> listCountDataType = iCounDataTypeService.list(queryWrapper);
        for (CounDataType counDataType : listCountDataType) {
            CounDevice counDevice = iCounDeviceService.getById(counDataType.getDeviceId());
            try {
                iCounDataTypeService.sendRealTime(counDataType.getDeviceId(),counDevice.getAgreement(),2);
            }catch (ConnectException e) {
                if(e.getMessage().equals("Connection timed out: connect")||e.getMessage().equals("Connection timed out (Connection timed out)")){
                    log.warn(" 连接服务器超时，请检查");
                }else if(e.getMessage().equals("Connection refused: connect")||e.getMessage().equals("Connection refused (Connection refused)")){
                    log.warn(" 连接服务器被拒绝，请检查");
                }else{
                    log.error("没处理的异常："+e.getMessage());
                }
            }catch (SocketException e) {
                if(e.getMessage().equals("Software caused connection abort: socket write error")){
                    log.warn("连接已过时，请重发");
                }else if(e.getMessage().equals("Connection reset by peer: send")){
                    log.warn("连接已过时，请重发");
                }else if(e.getMessage().equals("Software caused connection abort: send")){
                    log.warn("连接已过时，请重发");
                }else{
                    log.error("没处理的异常："+e.getMessage());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Scheduled(cron="0 0 0/1 * * ?")
    public void send2061Data(){
        System.out.println("小时数据运行啦："+ TimeUtil.getFormatCurrentTime("millisecond",0));
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing",1);
        queryWrapper.eq("data_type",3);
        List<CounDataType> listCountDataType = iCounDataTypeService.list(queryWrapper);
        for (CounDataType counDataType : listCountDataType) {
            CounDevice counDevice = iCounDeviceService.getById(counDataType.getDeviceId());
            try {
                iCounDataTypeService.sendRealTime(counDataType.getDeviceId(),counDevice.getAgreement(),3);
            }catch (ConnectException e) {
                if(e.getMessage().equals("Connection timed out: connect")||e.getMessage().equals("Connection timed out (Connection timed out)")){
                    log.warn(" 连接服务器超时，请检查");
                }else if(e.getMessage().equals("Connection refused: connect")||e.getMessage().equals("Connection refused (Connection refused)")){
                    log.warn(" 连接服务器被拒绝，请检查");
                }else{
                    log.error("没处理的异常："+e.getMessage());
                }
            }catch (SocketException e) {
                if(e.getMessage().equals("Software caused connection abort: socket write error")){
                    log.warn("连接已过时，请重发");
                }else if(e.getMessage().equals("Connection reset by peer: send")){
                    log.warn("连接已过时，请重发");
                }else if(e.getMessage().equals("Software caused connection abort: send")){
                    log.warn("连接已过时，请重发");
                }else{
                    log.error("没处理的异常："+e.getMessage());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Scheduled(cron="0 0 0 * * ? ")
    public void send2031Data(){
        System.out.println("日数据运行啦："+ TimeUtil.getFormatCurrentTime("millisecond",0));
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing",1);
        queryWrapper.eq("data_type",4);
        List<CounDataType> listCountDataType = iCounDataTypeService.list(queryWrapper);
        for (CounDataType counDataType : listCountDataType) {
            CounDevice counDevice = iCounDeviceService.getById(counDataType.getDeviceId());
            try {
                iCounDataTypeService.sendRealTime(counDataType.getDeviceId(),counDevice.getAgreement(),4);
            }catch (ConnectException e) {
                if(e.getMessage().equals("Connection timed out: connect")||e.getMessage().equals("Connection timed out (Connection timed out)")){
                    log.warn(" 连接服务器超时，请检查");
                }else if(e.getMessage().equals("Connection refused: connect")||e.getMessage().equals("Connection refused (Connection refused)")){
                    log.warn(" 连接服务器被拒绝，请检查");
                }else{
                    log.error("没处理的异常："+e.getMessage());
                }
            }catch (SocketException e) {
                if(e.getMessage().equals("Software caused connection abort: socket write error")){
                    log.warn("连接已过时，请重发");
                }else if(e.getMessage().equals("Connection reset by peer: send")){
                    log.warn("连接已过时，请重发");
                }else if(e.getMessage().equals("Software caused connection abort: send")){
                    log.warn("连接已过时，请重发");
                }else{
                    log.error("没处理的异常："+e.getMessage());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 定时检查socket连接池里面的socket是否还活着
     */
    @Scheduled(cron="0 0/10 * * * ?")
    public void checkSocketConnection(){
        Map<Integer, Socket> socketPool = iConnectionObj.getSocketPool();
        Set<Integer> integers = socketPool.keySet();
        Iterator<Integer> iterator = integers.iterator();
        while(iterator.hasNext()){
            Integer deviceId = iterator.next();
            try {
                socketPool.get(deviceId).sendUrgentData(0xFF);
            } catch (Exception e) {
                iConnectionObj.cleanConnetion(deviceId,true);
                log.warn("{}设备连接已过期，原因是：{}",deviceId,e.getMessage());
            }
        }
    }

}
