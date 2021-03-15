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
    @Autowired
    private CustomWebSocketConfig customWebSocketConfig;

    @GetMapping("/sendRealTime")
    public RetMsgData<AnalogDataType> sendRealTime(@RequestParam Integer deviceId, @RequestParam Integer dataType) {
        RetMsgData<AnalogDataType> msgData = new RetMsgData<>();
        try {
            iAnalogDataTypeService.sendRealTime(deviceId, dataType);
        } catch (ConnectException e) {
            if (e.getMessage().equals("Connection timed out: connect") || e.getMessage().equals("Connection timed out (Connection timed out)")) {
                msgData.setMsg(" 连接服务器超时，请检查服务器能否正常连接");
            } else if (e.getMessage().equals("Connection refused: connect") || e.getMessage().equals("Connection refused (Connection refused)")) {
                msgData.setMsg(" 连接服务器被拒绝，请检查服务器能否正常连接");
            } else {
                e.printStackTrace();
                msgData.setMsg("没处理的异常：" + e.getMessage());
            }
        } catch (SocketException e) {
            if (e.getMessage().equals("Software caused connection abort: socket write error")) {
                msgData.setMsg("连接已断开，请重连");
            } else if (e.getMessage().equals("Connection reset by peer: send")) {
                msgData.setMsg("连接已断开，请重连");
            } else if (e.getMessage().equals("Software caused connection abort: send")) {
                msgData.setMsg("连接已断开，请重连");
            } else {
                e.printStackTrace();
                msgData.setMsg("没处理的异常：" + e.getMessage());
            }
        } catch (SocketTimeoutException e) {
            if (e.getMessage().equals("connect timed out")) {
                msgData.setMsg("连接已超时，请检查该服务器是否能连上");
            } else {
                msgData.setMsg("没处理的异常：" + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/getRealTime")
    public RetMsgData<String> getRealTime(@RequestParam Integer deviceId, @RequestParam Integer dataType) {
        RetMsgData<String> msgData = new RetMsgData<>();
        try {
            String dataPackage = iAnalogDataTypeService.getDataPackage(deviceId, dataType);
            msgData.setData(dataPackage);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/sendMessage")
    public RetMsgData<AnalogDataType> sendMessage(@RequestParam Integer deviceId, @RequestParam String msg) {
        RetMsgData<AnalogDataType> msgData = new RetMsgData<>();
        try {
            ArrayList<String> dataPack = new ArrayList<>();
            iAnalogDataTypeService.sendMessage(deviceId, msg, dataPack);
            customWebSocketConfig.customWebSocketHandler().sendMessageToUser(String.valueOf(deviceId), new TextMessage(dataPack.toString()));
        } catch (ConnectException e) {
            if (e.getMessage().equals("Connection timed out: connect") || e.getMessage().equals("Connection timed out (Connection timed out)")) {
                msgData.setMsg(" 连接服务器超时，请检查");
            } else if (e.getMessage().equals("Connection refused: connect") || e.getMessage().equals("Connection refused (Connection refused)")) {
                msgData.setMsg(" 连接服务器被拒绝，请检查");
            } else {
                msgData.setMsg("没处理的异常：" + e.getMessage());
            }
        } catch (SocketException e) {
            if (e.getMessage().equals("Software caused connection abort: socket write error")) {
                msgData.setMsg("连接已断开，请重连");
            } else if (e.getMessage().equals("Connection reset by peer: send")) {
                msgData.setMsg("连接已断开，请重连");
            } else if (e.getMessage().equals("Software caused connection abort: send")) {
                msgData.setMsg("连接已断开，请重连");
            } else {
                e.printStackTrace();
                msgData.setMsg("没处理的异常：" + e.getMessage());
            }
        } catch (SocketTimeoutException e) {
            if (e.getMessage().equals("connect timed out")) {
                msgData.setMsg("连接已超时，请检查该服务器是否能连上");
            } else {
                msgData.setMsg("没处理的异常：" + e.getMessage());
            }
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
        } catch (ConnectException e) {
            if (e.getMessage().equals("Connection timed out: connect") || e.getMessage().equals("Connection timed out (Connection timed out)")) {
                msgData.setMsg(" 连接服务器超时，请检查");
            } else if (e.getMessage().equals("Connection refused: connect") || e.getMessage().equals("Connection refused (Connection refused)")) {
                msgData.setMsg(" 连接服务器被拒绝，请检查");
            } else {
                msgData.setMsg("没处理的异常：" + e.getMessage());
            }
        } catch (SocketException e) {
            if (e.getMessage().equals("Software caused connection abort: socket write error")) {
                msgData.setMsg("连接已断开，请重连");
            } else if (e.getMessage().equals("Connection reset by peer: send")) {
                msgData.setMsg("连接已断开，请重连");
            } else if (e.getMessage().equals("Software caused connection abort: send")) {
                msgData.setMsg("连接已断开，请重连");
            } else {
                e.printStackTrace();
                msgData.setMsg("没处理的异常：" + e.getMessage());
            }
        } catch (SocketTimeoutException e) {
            if (e.getMessage().equals("connect timed out")) {
                msgData.setMsg("连接已超时，请检查该服务器是否能连上");
            } else {
                msgData.setMsg("没处理的异常：" + e.getMessage());
            }
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

    @GetMapping("/cancelSupplyAgain")
    public RetMsgData<AnalogDataType> cancelSupplyAgain(@RequestParam Integer deviceId) {
        RetMsgData<AnalogDataType> msgData = new RetMsgData<>();
            iAnalogDataTypeService.cancelSupplyAgain(deviceId);
            return msgData;
    }

    @GetMapping("/getListByDeviceId")
    public RetMsgData<List<AnalogDataType>> getListCounDataTypeByDeviceId(@RequestParam Integer deviceId) {
        RetMsgData<List<AnalogDataType>> msgData = new RetMsgData<>();
        try {
            List<AnalogDataType> analogDataTypes = iAnalogDataTypeService.getListCounDataTypeByDeviceId(deviceId);
            msgData.setData(analogDataTypes);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            msgData.setMsg(e.getMessage());
        } finally {
            return msgData;
        }
    }

}

