package org.jiahuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.entity.Navigation;
import org.jiahuan.mapper.NavigationMapper;
import org.jiahuan.service.IIcomService;
import org.jiahuan.service.INavigationService;
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
public class NavigationServiceImpl extends ServiceImpl<NavigationMapper, Navigation> implements INavigationService {

    @Autowired
    private INavigationService iNavigationService;
    @Autowired
    private IIcomService iIcomService;

    @Override
    public List<Navigation> getNavigations(QueryWrapper<Navigation> queryWrapper){
        List<Navigation> navigations = iNavigationService.list(queryWrapper);
        for (Navigation navigation:navigations
        ) {
            String name = iIcomService.getById(navigation.getIcomId()).getName();
            navigation.setIcomName(name);
        }
        return navigations;
    }
}
