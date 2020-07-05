package org.jiahuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.model.State;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.Icom;
import org.jiahuan.entity.Navigation;
import org.jiahuan.service.IBookmarkService;
import org.jiahuan.service.IIcomService;
import org.jiahuan.service.INavigationService;
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
public class NavigationController {

    @Autowired
    private INavigationService iNavigationService;
    @Autowired
    private IBookmarkService iBookmarkService;
    @Autowired
    private IIcomService iIcomService;

    @PostMapping("/add")
    public RetMsgData<Navigation> add(@RequestBody Navigation navigation){
        RetMsgData<Navigation> retMsgData = new RetMsgData<>();

        navigation.setGmtCreate(LocalDateTime.now());

        try{
            iNavigationService.save(navigation);
            retMsgData.setData(navigation);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return retMsgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<Navigation> deleteById(@RequestParam Integer id){
        RetMsgData<Navigation> retMsgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            retMsgData.setMsg("id为空");
        }
        try{
            iBookmarkService.deleteByParentId(id);
            iNavigationService.removeById(id);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return retMsgData;
        }
    }

    @GetMapping("/getAll")
    public RetMsgData<List<Navigation>> getAll(){
        RetMsgData<List<Navigation>> retMsgData = new RetMsgData<>();
        QueryWrapper<Navigation> queryWrapper = new QueryWrapper<>();
        try{
            List<Navigation> navigations = iNavigationService.getNavigations(queryWrapper);
            retMsgData.setData(navigations);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return retMsgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<Navigation> getById(@RequestParam Integer id){
        RetMsgData<Navigation> retMsgData = new RetMsgData<>();
        try{
            Navigation navigation = iNavigationService.getById(id);
            retMsgData.setData(navigation);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return retMsgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<Navigation>> getPageIcom(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required=false)String name){
        RetMsgData<IPage<Navigation>> pageRetMsgData = new RetMsgData<>();
        Page<Navigation> navigationPage = new Page<>(page, size);
        QueryWrapper<Navigation> navigationQueryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(name)){
            navigationQueryWrapper.like("name",name);
        }
        try{
            IPage<Navigation> page1 = iNavigationService.page(navigationPage, navigationQueryWrapper);
            for (Navigation navigation:page1.getRecords()
            ) {
                Icom icom = iIcomService.getById(navigation.getIcomId());
                navigation.setIcomName(icom.getName());
            }
            pageRetMsgData.setData(page1);
            return pageRetMsgData;
        }catch (Exception e){
            pageRetMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return pageRetMsgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<Navigation>> update(@RequestBody Navigation navigation){
        RetMsgData<IPage<Navigation>> retMsgData = new RetMsgData<>();

        Navigation byId = iNavigationService.getById(navigation.getId());
        if(VerdictUtil.isNull(byId)){
            retMsgData.setMsg("找不到要修改的信息");
            return retMsgData;
        }

        UpdateWrapper<Navigation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",navigation.getId());

        navigation.setGmtModified(LocalDateTime.now());

        try{
            iNavigationService.update(navigation,updateWrapper);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return retMsgData;
        }

    }

}

