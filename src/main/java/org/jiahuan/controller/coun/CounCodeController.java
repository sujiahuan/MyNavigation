package org.jiahuan.controller.coun;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.model.State;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.coun.CounCode;
import org.jiahuan.service.coun.ICounCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

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
public class CounCodeController {

    @Autowired
    private ICounCodeService iCounCodeService;

    @GetMapping("/getListCounCodeByDeviceId")
    public RetMsgData<CounCode> getListCounCode(@RequestParam Integer deviceId){
        RetMsgData<CounCode> msgData = new RetMsgData<>();

        try {
            CounCode counCode = iCounCodeService.getCounCodeByDeviceId(deviceId);
            msgData.setData(counCode);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/add")
    public RetMsgData<CounCode> addCounCode(@RequestBody CounCode counCode){
        RetMsgData<CounCode> msgData = new RetMsgData<>();

        if(VerdictUtil.isNull(counCode.getDeviceId())){
            msgData.setMsg("deviceId为空");
            return msgData;
        }
        if(VerdictUtil.isNull(counCode.getCode())){
            msgData.setMsg("code为空");
            return msgData;
        }

        try {
            iCounCodeService.save(counCode);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<CounCode> updateCounCode(@RequestBody CounCode counCode){
        RetMsgData<CounCode> msgData = new RetMsgData<>();

        if(VerdictUtil.isNull(counCode.getDeviceId())){
            msgData.setMsg("deviceId为空");
            return msgData;
        }
        if(VerdictUtil.isNull(counCode.getCode())){
            msgData.setMsg("code为空");
            return msgData;
        }

        try {
            iCounCodeService.updateById(counCode);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }


    @DeleteMapping("/deleteById")
    public RetMsgData<CounCode> deleteById(@RequestParam Integer id){
        RetMsgData<CounCode> msgData = new RetMsgData<>();

        try {
            iCounCodeService.removeById(id);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

}

