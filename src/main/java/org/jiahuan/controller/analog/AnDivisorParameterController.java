package org.jiahuan.controller.analog;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.analog.AnDivisorParameter;
import org.jiahuan.service.analog.IAnDivisorParameterService;
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
public class AnDivisorParameterController {

    @Autowired
    private IAnDivisorParameterService iAnDivisorParameterService;

    @PostMapping("/add")
    public RetMsgData<List<AnDivisorParameter>> add(@RequestBody List<AnDivisorParameter> divisorParameters){
        RetMsgData<List<AnDivisorParameter>> msgData = new RetMsgData<>();

        divisorParameters.forEach(divisorParameter -> {
            divisorParameter.setGmtCreate(LocalDateTime.now());
        });

        try{
            iAnDivisorParameterService.saveOrUpdateBatch(divisorParameters);
            msgData.setData(divisorParameters);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("新增失败：{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<AnDivisorParameter> delete(@RequestParam Integer id){
        RetMsgData<AnDivisorParameter> msgData = new RetMsgData<>();
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
            iAnDivisorParameterService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("删除失败：{}",e);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<AnDivisorParameter> get(@RequestParam Integer id){
        RetMsgData<AnDivisorParameter> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            msgData.setData(iAnDivisorParameterService.getById(id));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getListByDeviceId")
    public RetMsgData<List<AnDivisorParameter>> getAll(@RequestParam Integer deviceId){
        RetMsgData<List<AnDivisorParameter>> msgData = new RetMsgData<>();
        QueryWrapper<AnDivisorParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        try{
            msgData.setData(iAnDivisorParameterService.list(queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<AnDivisorParameter>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam Integer deviceId){
        RetMsgData<IPage<AnDivisorParameter>> msgData = new RetMsgData<>();
        Page<AnDivisorParameter> page1 = new Page<>(page, size);
        QueryWrapper<AnDivisorParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        queryWrapper.orderByDesc("gmt_create");
        try{
            msgData.setData(iAnDivisorParameterService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<AnDivisorParameter>> update(@RequestBody AnDivisorParameter obj){
        RetMsgData<IPage<AnDivisorParameter>> msgData = new RetMsgData<>();

        AnDivisorParameter byId = iAnDivisorParameterService.getById(obj.getId());
        if(VerdictUtil.isNull(byId)){
            msgData.setMsg("找不到要修改的信息");
            return msgData;
        }

//        if(VerdictUtil.isNull(obj.getName())){
//            msgData.setMsg("name为空");
//            return msgData;
//        }
        UpdateWrapper<AnDivisorParameter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",obj.getId());

        obj.setGmtModified(LocalDateTime.now());

        try{
            iAnDivisorParameterService.update(obj,updateWrapper);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

}

