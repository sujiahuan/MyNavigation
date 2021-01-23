package org.jiahuan.controller.analog;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.analog.AnalogDivisorParameter;
import org.jiahuan.service.analog.IAnalogDivisorParameterService;
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
@RequestMapping("/counDivisor")
@Slf4j
public class AnalogDivisorParameterController {

    @Autowired
    private IAnalogDivisorParameterService iAnalogDivisorParameterService;

    @PostMapping("/add")
    public RetMsgData<List<AnalogDivisorParameter>> add(@RequestBody List<AnalogDivisorParameter> divisorParameters){
        RetMsgData<List<AnalogDivisorParameter>> msgData = new RetMsgData<>();

        divisorParameters.forEach(divisorParameter -> {
            divisorParameter.setGmtCreate(LocalDateTime.now());
        });

        try{
            iAnalogDivisorParameterService.saveOrUpdateBatch(divisorParameters);
            msgData.setData(divisorParameters);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("新增失败：{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<AnalogDivisorParameter> delete(@RequestParam Integer id){
        RetMsgData<AnalogDivisorParameter> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }

//        QueryWrapper<CounDivisor> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("icom_id",id);
//        List<CounDivisor> navigations = iCounDivisorService.getNavigations(queryWrapper);
//        if(navigations.size()!=0){
//            msgData.setMsg("该图标已关联侧边栏，请删除关联");
//            return msgData;
//        }

        try{
            iAnalogDivisorParameterService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("删除失败：{}",e);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<AnalogDivisorParameter> get(@RequestParam Integer id){
        RetMsgData<AnalogDivisorParameter> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            msgData.setData(iAnalogDivisorParameterService.getById(id));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getListByDeviceId")
    public RetMsgData<List<AnalogDivisorParameter>> getAll(@RequestParam Integer deviceId){
        RetMsgData<List<AnalogDivisorParameter>> msgData = new RetMsgData<>();
        QueryWrapper<AnalogDivisorParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        try{
            msgData.setData(iAnalogDivisorParameterService.list(queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<AnalogDivisorParameter>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam Integer deviceId){
        RetMsgData<IPage<AnalogDivisorParameter>> msgData = new RetMsgData<>();
        Page<AnalogDivisorParameter> page1 = new Page<>(page, size);
        QueryWrapper<AnalogDivisorParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        queryWrapper.orderByDesc("gmt_create");
        try{
            msgData.setData(iAnalogDivisorParameterService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<AnalogDivisorParameter>> update(@RequestBody AnalogDivisorParameter obj){
        RetMsgData<IPage<AnalogDivisorParameter>> msgData = new RetMsgData<>();

        AnalogDivisorParameter byId = iAnalogDivisorParameterService.getById(obj.getId());
        if(VerdictUtil.isNull(byId)){
            msgData.setMsg("找不到要修改的信息");
            return msgData;
        }

//        if(VerdictUtil.isNull(obj.getName())){
//            msgData.setMsg("name为空");
//            return msgData;
//        }
        UpdateWrapper<AnalogDivisorParameter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",obj.getId());

        obj.setGmtModified(LocalDateTime.now());

        try{
            iAnalogDivisorParameterService.update(obj,updateWrapper);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

}

