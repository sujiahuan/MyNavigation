package org.jiahuan.controller.coun;


import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.model.State;
import org.jiahuan.entity.coun.CounCountercharge;
import org.jiahuan.service.coun.ICounCounterchargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.ConnectException;

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
public class CounCounterchargeController {

    @Autowired
    private ICounCounterchargeService iCounCounterchargeService;

    @GetMapping("/getOneByDeviceId")
    public RetMsgData<CounCountercharge> getCounCountercharge(@RequestParam Integer deviceId){
        RetMsgData<CounCountercharge> msgData = new RetMsgData<>();

        try {
            CounCountercharge counCountercharge = iCounCounterchargeService.getCounCounterchargeByDeviceId(deviceId);
            msgData.setData(counCountercharge);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<CounCountercharge> updateCounCountercharge(@RequestBody CounCountercharge counCountercharge){
        RetMsgData<CounCountercharge> msgData = new RetMsgData<>();

        try {
            iCounCounterchargeService.updateById(counCountercharge);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/openConnectionByDeviceId")
    public RetMsgData<CounCountercharge> openConnection(@RequestParam Integer deviceId){
        RetMsgData<CounCountercharge> msgData = new RetMsgData<>();

        try {
            iCounCounterchargeService.openConnection(deviceId);
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
        } catch (Exception e) {
            e.printStackTrace();
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/colseConnectionByDeviceId")
    public RetMsgData<CounCountercharge> colseConnection(@RequestParam Integer deviceId){
        RetMsgData<CounCountercharge> msgData = new RetMsgData<>();

        try {
            iCounCounterchargeService.closeConnection(deviceId);
            return msgData;
        } catch (IOException e) {
            e.printStackTrace();
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }
}

