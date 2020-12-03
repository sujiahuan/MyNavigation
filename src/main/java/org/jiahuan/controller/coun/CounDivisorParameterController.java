package org.jiahuan.controller.coun;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.coun.CounDivisorParameter;
import org.jiahuan.service.coun.ICounDivisorParameterService;
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
public class CounDivisorParameterController {

    @Autowired
    private ICounDivisorParameterService iCounDivisorParameterService;

    @PostMapping("/add")
    public RetMsgData<CounDivisorParameter> add(@RequestBody CounDivisorParameter obj){
        RetMsgData<CounDivisorParameter> msgData = new RetMsgData<>();

//        if(VerdictUtil.isNull(CounDivisor.getName())){
//            msgData.setMsg("name为空");
//        }

        obj.setGmtCreate(LocalDateTime.now());

        try{
            iCounDivisorParameterService.save(obj);
            msgData.setData(obj);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("新增失败：{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<CounDivisorParameter> delete(@RequestParam Integer id){
        RetMsgData<CounDivisorParameter> msgData = new RetMsgData<>();
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
            iCounDivisorParameterService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("删除失败：{}",e);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<CounDivisorParameter> get(@RequestParam Integer id){
        RetMsgData<CounDivisorParameter> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            msgData.setData(iCounDivisorParameterService.getById(id));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getListByDeviceId")
    public RetMsgData<List<CounDivisorParameter>> getAll(@RequestParam Integer deviceId){
        RetMsgData<List<CounDivisorParameter>> msgData = new RetMsgData<>();
        QueryWrapper<CounDivisorParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        try{
            msgData.setData(iCounDivisorParameterService.list(queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<CounDivisorParameter>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam Integer deviceId){
        RetMsgData<IPage<CounDivisorParameter>> msgData = new RetMsgData<>();
        Page<CounDivisorParameter> page1 = new Page<>(page, size);
        QueryWrapper<CounDivisorParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        queryWrapper.orderByDesc("gmt_create");
        try{
            msgData.setData(iCounDivisorParameterService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<CounDivisorParameter>> update(@RequestBody CounDivisorParameter obj){
        RetMsgData<IPage<CounDivisorParameter>> msgData = new RetMsgData<>();

        CounDivisorParameter byId = iCounDivisorParameterService.getById(obj.getId());
        if(VerdictUtil.isNull(byId)){
            msgData.setMsg("找不到要修改的信息");
            return msgData;
        }

//        if(VerdictUtil.isNull(obj.getName())){
//            msgData.setMsg("name为空");
//            return msgData;
//        }
        UpdateWrapper<CounDivisorParameter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",obj.getId());

        obj.setGmtModified(LocalDateTime.now());

        try{
            iCounDivisorParameterService.update(obj,updateWrapper);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

}

