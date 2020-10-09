package org.jiahuan.controller.coun;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.model.State;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.coun.CounDivisor;
import org.jiahuan.service.coun.ICounDivisorService;
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
@RequestMapping("/counDivisor")
@Slf4j
public class CounDivisorController {

    @Autowired
    private ICounDivisorService iCounDivisorService;

    @PostMapping("/add")
    public RetMsgData<CounDivisor> add(@RequestBody CounDivisor obj){
        RetMsgData<CounDivisor> msgData = new RetMsgData<>();

//        if(VerdictUtil.isNull(CounDivisor.getName())){
//            msgData.setMsg("name为空");
//        }

        obj.setGmtCreate(LocalDateTime.now());

        try{
            iCounDivisorService.save(obj);
            msgData.setData(obj);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("新增失败：{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<CounDivisor> delete(@RequestParam Integer id){
        RetMsgData<CounDivisor> msgData = new RetMsgData<>();
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
            iCounDivisorService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("删除失败：{}",e);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<CounDivisor> get(@RequestParam Integer id){
        RetMsgData<CounDivisor> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            msgData.setData(iCounDivisorService.getById(id));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getListByDeviceId")
    public RetMsgData<List<CounDivisor>> getAll(@RequestParam Integer deviceId){
        RetMsgData<List<CounDivisor>> msgData = new RetMsgData<>();
        QueryWrapper<CounDivisor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        try{
            msgData.setData(iCounDivisorService.list(queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<CounDivisor>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam Integer deviceId){
        RetMsgData<IPage<CounDivisor>> msgData = new RetMsgData<>();
        Page<CounDivisor> page1 = new Page<>(page, size);
        QueryWrapper<CounDivisor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        queryWrapper.orderByDesc("gmt_create");
        try{
            msgData.setData(iCounDivisorService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<CounDivisor>> update(@RequestBody CounDivisor obj){
        RetMsgData<IPage<CounDivisor>> msgData = new RetMsgData<>();

        CounDivisor byId = iCounDivisorService.getById(obj.getId());
        if(VerdictUtil.isNull(byId)){
            msgData.setMsg("找不到要修改的信息");
            return msgData;
        }

//        if(VerdictUtil.isNull(obj.getName())){
//            msgData.setMsg("name为空");
//            return msgData;
//        }
        UpdateWrapper<CounDivisor> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",obj.getId());

        obj.setGmtModified(LocalDateTime.now());

        try{
            iCounDivisorService.update(obj,updateWrapper);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

}

