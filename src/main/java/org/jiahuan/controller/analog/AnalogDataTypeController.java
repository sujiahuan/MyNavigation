package org.jiahuan.controller.analog;


import org.jiahuan.common.config.CustomWebSocketConfig;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.DataPackageUtils;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.analog.AnalogDataType;
import org.jiahuan.service.analog.IAnalogDataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wj
 * @since 2020-08-02
 */
@RestController
@RequestMapping("/counDataType")
public class AnalogDataTypeController {

    @Autowired
    private IAnalogDataTypeService iAnalogDataTypeService;

    @GetMapping("/sendRealTime")
    public RetMsgData<AnalogDataType> sendRealTime(@RequestParam Integer deviceId, @RequestParam Integer dataType) {
        RetMsgData<AnalogDataType> msgData = new RetMsgData<>();
        try {
            iAnalogDataTypeService.sendRealTime(deviceId, dataType);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/sendParam3020")
    public RetMsgData<AnalogDataType> sendParam3020(@RequestParam Integer deviceId, @RequestParam Integer dataType) {
        RetMsgData<AnalogDataType> msgData = new RetMsgData<>();
        try {
            iAnalogDataTypeService.sendParam3020(deviceId, dataType);
        } catch (Exception e) {

            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/getDataPackage")
    public RetMsgData<String> getDataPackage(@RequestParam Integer deviceId, @RequestParam Integer dataType) {
        RetMsgData<String> msgData = new RetMsgData<>();
        try {
            String dataPackage = iAnalogDataTypeService.getDataPackage(deviceId, dataType);
            msgData.setData(dataPackage);
        } catch (Exception e) {

            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/computeCrc")
    public RetMsgData<String> computeCrc(@RequestParam String msg) {
        RetMsgData<String> msgData = new RetMsgData<>();
        try {
            String dataPackage = DataPackageUtils.composeDataPackage(DataPackageUtils.positiveExpression(msg), false);
            if (VerdictUtil.isNull(dataPackage)) {
                msgData.setMsg("数据格式不对");
                return msgData;
            }
            msgData.setData(dataPackage);
        } catch (Exception e) {

            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/sendMessage")
    public RetMsgData<AnalogDataType> sendCustomizeMessage(@RequestParam Integer deviceId, @RequestParam String msg) {
        RetMsgData<AnalogDataType> msgData = new RetMsgData<>();
        try {
            iAnalogDataTypeService.sendCustomizeMessage(deviceId, msg);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/getSupplyAgainCount")
    public RetMsgData<Integer> getSupplyAgainCount(@RequestParam Integer deviceId, @RequestParam Integer dataType) {
        RetMsgData<Integer> msgData = new RetMsgData<>();
        try {
            Integer supplyAgainCount = iAnalogDataTypeService.getSupplyAgainCount(deviceId, dataType);
            msgData.setData(supplyAgainCount);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/getSupplyAgainStatus")
    public RetMsgData<Boolean> getSupplyAgainStatus(@RequestParam Integer deviceId) {
        RetMsgData<Boolean> msgData = new RetMsgData<>();
        msgData.setData(iAnalogDataTypeService.getSupplyAgainStatus(deviceId));
        return msgData;
    }

    @GetMapping("/waitForTheReissueToComplete")
    public RetMsgData<Boolean> waitForTheReissueToComplete(@RequestParam Integer deviceId) {
        RetMsgData<Boolean> msgData = new RetMsgData<>();
        try {
            iAnalogDataTypeService.waitForTheReissueToComplete(deviceId);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/sendSupplyAgain")
    public RetMsgData<AnalogDataType> sendSupplyAgain(@RequestParam Integer deviceId, @RequestParam Integer dataType) {
        RetMsgData<AnalogDataType> msgData = new RetMsgData<>();
        try {
            iAnalogDataTypeService.sendSupplyAgain(deviceId, dataType);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/cancelSupplyAgain")
    public RetMsgData<AnalogDataType> cancelSupplyAgain(@RequestParam Integer deviceId) {
        RetMsgData<AnalogDataType> msgData = new RetMsgData<>();
            iAnalogDataTypeService.setSupplyAgainStatus(deviceId,false);
            return msgData;
    }

    @GetMapping("/getListByDeviceId")
    public RetMsgData<List<AnalogDataType>> getListCounDataTypeByDeviceId(@RequestParam Integer deviceId) {
        RetMsgData<List<AnalogDataType>> msgData = new RetMsgData<>();
        try {
            List<AnalogDataType> analogDataTypes = iAnalogDataTypeService.getListCounDataTypeByDeviceId(deviceId);
            msgData.setData(analogDataTypes);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<AnalogDataType> updateCounDataType(@RequestBody AnalogDataType analogDataType) {
        RetMsgData<AnalogDataType> msgData = new RetMsgData<>();
        try {
            iAnalogDataTypeService.updateById(analogDataType);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

}

