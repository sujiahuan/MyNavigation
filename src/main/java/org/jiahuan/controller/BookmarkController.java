package org.jiahuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.model.RetMsgData;
import org.jiahuan.common.model.State;
import org.jiahuan.common.util.VerdictUtil;
import org.jiahuan.entity.Bookmark;
import org.jiahuan.service.IBookmarkService;
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
public class BookmarkController {

    @Autowired
    private IBookmarkService iBookmarkService;

        @PostMapping("/add")
    public RetMsgData<Bookmark> addBookmark(@RequestBody Bookmark bookmark){
        RetMsgData<Bookmark> retMsgData = new RetMsgData<>();

        if(VerdictUtil.isNull(bookmark.getParentId())){
            retMsgData.setMsg("ParentId为空");
            return retMsgData;
        }

        bookmark.setGmtCreate(LocalDateTime.now());

        try{
            iBookmarkService.save(bookmark);
            retMsgData.setData(bookmark);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return retMsgData;
        }
    }

    @DeleteMapping("/deleteById")
    public RetMsgData<Bookmark> deleteBookmar(@RequestParam Integer id){
        RetMsgData<Bookmark> retMsgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            retMsgData.setMsg("id为空");
        }
        try{
            iBookmarkService.removeById(id);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return retMsgData;
        }
    }

    @GetMapping("/getById")
    public RetMsgData<Bookmark> getById(@RequestParam Integer id){
        RetMsgData<Bookmark> retMsgData = new RetMsgData<>();
        if(VerdictUtil.isNull(id)){
            retMsgData.setMsg("id为空");
        }
        try{
            Bookmark bookmarks = iBookmarkService.getById(id);
            retMsgData.setData(bookmarks);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return retMsgData;
        }
    }

    @GetMapping("/getBookmark")
    public RetMsgData<List<Bookmark>> getBookmark(@RequestParam Integer parenId,@RequestParam(required=false)String title){
        RetMsgData<List<Bookmark>> retMsgData = new RetMsgData<>();
        if(VerdictUtil.isNull(parenId)){
            retMsgData.setMsg("id为空");
        }
        QueryWrapper<Bookmark> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parenId);
        if(VerdictUtil.isNotNull(title)){
        queryWrapper.like("title","%"+title+"%");
        }
        try{
            List<Bookmark> bookmarks = iBookmarkService.list(queryWrapper);
            retMsgData.setData(bookmarks);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return retMsgData;
        }
    }

    @GetMapping("/getPage")
    public RetMsgData<IPage<Bookmark>> getPageBookmark(@RequestParam Integer page,@RequestParam Integer size){
        RetMsgData<IPage<Bookmark>> BookmarkRetMsgData = new RetMsgData<>();
        Page<Bookmark> bookmarkPage = new Page<>(page, size);
        QueryWrapper<Bookmark> bookmarkQueryWrapper = new QueryWrapper<>();
        IPage<Bookmark> page1 = iBookmarkService.page(bookmarkPage, bookmarkQueryWrapper);
        try{
            BookmarkRetMsgData.setData(page1);
            return BookmarkRetMsgData;
        }catch (Exception e){
            BookmarkRetMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return BookmarkRetMsgData;
        }
    }

    @PostMapping("/update")
    public RetMsgData<IPage<Bookmark>> updateBookmark(@RequestBody Bookmark bookmark){
        RetMsgData<IPage<Bookmark>> retMsgData = new RetMsgData<>();

        Bookmark byId = iBookmarkService.getById(bookmark.getId());
        if(VerdictUtil.isNull(byId)){
            retMsgData.setMsg("找不到要修改的信息");
            return retMsgData;
        }

        UpdateWrapper<Bookmark> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",bookmark.getId());

        bookmark.setGmtModified(LocalDateTime.now());

        try{
            iBookmarkService.update(bookmark,updateWrapper);
            return retMsgData;
        }catch (Exception e){
            retMsgData.setState(State.RET_STATE_SYSTEM_ERROR);
            log.error("{}",e);
            return retMsgData;
        }

    }

}

