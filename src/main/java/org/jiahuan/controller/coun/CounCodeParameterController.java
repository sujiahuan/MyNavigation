package org.jiahuan.controller.coun;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.coun.CounCodeParameter;
import org.jiahuan.service.coun.ICounCodeParameterService;
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
public class CounCodeParameterController {

    @Autowired
    private ICounCodeParameterService iCounCodeParameterService;

    @PostMapping("/add")
    public RetMsgData<CounCodeParameter> add(@RequestBody CounCodeParameter obj){
        RetMsgData<CounCodeParameter> msgData = new RetMsgData<>();

//        if(VerdictUtil.isNull(CounParameter.getName())){
//            msgData.setMsg("name为空");
//        }

        obj.setGmtCreate(LocalDateTime.now());

        try{
            iCounCodeParameterService.save(obj);
            msgData.setData(obj);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("新增失败：{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<CounCodeParameter> delete(@RequestParam Integer id){
        RetMsgData<CounCodeParameter> msgData = new RetMsgData<>();
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
            iCounCodeParameterService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("删除失败：{}",e);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<CounCodeParameter> get(@RequestParam Integer id){
        RetMsgData<CounCodeParameter> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            msgData.setData(iCounCodeParameterService.getById(id));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getAll")
    public RetMsgData<List<CounCodeParameter>> getAll(){
        RetMsgData<List<CounCodeParameter>> msgData = new RetMsgData<>();
        QueryWrapper<CounCodeParameter> queryWrapper = new QueryWrapper<>();
        try{
            msgData.setData(iCounCodeParameterService.list(queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<CounCodeParameter>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required=false)String name){
        RetMsgData<IPage<CounCodeParameter>> msgData = new RetMsgData<>();
        Page<CounCodeParameter> page1 = new Page<>(page, size);
        QueryWrapper<CounCodeParameter> queryWrapper = new QueryWrapper<>();
        if(VerdictUtil.isNotNull(name)){
            queryWrapper.like("name",name);
        }
        try{
            msgData.setData(iCounCodeParameterService.page(page1, queryWrapper));
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<CounCodeParameter>> update(@RequestBody CounCodeParameter obj){
        RetMsgData<IPage<CounCodeParameter>> msgData = new RetMsgData<>();

        CounCodeParameter byId = iCounCodeParameterService.getById(obj.getId());
        if(VerdictUtil.isNull(byId)){
            msgData.setMsg("找不到要修改的信息");
            return msgData;
        }

//        if(VerdictUtil.isNull(obj.getName())){
//            msgData.setMsg("name为空");
//            return msgData;
//        }
        UpdateWrapper<CounCodeParameter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",obj.getId());

        obj.setGmtModified(LocalDateTime.now());

        try{
            iCounCodeParameterService.update(obj,updateWrapper);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

}

