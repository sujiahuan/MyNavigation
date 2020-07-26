package org.jiahuan.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.sys.SysNavigation;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2020-06-16
 */
public interface ISysNavigationService extends IService<SysNavigation> {

    List<SysNavigation> getNavigations(QueryWrapper<SysNavigation> queryWrapper);
}
