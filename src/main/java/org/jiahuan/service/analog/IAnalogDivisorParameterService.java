package org.jiahuan.service.analog;

import org.jiahuan.entity.analog.AnalogDivisorParameter;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
public interface IAnalogDivisorParameterService extends IService<AnalogDivisorParameter> {

    List<AnalogDivisorParameter> getCounDivisorByDeviceId(Integer deviceId);

    void deleteByDeviceId(Integer deviceId);
}
