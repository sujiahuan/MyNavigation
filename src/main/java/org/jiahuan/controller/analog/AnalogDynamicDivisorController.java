package org.jiahuan.controller.analog;


import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.analog.AnalogDynamicDivisor;
import org.jiahuan.service.analog.IAnalogDynamicDivisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jh
 * @since 2020-08-05
 */
@RestController
@RequestMapping("/dynamicDivisor")
public class AnalogDynamicDivisorController {

    @Autowired
    private IAnalogDynamicDivisorService iAnalogDynamicDivisorService;

    @GetMapping("/getListByDeviceId")
    public RetMsgData<AnalogDynamicDivisor> getListCounCode(@RequestParam Integer deviceId){
        RetMsgData<AnalogDynamicDivisor> msgData = new RetMsgData<>();

        try {
            AnalogDynamicDivisor analogDynamicDivisor = iAnalogDynamicDivisorService.getDynamicDivisorByDeviceId(deviceId);
            msgData.setData(analogDynamicDivisor);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/add")
    public RetMsgData<AnalogDynamicDivisor> addCounCode(@RequestBody AnalogDynamicDivisor analogDynamicDivisor){
        RetMsgData<AnalogDynamicDivisor> msgData = new RetMsgData<>();

        if(VerdictUtil.isNull(analogDynamicDivisor.getDeviceId())){
            msgData.setMsg("deviceId为空");
            return msgData;
        }
        if(VerdictUtil.isNull(analogDynamicDivisor.getDivisorId())){
            msgData.setMsg("code为空");
            return msgData;
        }

        try {
            iAnalogDynamicDivisorService.save(analogDynamicDivisor);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<AnalogDynamicDivisor> updateCounCode(@RequestBody AnalogDynamicDivisor analogDynamicDivisor){
        RetMsgData<AnalogDynamicDivisor> msgData = new RetMsgData<>();

        if(VerdictUtil.isNull(analogDynamicDivisor.getDeviceId())){
            msgData.setMsg("deviceId为空");
            return msgData;
        }
        if(VerdictUtil.isNull(analogDynamicDivisor.getDivisorId())){
            msgData.setMsg("code为空");
            return msgData;
        }

        try {
            iAnalogDynamicDivisorService.updateById(analogDynamicDivisor);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }


    @DeleteMapping("/deleteById")
    public RetMsgData<AnalogDynamicDivisor> deleteById(@RequestParam Integer id){
        RetMsgData<AnalogDynamicDivisor> msgData = new RetMsgData<>();

        try {
            iAnalogDynamicDivisorService.removeById(id);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

}

