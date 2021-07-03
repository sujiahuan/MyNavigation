package org.jiahuan.controller.analog;


import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.analog.AnDynamicDivisor;
import org.jiahuan.service.analog.IAnDynamicDivisorService;
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
public class AnDynamicDivisorController {

    @Autowired
    private IAnDynamicDivisorService iAnDynamicDivisorService;

    @GetMapping("/getByDeviceId")
    public RetMsgData<AnDynamicDivisor> getListCounCode(@RequestParam Integer deviceId){
        RetMsgData<AnDynamicDivisor> msgData = new RetMsgData<>();

        try {
            AnDynamicDivisor anDynamicDivisor = iAnDynamicDivisorService.getDynamicDivisorByDeviceId(deviceId);
            msgData.setData(anDynamicDivisor);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/addOrUpdate")
    public RetMsgData<AnDynamicDivisor> addCounCode(@RequestBody AnDynamicDivisor anDynamicDivisor){
        RetMsgData<AnDynamicDivisor> msgData = new RetMsgData<>();

        if(VerdictUtil.isNull(anDynamicDivisor.getDivisorId())){
            msgData.setMsg("code为空");
            return msgData;
        }

        try {
            iAnDynamicDivisorService.saveOrUpdate(anDynamicDivisor);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<AnDynamicDivisor> deleteById(@RequestParam Integer id){
        RetMsgData<AnDynamicDivisor> msgData = new RetMsgData<>();

        try {
            iAnDynamicDivisorService.removeById(id);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

}

