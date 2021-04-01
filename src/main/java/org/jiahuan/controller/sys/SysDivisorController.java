package org.jiahuan.controller.sys;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.sys.SysDivisor;
import org.jiahuan.service.sys.ISysDivisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * v
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jh
 * @since 2020-09-29
 */
@RestController
@RequestMapping("/sysCode")
public class SysDivisorController {

    @Autowired
    private ISysDivisorService iSysDivisorService;

    @PostMapping("/add")
    public RetMsgData<SysDivisor> addSysCode(@RequestBody SysDivisor sysCode) {
        RetMsgData<SysDivisor> msgData = new RetMsgData<>();
        if (VerdictUtil.isNull(sysCode.getName())) {
            msgData.setMsg("名称不能为空");
            return msgData;
        }
        if (VerdictUtil.isNull(sysCode.getCode())) {
            msgData.setMsg("Code不能为空");
            return msgData;
        }

        QueryWrapper<SysDivisor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", sysCode.getName());
        queryWrapper.eq("code", sysCode.getCode());
        if(iSysDivisorService.list(queryWrapper).size()>0 ){
            msgData.setMsg("该因子已存在");
            return msgData;
        }

        sysCode.setGmtCreate(LocalDateTime.now());

        try {
            iSysDivisorService.save(sysCode);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<SysDivisor> deleteById(@RequestParam Integer id) {
        RetMsgData<SysDivisor> msgData = new RetMsgData<>();

        try {
            iSysDivisorService.deleteById(id);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<SysDivisor> getById(@RequestParam Integer id) {
        RetMsgData<SysDivisor> msgData = new RetMsgData<>();
        try {
            msgData.setData(iSysDivisorService.getById(id));
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getAll")
    public RetMsgData<List<SysDivisor>> getAll() {
        RetMsgData<List<SysDivisor>> msgData = new RetMsgData<>();
        QueryWrapper<SysDivisor> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        try {
            msgData.setData(iSysDivisorService.list(queryWrapper));
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<SysDivisor>> getPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String code, @RequestParam String name,@RequestParam Integer type) {
        RetMsgData<IPage<SysDivisor>> msgData = new RetMsgData<>();
        Page<SysDivisor> page1 = new Page<>(page, size);
        QueryWrapper<SysDivisor> queryWrapper = new QueryWrapper<>();
        if (VerdictUtil.isNotNull(code)) {
            queryWrapper.eq("code", code);
        }
        if (VerdictUtil.isNotNull(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.eq("type", type);
        queryWrapper.orderByDesc("gmt_create");
        try {
            msgData.setData(iSysDivisorService.page(page1, queryWrapper));
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<SysDivisor>> update(@RequestBody SysDivisor sysCode) {
        RetMsgData<IPage<SysDivisor>> msgData = new RetMsgData<>();

        QueryWrapper<SysDivisor> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("id", sysCode.getId());
        queryWrapper.eq("name", sysCode.getName());
        queryWrapper.eq("code", sysCode.getCode());
        if(iSysDivisorService.list(queryWrapper).size()>0 ){
            msgData.setMsg("该因子已存在");
            return msgData;
        }

        sysCode.setGmtModified(LocalDateTime.now());

        try {
            iSysDivisorService.updateById(sysCode);
            return msgData;
        } catch (Exception e) {
            msgData.setMsg(e.getMessage());
            return msgData;
        }
    }

}

