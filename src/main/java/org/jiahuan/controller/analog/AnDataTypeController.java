package org.jiahuan.controller.analog;


import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.DataPackageUtils;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.analog.AnDataType;
import org.jiahuan.service.analog.IAnDataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class AnDataTypeController {

    @Autowired
    private IAnDataTypeService iAnDataTypeService;

    @GetMapping("/sendRealTime")
    public RetMsgData<AnDataType> sendRealTime(@RequestParam Integer deviceId, @RequestParam Integer dataType) {
        RetMsgData<AnDataType> msgData = new RetMsgData<>();
        try {
            iAnDataTypeService.sendRealTime(deviceId, dataType);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/sendParam3020")
    public RetMsgData<AnDataType> sendParam3020(@RequestParam Integer deviceId, @RequestParam Integer dataType) {
        RetMsgData<AnDataType> msgData = new RetMsgData<>();
        try {
            iAnDataTypeService.sendParam3020(deviceId, dataType);
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
            String dataPackage = iAnDataTypeService.getDataPackage(deviceId, dataType);
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
    public RetMsgData<AnDataType> sendCustomizeMessage(@RequestParam Integer deviceId, @RequestParam String msg) {
        RetMsgData<AnDataType> msgData = new RetMsgData<>();
        try {
            iAnDataTypeService.sendCustomizeMessage(deviceId, msg);
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
            Integer supplyAgainCount = iAnDataTypeService.getSupplyAgainCount(deviceId, dataType);
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
        msgData.setData(iAnDataTypeService.getSupplyAgainStatus(deviceId));
        return msgData;
    }

    @GetMapping("/waitForTheReissueToComplete")
    public RetMsgData<Boolean> waitForTheReissueToComplete(@RequestParam Integer deviceId) {
        RetMsgData<Boolean> msgData = new RetMsgData<>();
        try {
            iAnDataTypeService.waitForTheReissueToComplete(deviceId);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/sendSupplyAgain")
    public RetMsgData<AnDataType> sendSupplyAgain(@RequestParam Integer deviceId, @RequestParam Integer dataType) {
        RetMsgData<AnDataType> msgData = new RetMsgData<>();
        try {
            iAnDataTypeService.sendSupplyAgain(deviceId, dataType);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/cancelSupplyAgain")
    public RetMsgData<AnDataType> cancelSupplyAgain(@RequestParam Integer deviceId) {
        RetMsgData<AnDataType> msgData = new RetMsgData<>();
            iAnDataTypeService.setSupplyAgainStatus(deviceId,false);
            return msgData;
    }

    @GetMapping("/getListByDeviceId")
    public RetMsgData<List<AnDataType>> getListCounDataTypeByDeviceId(@RequestParam Integer deviceId) {
        RetMsgData<List<AnDataType>> msgData = new RetMsgData<>();
        try {
            List<AnDataType> anDataTypes = iAnDataTypeService.getListCounDataTypeByDeviceId(deviceId);
            msgData.setData(anDataTypes);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<AnDataType> updateCounDataType(@RequestBody AnDataType anDataType) {
        RetMsgData<AnDataType> msgData = new RetMsgData<>();
        try {
            iAnDataTypeService.updateById(anDataType);
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

}

