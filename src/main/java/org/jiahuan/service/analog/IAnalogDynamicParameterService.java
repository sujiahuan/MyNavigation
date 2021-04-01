package org.jiahuan.service.analog;

import org.jiahuan.entity.analog.AnalogDynamicParameter;
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
public interface IAnalogDynamicParameterService extends IService<AnalogDynamicParameter> {

    AnalogDynamicParameter getDynamicParameterById(Integer id);

    List<AnalogDynamicParameter> getDynamicParameterByDeviceId(Integer deviceId,Integer type);

    void deleteByDeviceId(Integer deviceId);

}
