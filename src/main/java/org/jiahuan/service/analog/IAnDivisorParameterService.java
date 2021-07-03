package org.jiahuan.service.analog;

import org.jiahuan.entity.analog.AnDivisorParameter;
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
public interface IAnDivisorParameterService extends IService<AnDivisorParameter> {

    List<AnDivisorParameter> getDivisorParameterByDeviceId(Integer deviceId);

    void deleteByDeviceId(Integer deviceId);
}
