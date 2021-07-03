package org.jiahuan.controller.analog;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.analog.AnDynamicParameter;
import org.jiahuan.service.analog.IAnDynamicParameterService;
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
public class AnDynamicParameterController {

    @Autowired
    private IAnDynamicParameterService iAnDynamicParameterService;

    @PostMapping("/addOrUpdate")
    public RetMsgData<List<AnDynamicParameter>> add(@RequestBody List<AnDynamicParameter> objs){
        RetMsgData<List<AnDynamicParameter>> msgData = new RetMsgData<>();

        objs.forEach(obj->obj.setGmtCreate(LocalDateTime.now()));

        try{
            iAnDynamicParameterService.saveOrUpdateBatch(objs);
            msgData.setData(objs);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("新增失败：{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<AnDynamicParameter> delete(@RequestParam Integer id){
        RetMsgData<AnDynamicParameter> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }

        try{
            iAnDynamicParameterService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("删除失败：{}",e);
            return msgData;
        }
    }

    @GetMapping("/getList")
    public RetMsgData<List<AnDynamicParameter>> get(@RequestParam Integer deviceId, Integer type){
        RetMsgData<List<AnDynamicParameter>> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(deviceId)){
            msgData.setMsg("设备id为空");
        }
        try{
            msgData.setData(iAnDynamicParameterService.getDynamicParameterByDeviceId(deviceId,type));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<AnDynamicParameter>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required=false)String name){
        RetMsgData<IPage<AnDynamicParameter>> msgData = new RetMsgData<>();
        Page<AnDynamicParameter> page1 = new Page<>(page, size);
        QueryWrapper<AnDynamicParameter> queryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(name)){
            queryWrapper.like("name",name);
        }
        try{
            msgData.setData(iAnDynamicParameterService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

}

