package org.jiahuan.controller.coun;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.model.State;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.coun.CounDataType;
import org.jiahuan.service.coun.ICounDataTypeService;
import org.jiahuan.service.coun.ICounDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wj
 * @since 2020-08-02
 */
@RestController
@RequestMapping("/counDataType")
public class CounDataTypeController {

    @Autowired
    private ICounDataTypeService iCounDataTypeService;
    @Autowired
    private ICounDeviceService iCounDeviceService;

    @GetMapping("/sendRealTime")
    public RetMsgData<CounDataType> sendRealTime(@RequestParam Integer deviceId,@RequestParam Integer dataType){
        RetMsgData<CounDataType> msgData = new RetMsgData<>();
        try {
            String agreement = iCounDeviceService.getById(deviceId).getAgreement();
            iCounDataTypeService.sendRealTime(deviceId,agreement,dataType);
            return msgData;
        } catch (Exception e) {
            e.printStackTrace();
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            return msgData;
        }
    }

    @GetMapping("/sendSupplyAgain")
    public RetMsgData<CounDataType> sendSupplyAgain(@RequestParam Integer deviceId,@RequestParam Integer dataType){
        RetMsgData<CounDataType> msgData = new RetMsgData<>();
        try {
            String agreement = iCounDeviceService.getById(deviceId).getAgreement();
            iCounDataTypeService.sendSupplyAgain(deviceId,agreement,dataType);
            return msgData;
        } catch (Exception e) {
            e.printStackTrace();
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            return msgData;
        }
    }

    @GetMapping("/getListByDeviceId")
    public RetMsgData<List<CounDataType>> getListCounDataTypeByDeviceId(@RequestParam Integer deviceId){
        RetMsgData<List<CounDataType>> msgData = new RetMsgData<>();
        try {
            List<CounDataType> counDataTypes = iCounDataTypeService.getListCounDataTypeByDeviceId(deviceId);
            msgData.setData(counDataTypes);
            return msgData;
        } catch (Exception e) {
            e.printStackTrace();
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            return msgData;
        }
    }

    @PostMapping("/update")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    public RetMsgData<CounDataType> updateCounDataType(@RequestBody CounDataType counDataType){
        RetMsgData<CounDataType> msgData = new RetMsgData<>();
        try {
            iCounDataTypeService.updateById(counDataType);
            return msgData;
        } catch (Exception e) {
            e.printStackTrace();
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            return msgData;
        }
    }

}

