package org.jiahuan.controller.sys;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.model.State;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.sys.SysBookmark;
import org.jiahuan.service.sys.ISysBookmarkService;
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
@RequestMapping("/bookmark")
@Slf4j
public class SysBookmarkController {

    @Autowired
    private ISysBookmarkService iBookmarkService;

        @PostMapping("/add")
    public RetMsgData<SysBookmark> addBookmark(@RequestBody SysBookmark bookmark){
        RetMsgData<SysBookmark> msgData = new RetMsgData<>();

        if(VerdictUtil.isNull(bookmark.getParentId())){
            msgData.setMsg("ParentId为空");
            return msgData;
        }

        bookmark.setGmtCreate(LocalDateTime.now());

        try{
            iBookmarkService.save(bookmark);
            msgData.setData(bookmark);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<SysBookmark> deleteBookmar(@RequestParam Integer id){
        RetMsgData<SysBookmark> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            iBookmarkService.removeById(id);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<SysBookmark> getById(@RequestParam Integer id){
        RetMsgData<SysBookmark> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            msgData.setMsg("id为空");
        }
        try{
            SysBookmark bookmarks = iBookmarkService.getById(id);
            msgData.setData(bookmarks);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getBookmark")
    public RetMsgData<List<SysBookmark>> getBookmark(@RequestParam Integer parenId, @RequestParam(required=false)String title){
        RetMsgData<List<SysBookmark>> msgData = new RetMsgData<>();
        if(VerdictUtil.isNull(parenId)){
            msgData.setMsg("id为空");
        }
        QueryWrapper<SysBookmark> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parenId);
        if(VerdictUtil.isNotNull(title)){
        queryWrapper.like("title","%"+title+"%");
        }
        try{
            List<SysBookmark> bookmarks = iBookmarkService.list(queryWrapper);
            msgData.setData(bookmarks);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<SysBookmark>> getPageBookmark(@RequestParam Integer page, @RequestParam Integer size){
        RetMsgData<IPage<SysBookmark>> msgData = new RetMsgData<>();
        Page<SysBookmark> bookmarkPage = new Page<>(page, size);
        QueryWrapper<SysBookmark> bookmarkQueryWrapper = new QueryWrapper<>();
        IPage<SysBookmark> page1 = iBookmarkService.page(bookmarkPage, bookmarkQueryWrapper);
        try{
            msgData.setData(page1);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<SysBookmark>> updateBookmark(@RequestBody SysBookmark bookmark){
        RetMsgData<IPage<SysBookmark>> msgData = new RetMsgData<>();

        SysBookmark byId = iBookmarkService.getById(bookmark.getId());
        if(VerdictUtil.isNull(byId)){
            msgData.setMsg("找不到要修改的信息");
            return msgData;
        }

        UpdateWrapper<SysBookmark> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",bookmark.getId());

        bookmark.setGmtModified(LocalDateTime.now());

        try{
            iBookmarkService.update(bookmark,updateWrapper);
            return msgData;
        }catch (Exception e){
            msgData.setMsg(e.getMessage());
            log.error("{}",e);
            return msgData;
        }

    }

}

