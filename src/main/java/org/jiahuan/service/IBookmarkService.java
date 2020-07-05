package org.jiahuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.Bookmark;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2020-06-16
 */
public interface IBookmarkService extends IService<Bookmark> {


    void deleteByParentId(Integer parentId);

}
