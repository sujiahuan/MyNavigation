package org.jiahuan.service.analog;

import org.jiahuan.entity.analog.AnDynamicDivisor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jh
 * @since 2020-08-05
 */
public interface IAnDynamicDivisorService extends IService<AnDynamicDivisor> {

    AnDynamicDivisor getDynamicDivisorByDeviceId(Integer deviceId);

    AnDynamicDivisor getDynamicDivisorById(Integer id);

    void deleteDynamicDivisorByDeviceId(Integer deviceId);
}
