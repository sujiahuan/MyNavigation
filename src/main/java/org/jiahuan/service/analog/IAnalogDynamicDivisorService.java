package org.jiahuan.service.analog;

import org.jiahuan.entity.analog.AnalogDynamicDivisor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jh
 * @since 2020-08-05
 */
public interface IAnalogDynamicDivisorService extends IService<AnalogDynamicDivisor> {

    AnalogDynamicDivisor getDynamicDivisorByDeviceId(Integer deviceId);

    AnalogDynamicDivisor getDynamicDivisorById(Integer id);

    void deleteDynamicDivisorByDeviceId(Integer deviceId);
}
