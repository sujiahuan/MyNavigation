package org.jiahuan.service.coun;

import org.jiahuan.entity.coun.CounDataType;
import org.jiahuan.entity.coun.CounDevice;
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
public interface ICounDeviceService extends IService<CounDevice> {

    void addInitCounDevice(CounDevice counDvice) throws IOException;

    void updateCounDevice(CounDevice counDevice) throws IOException;

    void deleteInitById(Integer deviceId);
}
