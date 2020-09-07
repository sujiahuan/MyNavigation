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
import org.jiahuan.service.sys.ISysBookmarkService;
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
@RequestMapping("/navigation")
@Slf4j
public class SysNavigationController {

    @Autowired
    private ISysNavigationService iNavigationService;
    @Autowired
    private ISysBookmarkService iBookmarkService;
    @Autowired
    private ISysIcomService iIcomService;

    @PostMapping("/add")
    public RetMsgData<SysNavigation> add(@RequestBody SysNavigation navigation){
        RetMsgData<SysNavigation> msgData = new RetMsgData<>();

        navigation.setGmtCreate(LocalDateTime.now());

        try{
            iNavigationService.save(navigation);
            msgData.setData(navigation);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<SysNavigation> deleteById(@RequestParam Integer id){
        RetMsgData<SysNavigation> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            iBookmarkService.deleteByParentId(id);
            iNavigationService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getAll")
    public RetMsgData<List<SysNavigation>> getAll(){
        RetMsgData<List<SysNavigation>> msgData = new RetMsgData<>();
        QueryWrapper<SysNavigation> queryWrapper = new QueryWrapper<>();
        try{
            List<SysNavigation> navigations = iNavigationService.getNavigations(queryWrapper);
            msgData.setData(navigations);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<SysNavigation> getById(@RequestParam Integer id){
        RetMsgData<SysNavigation> msgData = new RetMsgData<>();
        try{
            SysNavigation navigation = iNavigationService.getById(id);
            msgData.setData(navigation);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<SysNavigation>> getPageIcom(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required=false)String name){
        RetMsgData<IPage<SysNavigation>> msgData = new RetMsgData<>();
        Page<SysNavigation> navigationPage = new Page<>(page, size);
        QueryWrapper<SysNavigation> navigationQueryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(name)){
            navigationQueryWrapper.like("name",name);
        }
        try{
            IPage<SysNavigation> page1 = iNavigationService.page(navigationPage, navigationQueryWrapper);
            for (SysNavigation navigation:page1.getRecords()
            ) {
                SysIcom icom = iIcomService.getById(navigation.getIcomId());
                navigation.setIcomName(icom.getName());
            }
            msgData.setData(page1);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<SysNavigation>> update(@RequestBody SysNavigation navigation){
        RetMsgData<IPage<SysNavigation>> msgData = new RetMsgData<>();

        SysNavigation byId = iNavigationService.getById(navigation.getId());
        if(VerdictUtil.isNull(byId)){
            msgData.setMsg("找不到要修改的信息");
            return msgData;
        }

        UpdateWrapper<SysNavigation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",navigation.getId());

        navigation.setGmtModified(LocalDateTime.now());

        try{
            iNavigationService.update(navigation,updateWrapper);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }

    }

}

