package org.jiahuan.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.entity.sys.SysNavigation;
import org.jiahuan.mapper.sys.SysNavigationMapper;
import org.jiahuan.service.sys.ISysIcomService;
import org.jiahuan.service.sys.ISysNavigationService;
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
public class SysNavigationServiceImpl extends ServiceImpl<SysNavigationMapper, SysNavigation> implements ISysNavigationService {

    @Autowired
    private ISysNavigationService iNavigationService;
    @Autowired
    private ISysIcomService iIcomService;

    @Override
    public List<SysNavigation> getNavigations(QueryWrapper<SysNavigation> queryWrapper){
        List<SysNavigation> navigations = iNavigationService.list(queryWrapper);
        for (SysNavigation navigation:navigations
        ) {
            String name = iIcomService.getById(navigation.getIcomId()).getName();
            navigation.setIcomName(name);
        }
        return navigations;
    }
}
