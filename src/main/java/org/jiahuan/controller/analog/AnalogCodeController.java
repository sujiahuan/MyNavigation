package org.jiahuan.controller.analog;


import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.analog.AnalogCode;
import org.jiahuan.service.analog.IAnalogCodeService;
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
@RequestMapping("/counCode")
public class AnalogCodeController {

    @Autowired
    private IAnalogCodeService iAnalogCodeService;

    @GetMapping("/getListCounCodeByDeviceId")
    public RetMsgData<AnalogCode> getListCounCode(@RequestParam Integer deviceId){
        RetMsgData<AnalogCode> msgData = new RetMsgData<>();

        try {
            AnalogCode analogCode = iAnalogCodeService.getCounCodeByDeviceId(deviceId);
            msgData.setData(analogCode);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/add")
    public RetMsgData<AnalogCode> addCounCode(@RequestBody AnalogCode analogCode){
        RetMsgData<AnalogCode> msgData = new RetMsgData<>();

        if(VerdictUtil.isNull(analogCode.getDeviceId())){
            msgData.setMsg("deviceId为空");
            return msgData;
        }
        if(VerdictUtil.isNull(analogCode.getCode())){
            msgData.setMsg("code为空");
            return msgData;
        }

        try {
            iAnalogCodeService.save(analogCode);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<AnalogCode> updateCounCode(@RequestBody AnalogCode analogCode){
        RetMsgData<AnalogCode> msgData = new RetMsgData<>();

        if(VerdictUtil.isNull(analogCode.getDeviceId())){
            msgData.setMsg("deviceId为空");
            return msgData;
        }
        if(VerdictUtil.isNull(analogCode.getCode())){
            msgData.setMsg("code为空");
            return msgData;
        }

        try {
            iAnalogCodeService.updateById(analogCode);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }


    @DeleteMapping("/deleteById")
    public RetMsgData<AnalogCode> deleteById(@RequestParam Integer id){
        RetMsgData<AnalogCode> msgData = new RetMsgData<>();

        try {
            iAnalogCodeService.removeById(id);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

}

