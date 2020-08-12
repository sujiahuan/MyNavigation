package org.jiahuan.controller.coun;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.model.State;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.coun.CounParameter;
import org.jiahuan.service.coun.ICounParameterService;
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
@RequestMapping("/counParameter")
@Slf4j
public class CounParameterController {

    @Autowired
    private ICounParameterService iCounParameterService;

    @PostMapping("/add")
    public RetMsgData<CounParameter> add(@RequestBody CounParameter obj){
        RetMsgData<CounParameter> msgData = new RetMsgData<>();

//        if(VerdictUtil.isNull(CounParameter.getName())){
//            msgData.setMsg("name为空");
//        }

        obj.setGmtCreate(LocalDateTime.now());

        try{
            iCounParameterService.save(obj);
            msgData.setData(obj);
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("新增失败：{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<CounParameter> delete(@RequestParam Integer id){
        RetMsgData<CounParameter> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }

//        QueryWrapper<CounParameter> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("icom_id",id);
//        List<CounParameter> navigations = iCounParameterService.getNavigations(queryWrapper);
//        if(navigations.size()!=0){
//            msgData.setMsg("该图标已关联侧边栏，请删除关联");
//            return msgData;
//        }

        try{
            iCounParameterService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("删除失败：{}",e);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<CounParameter> get(@RequestParam Integer id){
        RetMsgData<CounParameter> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            msgData.setData(iCounParameterService.getById(id));
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getAll")
    public RetMsgData<List<CounParameter>> getAll(){
        RetMsgData<List<CounParameter>> msgData = new RetMsgData<>();
        QueryWrapper<CounParameter> queryWrapper = new QueryWrapper<>();
        try{
            msgData.setData(iCounParameterService.list(queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<CounParameter>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required=false)String name){
        RetMsgData<IPage<CounParameter>> msgData = new RetMsgData<>();
        Page<CounParameter> page1 = new Page<>(page, size);
        QueryWrapper<CounParameter> queryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(name)){
            queryWrapper.like("name",name);
        }
        try{
            msgData.setData(iCounParameterService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<CounParameter>> update(@RequestBody CounParameter obj){
        RetMsgData<IPage<CounParameter>> msgData = new RetMsgData<>();

        CounParameter byId = iCounParameterService.getById(obj.getId());
        if(VerdictUtil.isNull(byId)){
            msgData.setMsg("找不到要修改的信息");
            return msgData;
        }

//        if(VerdictUtil.isNull(obj.getName())){
//            msgData.setMsg("name为空");
//            return msgData;
//        }
        UpdateWrapper<CounParameter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",obj.getId());

        obj.setGmtModified(LocalDateTime.now());

        try{
            iCounParameterService.update(obj,updateWrapper);
            return msgData;
        }catch (Exception e){
            msgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return msgData;
        }
    }

}

