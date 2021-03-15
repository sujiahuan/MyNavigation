package org.jiahuan.service.analog;

import org.jiahuan.entity.analog.AnalogCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jh
 * @since 2020-08-05
 */
public interface IAnalogCodeService extends IService<AnalogCode> {

    AnalogCode getAnalogCodeByDeviceId(Integer deviceId);

    AnalogCode getAnalogCodeByDivisorName(String divisorName);

    void deleteAnalogCodeByDeviceId(Integer deviceId);
}
