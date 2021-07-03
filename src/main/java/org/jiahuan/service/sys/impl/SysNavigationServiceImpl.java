package org.jiahuan.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.entity.sys.SysDeviceNavigation;
import org.jiahuan.entity.sys.SysNavigation;
import org.jiahuan.mapper.sys.SysNavigationMapper;
import org.jiahuan.service.sys.ISysBookmarkService;
import org.jiahuan.service.sys.ISysDeviceNavigationService;
import org.jiahuan.service.sys.ISysIcomService;
import org.jiahuan.service.sys.ISysNavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wj
 * @since 2020-06-16
 */
@Service
public class SysNavigationServiceImpl extends ServiceImpl<SysNavigationMapper, SysNavigation> implements ISysNavigationService {

    @Autowired
    private ISysIcomService iIcomService;
    @Autowired
    private ISysBookmarkService iBookmarkService;
    @Autowired
    private ISysDeviceNavigationService iSysDeviceNavigationService;
    @Resource
    private SysNavigationMapper sysNavigationMapper;

    @Override
    public List<SysNavigation> getByDeviceId(Integer deviceId) {
        List<SysNavigation> sysNavigations = sysNavigationMapper.getByDeviceId(deviceId);
        return sysNavigations;
    }

    @Override
    public List<SysNavigation> getNavigations(QueryWrapper<SysNavigation> queryWrapper) {
        List<SysNavigation> navigations = this.list(queryWrapper);
        for (SysNavigation navigation : navigations
        ) {
            if (navigation.getType() != 1) {
                break;
            }
            String name = iIcomService.getById(navigation.getIcomId()).getName();
            navigation.setIcomName(name);
        }
        return navigations;
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        QueryWrapper<SysDeviceNavigation> wrapper = new QueryWrapper<>();
        wrapper.eq("navigation_id", id);
        List<SysDeviceNavigation> list = iSysDeviceNavigationService.list(wrapper);
        if(list.size()!=0){
            throw new Exception("已有设备在使用，请移除后再删除");
        }

        iBookmarkService.deleteByParentId(id);
        this.removeById(id);
    }
}
