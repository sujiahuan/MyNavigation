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
@RequestMapping("/icom")
@Slf4j
public class IcomController {

    @Autowired
    private IIcomService iIcomService;
    @Autowired
    private INavigationService iNavigationService;

    @PostMapping("/add")
    public RetMsgData<Icom> addIcom(@RequestBody Icom icom){
        RetMsgData<Icom> icomRetMsgData = new RetMsgData<>();
        if(VerdictUtil.isNull(icom.getName())){
            icomRetMsgData.setMsg("name为空");
        }

        icom.setGmtCreate(LocalDateTime.now());

        try{
            iIcomService.save(icom);
            icomRetMsgData.setData(icom);
            return icomRetMsgData;
        }catch (Exception e){
            icomRetMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("新增Icom失败：{}",e);
            return icomRetMsgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<Icom> deleteIcom(@RequestParam Integer id){
        RetMsgData<Icom> icomRetMsgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            icomRetMsgData.setMsg("id为空");
        }
        QueryWrapper<Navigation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("icom_id",id);
        List<Navigation> navigations = iNavigationService.getNavigations(queryWrapper);
        if(navigations.size()!=0){
            icomRetMsgData.setMsg("该图标已关联侧边栏，请删除关联");
            return icomRetMsgData;
        }
        try{
            iIcomService.removeById(id);
            return icomRetMsgData;
        }catch (Exception e){
            icomRetMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("删除Icom失败：{}",e);
            return icomRetMsgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<Icom> getIcom(@RequestParam Integer id){
        RetMsgData<Icom> icomRetMsgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            icomRetMsgData.setMsg("id为空");
        }
        try{
            icomRetMsgData.setData(iIcomService.getById(id));
            return icomRetMsgData;
        }catch (Exception e){
            icomRetMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return icomRetMsgData;
        }
    }

    @GetMapping("/getAll")
    public RetMsgData<List<Icom>> getAll(){
        RetMsgData<List<Icom>> icomRetMsgData = new RetMsgData<>();
        QueryWrapper<Icom> icomQueryWrapper = new QueryWrapper<>();
        try{
            icomRetMsgData.setData(iIcomService.list(icomQueryWrapper));
            return icomRetMsgData;
        }catch (Exception e){
            icomRetMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return icomRetMsgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<Icom>> getPageIcom(@RequestParam Integer page,@RequestParam Integer size,@RequestParam(required=false)String name){
        RetMsgData<IPage<Icom>> icomRetMsgData = new RetMsgData<>();
        Page<Icom> icomPage = new Page<>(page, size);
        QueryWrapper<Icom> IcomQueryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(name)){
            IcomQueryWrapper.like("name",name);
        }
        try{
            IPage<Icom> page1 = iIcomService.page(icomPage, IcomQueryWrapper);
            icomRetMsgData.setData(page1);
            return icomRetMsgData;
        }catch (Exception e){
            icomRetMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return icomRetMsgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<Icom>> updateIcom(@RequestBody Icom icom){
        RetMsgData<IPage<Icom>> icomRetMsgData = new RetMsgData<>();

        Icom byId = iIcomService.getById(icom.getId());
        if(VerdictUtil.isNull(byId)){
            icomRetMsgData.setMsg("找不到要修改的Icom信息");
            return icomRetMsgData;
        }

        if(VerdictUtil.isNull(icom.getName())){
            icomRetMsgData.setMsg("name为空");
            return icomRetMsgData;
        }
        UpdateWrapper<Icom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",icom.getId());

        icom.setGmtModified(LocalDateTime.now());

        try{
            iIcomService.update(icom,updateWrapper);
            return icomRetMsgData;
        }catch (Exception e){
            icomRetMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return icomRetMsgData;
        }

    }

}

