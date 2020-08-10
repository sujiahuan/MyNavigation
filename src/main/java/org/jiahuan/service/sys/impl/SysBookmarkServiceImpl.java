package org.jiahuan.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.sys.SysBookmark;
import org.jiahuan.mapper.sys.SysBookmarkMapper;
import org.jiahuan.service.sys.ISysBookmarkService;
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
public class SysBookmarkServiceImpl extends ServiceImpl<SysBookmarkMapper, SysBookmark> implements ISysBookmarkService {

    @Autowired
    private ISysBookmarkService iBookmarkService;

    @Override
    public void deleteByParentId(Integer parentId) {
        QueryWrapper<SysBookmark> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId);
        List<SysBookmark> bookmarks = iBookmarkService.list(queryWrapper);

        for (SysBookmark bookmark:bookmarks
             ) {
            iBookmarkService.removeById(bookmark.getId());
        }

    }

}
