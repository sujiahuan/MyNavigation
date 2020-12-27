package org.jiahuan.controller.analog;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.analog.AnalogCodeParameter;
import org.jiahuan.service.analog.IAnalogCodeParameterService;
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
@RequestMapping("/counParameter")
@Slf4j
public class AnalogCodeParameterController {

    @Autowired
    private IAnalogCodeParameterService iAnalogCodeParameterService;

    @PostMapping("/add")
    public RetMsgData<AnalogCodeParameter> add(@RequestBody AnalogCodeParameter obj){
        RetMsgData<AnalogCodeParameter> msgData = new RetMsgData<>();

        obj.setGmtCreate(LocalDateTime.now());

        try{
            iAnalogCodeParameterService.save(obj);
            msgData.setData(obj);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("新增失败：{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<AnalogCodeParameter> delete(@RequestParam Integer id){
        RetMsgData<AnalogCodeParameter> msgData = new RetMsgData<>();
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
            iAnalogCodeParameterService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("删除失败：{}",e);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<AnalogCodeParameter> get(@RequestParam Integer id){
        RetMsgData<AnalogCodeParameter> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            msgData.setData(iAnalogCodeParameterService.getById(id));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getAll")
    public RetMsgData<List<AnalogCodeParameter>> getAll(){
        RetMsgData<List<AnalogCodeParameter>> msgData = new RetMsgData<>();
        QueryWrapper<AnalogCodeParameter> queryWrapper = new QueryWrapper<>();
        try{
            msgData.setData(iAnalogCodeParameterService.list(queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<AnalogCodeParameter>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required=false)String name){
        RetMsgData<IPage<AnalogCodeParameter>> msgData = new RetMsgData<>();
        Page<AnalogCodeParameter> page1 = new Page<>(page, size);
        QueryWrapper<AnalogCodeParameter> queryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(name)){
            queryWrapper.like("name",name);
        }
        try{
            msgData.setData(iAnalogCodeParameterService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<AnalogCodeParameter>> update(@RequestBody AnalogCodeParameter obj){
        RetMsgData<IPage<AnalogCodeParameter>> msgData = new RetMsgData<>();

        AnalogCodeParameter byId = iAnalogCodeParameterService.getById(obj.getId());
        if(VerdictUtil.isNull(byId)){
            msgData.setMsg("找不到要修改的信息");
            return msgData;
        }

//        if(VerdictUtil.isNull(obj.getName())){
//            msgData.setMsg("name为空");
//            return msgData;
//        }
        UpdateWrapper<AnalogCodeParameter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",obj.getId());

        obj.setGmtModified(LocalDateTime.now());

        try{
            iAnalogCodeParameterService.update(obj,updateWrapper);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

}

