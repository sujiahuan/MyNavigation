package org.jiahuan.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.sys.SysDevice;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
public interface ISysDeviceService extends IService<SysDevice> {

    void addInitCounDevice(SysDevice counDvice);

    void updateCounDevice(SysDevice sysDevice) ;

    void deleteInitById(Integer deviceId);
}
