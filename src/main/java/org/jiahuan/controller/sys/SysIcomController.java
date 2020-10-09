package org.jiahuan.controller.sys;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.model.State;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.sys.SysIcom;
import org.jiahuan.entity.sys.SysNavigation;
import org.jiahuan.service.sys.ISysIcomService;
import org.jiahuan.service.sys.ISysNavigationService;
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
 * @since 2020-06-16
 */
@RestController
@RequestMapping("/icom")
@Slf4j
public class SysIcomController {

    @Autowired
    private ISysIcomService iIcomService;
    @Autowired
    private ISysNavigationService iNavigationService;

    @PostMapping("/add")
    public RetMsgData<SysIcom> addIcom(@RequestBody SysIcom icom){
        RetMsgData<SysIcom> retMsgData = new RetMsgData<>();
        if(VerdictUtil.isNull(icom.getName())){
            retMsgData.setMsg("name为空");
        }

        icom.setGmtCreate(LocalDateTime.now());

        try{
            iIcomService.save(icom);
            retMsgData.setData(icom);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setMsg(e.getMessage());
            return retMsgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<SysIcom> deleteIcom(@RequestParam Integer id){
        RetMsgData<SysIcom> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        QueryWrapper<SysNavigation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("icom_id",id);
        List<SysNavigation> navigations = iNavigationService.getNavigations(queryWrapper);
        if(navigations.size()!=0){
            msgData.setMsg("该图标已关联侧边栏，请删除关联");
            return msgData;
        }
        try{
            iIcomService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("删除Icom失败：{}",e);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<SysIcom> getIcom(@RequestParam Integer id){
        RetMsgData<SysIcom> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            msgData.setData(iIcomService.getById(id));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getAll")
    public RetMsgData<List<SysIcom>> getAll(){
        RetMsgData<List<SysIcom>> msgData = new RetMsgData<>();
        QueryWrapper<SysIcom> icomQueryWrapper = new QueryWrapper<>();
        try{
            msgData.setData(iIcomService.list(icomQueryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<SysIcom>> getPageIcom(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required=false)String name){
        RetMsgData<IPage<SysIcom>> msgData = new RetMsgData<>();
        Page<SysIcom> icomPage = new Page<>(page, size);
        QueryWrapper<SysIcom> queryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(name)){
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByDesc("gmt_create");
        try{
            IPage<SysIcom> page1 = iIcomService.page(icomPage, queryWrapper);
            msgData.setData(page1);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<SysIcom>> updateIcom(@RequestBody SysIcom icom){
        RetMsgData<IPage<SysIcom>> msgData = new RetMsgData<>();

        SysIcom byId = iIcomService.getById(icom.getId());
        if(VerdictUtil.isNull(byId)){
            msgData.setMsg("找不到要修改的Icom信息");
            return msgData;
        }

        if(VerdictUtil.isNull(icom.getName())){
            msgData.setMsg("name为空");
            return msgData;
        }
        UpdateWrapper<SysIcom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",icom.getId());

        icom.setGmtModified(LocalDateTime.now());

        try{
            iIcomService.update(icom,updateWrapper);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }

    }

}

