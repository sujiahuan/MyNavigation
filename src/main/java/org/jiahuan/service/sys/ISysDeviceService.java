package org.jiahuan.service.sys;

import org.jiahuan.entity.sys.SysDevice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
public interface ISysDeviceService extends IService<SysDevice> {

    void addInitCounDevice(SysDevice counDvice) throws IOException;

    void updateCounDevice(SysDevice sysDevice) throws IOException;

    void deleteInitById(Integer deviceId);
}
