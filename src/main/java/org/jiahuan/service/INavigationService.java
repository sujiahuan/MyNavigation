package org.jiahuan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.Navigation;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2020-06-16
 */
public interface INavigationService extends IService<Navigation> {

    List<Navigation> getNavigations(QueryWrapper<Navigation> queryWrapper);
}
