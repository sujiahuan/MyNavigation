package org.jiahuan.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.entity.sys.SysDeviceNavigation;
import org.jiahuan.entity.sys.SysNavigation;
import org.jiahuan.mapper.sys.SysDeviceNavigationMapper;
import org.jiahuan.service.sys.ISysDeviceNavigationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jh
 * @since 2021-05-24
 */
@Service
public class SysDeviceNavigationServiceImpl extends ServiceImpl<SysDeviceNavigationMapper, SysDeviceNavigation> implements ISysDeviceNavigationService {



    @Override
    public void udpate(SysDevice sysDevice) {
        this.deleteByDeviceId(sysDevice.getId());

        List<SysDeviceNavigation> sysDeviceNavigations=new ArrayList<>();

        String[] sysNavigations = sysDevice.getSysNavigations().split(",");

        if(sysNavigations.length==0){
            return;
        }

        for (String sysNavigation : sysNavigations) {
            sysDeviceNavigations.add(new SysDeviceNavigation(null, sysDevice.getId(), Integer.valueOf(sysNavigation), LocalDateTime.now(), null));
        }

        this.saveBatch(sysDeviceNavigations);
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<SysDeviceNavigation> wrapper = new QueryWrapper<>();
        wrapper.eq("device_id", deviceId);
        this.remove(wrapper);
    }

}
