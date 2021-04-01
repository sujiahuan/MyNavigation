package org.jiahuan.controller.analog;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.analog.AnalogDynamicParameter;
import org.jiahuan.service.analog.IAnalogDynamicParameterService;
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
@RequestMapping("/dynamicParameter")
@Slf4j
public class AnalogDynamicParameterController {

    @Autowired
    private IAnalogDynamicParameterService iAnalogDynamicParameterService;

    @PostMapping("/add")
    public RetMsgData<AnalogDynamicParameter> add(@RequestBody AnalogDynamicParameter obj){
        RetMsgData<AnalogDynamicParameter> msgData = new RetMsgData<>();

        obj.setGmtCreate(LocalDateTime.now());

        try{
            iAnalogDynamicParameterService.save(obj);
            msgData.setData(obj);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("新增失败：{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<AnalogDynamicParameter> delete(@RequestParam Integer id){
        RetMsgData<AnalogDynamicParameter> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }

        try{
            iAnalogDynamicParameterService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("删除失败：{}",e);
            return msgData;
        }
    }

    @GetMapping("/getList")
    public RetMsgData<List<AnalogDynamicParameter>> get(@RequestParam Integer deviceId,Integer type){
        RetMsgData<List<AnalogDynamicParameter>> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(deviceId)){
            msgData.setMsg("设备id为空");
        }
        try{
            msgData.setData(iAnalogDynamicParameterService.getDynamicParameterByDeviceId(deviceId,type));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<AnalogDynamicParameter>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required=false)String name){
        RetMsgData<IPage<AnalogDynamicParameter>> msgData = new RetMsgData<>();
        Page<AnalogDynamicParameter> page1 = new Page<>(page, size);
        QueryWrapper<AnalogDynamicParameter> queryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(name)){
            queryWrapper.like("name",name);
        }
        try{
            msgData.setData(iAnalogDynamicParameterService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<AnalogDynamicParameter>> update(@RequestBody AnalogDynamicParameter obj){
        RetMsgData<IPage<AnalogDynamicParameter>> msgData = new RetMsgData<>();

//        AnalogCodeParameter byId = iAnalogCodeParameterService.getById(obj.getId());
//        if(VerdictUtil.isNull(byId)){
//            msgData.setMsg("找不到要修改的信息");
//            return msgData;
//        }

        obj.setGmtModified(LocalDateTime.now());

        UpdateWrapper<AnalogDynamicParameter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",obj.getId());

        obj.setGmtModified(LocalDateTime.now());

        try{
            iAnalogDynamicParameterService.updateById(obj);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

}

