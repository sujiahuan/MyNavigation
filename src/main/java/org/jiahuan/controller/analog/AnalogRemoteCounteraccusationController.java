package org.jiahuan.controller.analog;


import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.entity.analog.AnalogRemoteCounteraccusation;
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
    public RetMsgData<AnalogRemoteCounteraccusation> getCounCountercharge(@RequestParam Integer deviceId){
        RetMsgData<AnalogRemoteCounteraccusation> msgData = new RetMsgData<>();

        try {
            AnalogRemoteCounteraccusation analogRemoteCounteraccusation = iAnalogRemoteCounteraccusationService.getCounCounterchargeByDeviceId(deviceId);
            msgData.setData(analogRemoteCounteraccusation);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<AnalogRemoteCounteraccusation> updateCounCountercharge(@RequestBody AnalogRemoteCounteraccusation analogRemoteCounteraccusation){
        RetMsgData<AnalogRemoteCounteraccusation> msgData = new RetMsgData<>();

        try {
            iAnalogRemoteCounteraccusationService.updateCounCountercharge(analogRemoteCounteraccusation);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/openConnectionByDeviceId")
    public RetMsgData<AnalogRemoteCounteraccusation> openControlConnection(@RequestParam Integer deviceId){
        RetMsgData<AnalogRemoteCounteraccusation> msgData = new RetMsgData<>();

        try {
            iAnalogRemoteCounteraccusationService.openControlConnection(deviceId);
            return msgData;
        }catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/colseConnectionByDeviceId")
    public RetMsgData<AnalogRemoteCounteraccusation> colseControlConnection(@RequestParam Integer deviceId){
        RetMsgData<AnalogRemoteCounteraccusation> msgData = new RetMsgData<>();

        try {
            iAnalogRemoteCounteraccusationService.colseControlConnection(deviceId);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/openOrColseSocketConnection")
    public RetMsgData<AnalogRemoteCounteraccusation> openOrColseSocketConnection(@RequestParam Integer deviceId,boolean isOpen){
        RetMsgData<AnalogRemoteCounteraccusation> msgData = new RetMsgData<>();

        try {
            if(isOpen){
                if(!iAnalogRemoteCounteraccusationService.openSocketConnetion(deviceId)){
                    throw new Exception("连接失败，请检查能否连接");
                }
            }else {
                iAnalogRemoteCounteraccusationService.colseSocketConnetion(deviceId);
            }
            return msgData;
        }catch (Exception e) {
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
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }
}

