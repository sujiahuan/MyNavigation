package org.jiahuan.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.sys.SysBookmark;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2020-06-16
 */
public interface ISysBookmarkService extends IService<SysBookmark> {


    void deleteByParentId(Integer parentId);

}
