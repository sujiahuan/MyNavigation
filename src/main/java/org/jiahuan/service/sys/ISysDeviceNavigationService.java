package org.jiahuan.service.sys;

import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.entity.sys.SysDeviceNavigation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jh
 * @since 2021-05-24
 */
public interface ISysDeviceNavigationService extends IService<SysDeviceNavigation> {

    void udpate(SysDevice sysDevice);

    void deleteByDeviceId(Integer deviceId);
}
