package org.jiahuan.controller.coun;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.model.State;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.coun.CounDevice;
import org.jiahuan.entity.sys.SysIcom;
import org.jiahuan.entity.sys.SysNavigation;
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
 * @since 2020-07-26
 */
@RestController
@RequestMapping("/counDevice")
public class CounDeviceController {

    @Autowired
    private ICounDeviceService iCounDeviceService;

    @PostMapping("/add")
    public RetMsgData<CounDevice> addCounDevice(@RequestBody CounDevice counDevice){
        RetMsgData<CounDevice> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(counDevice.getIp())){
            msgData.setMsg("ip为空");
            return msgData;
        }
        if(VerdictUtil.isNull(counDevice.getPort())){
            msgData.setMsg("port为空");
            return msgData;
        }

        if(VerdictUtil.isNull(counDevice.getMn())){
            msgData.setMsg("Mn为空");
            return msgData;
        }

        if(VerdictUtil.isNull(counDevice.getMonitoringType())){
            msgData.setMsg("monitoringType为空");
            return msgData;
        }

        if(VerdictUtil.isNull(counDevice.getAgreement())){
            msgData.setMsg("agreement为空");
            return msgData;
        }

        counDevice.setGmtCreate(LocalDateTime.now());

        try{
            iCounDeviceService.addInitCounDevice(counDevice);
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<CounDevice> deleteById(@RequestParam Integer id){
        RetMsgData<CounDevice> msgData = new RetMsgData<>();

        try{
            iCounDeviceService.deleteInitById(id);
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<CounDevice> getById(@RequestParam Integer id){
        RetMsgData<CounDevice> msgData = new RetMsgData<>();
        try{
            msgData.setData(iCounDeviceService.getById(id));
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            return msgData;
        }
    }

    @GetMapping("/getAll")
    public RetMsgData<List<CounDevice>> getAll(){
        RetMsgData<List<CounDevice>> msgData = new RetMsgData<>();
        QueryWrapper<CounDevice> queryWrapper = new QueryWrapper<>();
        try{
            msgData.setData(iCounDeviceService.list(queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<CounDevice>> getPage(@RequestParam Integer page, @RequestParam Integer size,@RequestParam String mn){
        RetMsgData<IPage<CounDevice>> msgData = new RetMsgData<>();
        Page<CounDevice> page1 = new Page<>(page, size);
        QueryWrapper<CounDevice> queryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(mn)){
            queryWrapper.like("mn",mn);
        }
        try{
            msgData.setData(iCounDeviceService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<CounDevice>> update(@RequestBody CounDevice counDevice){
        RetMsgData<IPage<CounDevice>> msgData = new RetMsgData<>();

        counDevice.setGmtModified(LocalDateTime.now());

        try{
            iCounDeviceService.updateById(counDevice);
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            return msgData;
        }
    }

}

