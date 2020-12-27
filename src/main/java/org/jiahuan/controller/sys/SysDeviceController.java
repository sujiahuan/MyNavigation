package org.jiahuan.controller.sys;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.service.sys.ISysDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class SysDeviceController {

    @Autowired
    private ISysDeviceService iSysDeviceService;

    @PostMapping("/add")
    public RetMsgData<SysDevice> addCounDevice(@RequestBody SysDevice sysDevice){
        RetMsgData<SysDevice> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(sysDevice.getIp())){
            msgData.setMsg("ip为空");
            return msgData;
        }
        if(VerdictUtil.isNull(sysDevice.getPort())){
            msgData.setMsg("port为空");
            return msgData;
        }

        if(VerdictUtil.isNull(sysDevice.getMn())){
            msgData.setMsg("Mn为空");
            return msgData;
        }

        if(VerdictUtil.isNull(sysDevice.getMonitoringType())){
            msgData.setMsg("monitoringType为空");
            return msgData;
        }

        if(VerdictUtil.isNull(sysDevice.getAgreement())){
            msgData.setMsg("agreement为空");
            return msgData;
        }

        sysDevice.setGmtCreate(LocalDateTime.now());

        try{
            iSysDeviceService.addInitCounDevice(sysDevice);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<SysDevice> deleteById(@RequestParam Integer id){
        RetMsgData<SysDevice> msgData = new RetMsgData<>();

        try{
            iSysDeviceService.deleteInitById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<SysDevice> getById(@RequestParam Integer id){
        RetMsgData<SysDevice> msgData = new RetMsgData<>();
        try{
            msgData.setData(iSysDeviceService.getById(id));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getAll")
    public RetMsgData<List<SysDevice>> getAll(){
        RetMsgData<List<SysDevice>> msgData = new RetMsgData<>();
        QueryWrapper<SysDevice> queryWrapper = new QueryWrapper<>();
        try{
            msgData.setData(iSysDeviceService.list(queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<SysDevice>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String mn, @RequestParam Integer id){
        RetMsgData<IPage<SysDevice>> msgData = new RetMsgData<>();
        Page<SysDevice> page1 = new Page<>(page, size);
        QueryWrapper<SysDevice> queryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(mn)){
            queryWrapper.eq("mn",mn);
        }
        if(VerdictUtil.isNotNull(id)){
            queryWrapper.eq("id",id);
        }

        queryWrapper.orderByDesc("gmt_create");

        try{
            msgData.setData(iSysDeviceService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<SysDevice>> update(@RequestBody SysDevice sysDevice){
        RetMsgData<IPage<SysDevice>> msgData = new RetMsgData<>();

        sysDevice.setGmtModified(LocalDateTime.now());

        try{
            iSysDeviceService.updateCounDevice(sysDevice);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

}

