package org.jiahuan.controller.analog;


import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.service.analog.IAnalogRemoteCounteraccusationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.net.SocketException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jh
 * @since 2020-08-07
 */
@RestController
@RequestMapping("/counCountercharge")
public class AnalogRemoteCounteraccusationController {

    @Autowired
    private IAnalogRemoteCounteraccusationService iAnalogRemoteCounteraccusationService;

    @GetMapping("/getOneByDeviceId")
    public RetMsgData<org.jiahuan.entity.analog.AnalogRemoteCounteraccusation> getCounCountercharge(@RequestParam Integer deviceId){
        RetMsgData<org.jiahuan.entity.analog.AnalogRemoteCounteraccusation> msgData = new RetMsgData<>();

        try {
            org.jiahuan.entity.analog.AnalogRemoteCounteraccusation analogRemoteCounteraccusation = iAnalogRemoteCounteraccusationService.getCounCounterchargeByDeviceId(deviceId);
            msgData.setData(analogRemoteCounteraccusation);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<org.jiahuan.entity.analog.AnalogRemoteCounteraccusation> updateCounCountercharge(@RequestBody org.jiahuan.entity.analog.AnalogRemoteCounteraccusation analogRemoteCounteraccusation){
        RetMsgData<org.jiahuan.entity.analog.AnalogRemoteCounteraccusation> msgData = new RetMsgData<>();

        try {
            iAnalogRemoteCounteraccusationService.updateCounCountercharge(analogRemoteCounteraccusation);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/openConnectionByDeviceId")
    public RetMsgData<org.jiahuan.entity.analog.AnalogRemoteCounteraccusation> openControlConnection(@RequestParam Integer deviceId){
        RetMsgData<org.jiahuan.entity.analog.AnalogRemoteCounteraccusation> msgData = new RetMsgData<>();

        try {
            iAnalogRemoteCounteraccusationService.openControlConnection(deviceId);
            return msgData;
        }catch (ConnectException e) {
            if(e.getMessage().equals("Connection timed out: connect")||e.getMessage().equals("Connection timed out (Connection timed out)")){
                msgData.setMsg(" 连接服务器超时，请检查");
            }else if(e.getMessage().equals("Connection refused: connect")||e.getMessage().equals("Connection refused (Connection refused)")){
                msgData.setMsg(" 连接服务器被拒绝，请检查");
            }else{
                msgData.setMsg("没处理的异常："+e.getMessage());
            }
            return msgData;
        }catch (SocketException e) {
            if(e.getMessage().equals("Software caused connection abort: socket write error")){
                msgData.setMsg("连接已过时，请重发");
            }else if(e.getMessage().equals("Connection reset by peer: send")){
                msgData.setMsg("连接已过时，请重发");
            }else if(e.getMessage().equals("Software caused connection abort: send")){
                msgData.setMsg("连接已过时，请重发");
            }else{
                e.printStackTrace();
                msgData.setMsg("没处理的异常："+e.getMessage());
            }
            return msgData;
        } catch (Exception e) {
            e.printStackTrace();
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/colseConnectionByDeviceId")
    public RetMsgData<org.jiahuan.entity.analog.AnalogRemoteCounteraccusation> colseControlConnection(@RequestParam Integer deviceId){
        RetMsgData<org.jiahuan.entity.analog.AnalogRemoteCounteraccusation> msgData = new RetMsgData<>();

        try {
            iAnalogRemoteCounteraccusationService.colseControlConnection(deviceId);
            return msgData;
        } catch (Exception e) {
            e.printStackTrace();
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/openOrColseSocketConnection")
    public RetMsgData<org.jiahuan.entity.analog.AnalogRemoteCounteraccusation> openOrColseSocketConnection(@RequestParam Integer deviceId,boolean isOpen){
        RetMsgData<org.jiahuan.entity.analog.AnalogRemoteCounteraccusation> msgData = new RetMsgData<>();

        try {
            if(isOpen){
                iAnalogRemoteCounteraccusationService.openSocketConnetion(deviceId);
            }else {
                iAnalogRemoteCounteraccusationService.colseSocketConnetion(deviceId);
            }
            return msgData;
        }catch (ConnectException e) {
            if(e.getMessage().equals("Connection timed out: connect")||e.getMessage().equals("Connection timed out (Connection timed out)")){
                msgData.setMsg(" 连接服务器超时，请检查");
            }else if(e.getMessage().equals("Connection refused: connect")||e.getMessage().equals("Connection refused (Connection refused)")){
                msgData.setMsg(" 连接服务器被拒绝，请检查");
            }else{
                msgData.setMsg("没处理的异常："+e.getMessage());
            }
            return msgData;
        }catch (SocketException e) {
            if(e.getMessage().equals("Software caused connection abort: socket write error")){
                msgData.setMsg("连接已过时，请重发");
            }else if(e.getMessage().equals("Connection reset by peer: send")){
                msgData.setMsg("连接已过时，请重发");
            }else if(e.getMessage().equals("Software caused connection abort: send")){
                msgData.setMsg("连接已过时，请重发");
            }else{
                e.printStackTrace();
                msgData.setMsg("没处理的异常："+e.getMessage());
            }
            return msgData;
        } catch (Exception e) {
            e.printStackTrace();
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getSocketConnectionStatus")
    public RetMsgData<Boolean> getSocketConnectionStatus(@RequestParam Integer deviceId){
        RetMsgData<Boolean> msgData = new RetMsgData<>();

        try {
            boolean socketConnetionStatus = iAnalogRemoteCounteraccusationService.getSocketConnetionStatus(deviceId);
            msgData.setData(socketConnetionStatus);
            return msgData;
        } catch (Exception e) {
            e.printStackTrace();
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }
}

