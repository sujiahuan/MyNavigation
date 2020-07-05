package org.jiahuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.Bookmark;
import org.jiahuan.mapper.BookmarkMapper;
import org.jiahuan.service.IBookmarkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wj
 * @since 2020-06-16
 */
@Service
public class BookmarkServiceImpl extends ServiceImpl<BookmarkMapper, Bookmark> implements IBookmarkService {

    @Autowired
    private IBookmarkService iBookmarkService;

    @Override
    public void deleteByParentId(Integer parentId) {
        QueryWrapper<Bookmark> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId);
        List<Bookmark> bookmarks = iBookmarkService.list(queryWrapper);

        for (Bookmark bookmark:bookmarks
             ) {
            iBookmarkService.removeById(bookmark.getId());
        }

    }

}
