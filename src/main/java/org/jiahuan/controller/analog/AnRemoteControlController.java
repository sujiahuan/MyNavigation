package org.jiahuan.controller.analog;


import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.entity.analog.AnRemoteControl;
import org.jiahuan.service.analog.IAnRemoteControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class AnRemoteControlController {

    @Autowired
    private IAnRemoteControlService iAnRemoteControlService;

    @GetMapping("/getOneByDeviceId")
    public RetMsgData<AnRemoteControl> getCounCountercharge(@RequestParam Integer deviceId){
        RetMsgData<AnRemoteControl> msgData = new RetMsgData<>();

        try {
            AnRemoteControl anRemoteControl = iAnRemoteControlService.getCounCounterchargeByDeviceId(deviceId);
            msgData.setData(anRemoteControl);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<AnRemoteControl> updateCounCountercharge(@RequestBody AnRemoteControl anRemoteControl){
        RetMsgData<AnRemoteControl> msgData = new RetMsgData<>();

        try {
            iAnRemoteControlService.updateCounCountercharge(anRemoteControl);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/openConnectionByDeviceId")
    public RetMsgData<AnRemoteControl> openControlConnection(@RequestParam Integer deviceId){
        RetMsgData<AnRemoteControl> msgData = new RetMsgData<>();

        try {
            iAnRemoteControlService.openControlConnection(deviceId);
            return msgData;
        }catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/colseConnectionByDeviceId")
    public RetMsgData<AnRemoteControl> colseControlConnection(@RequestParam Integer deviceId){
        RetMsgData<AnRemoteControl> msgData = new RetMsgData<>();

        try {
            iAnRemoteControlService.colseControlConnection(deviceId);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/openOrColseSocketConnection")
    public RetMsgData<AnRemoteControl> openOrColseSocketConnection(@RequestParam Integer deviceId, boolean isOpen){
        RetMsgData<AnRemoteControl> msgData = new RetMsgData<>();

        try {
            if(isOpen){
                if(!iAnRemoteControlService.openSocketConnetion(deviceId)){
                    throw new Exception("连接失败，请检查能否连接");
                }
            }else {
                iAnRemoteControlService.colseSocketConnetion(deviceId);
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
            boolean socketConnetionStatus = iAnRemoteControlService.getSocketConnetionStatus(deviceId);
            msgData.setData(socketConnetionStatus);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }
}

