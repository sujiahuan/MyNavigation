package org.jiahuan.service.analog;

import org.jiahuan.entity.analog.AnDynamicParameter;
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
public interface IAnDynamicParameterService extends IService<AnDynamicParameter> {

    AnDynamicParameter getDynamicParameterById(Integer id);

    List<AnDynamicParameter> getDynamicParameterByDeviceId(Integer deviceId, Integer type);

    void deleteByDeviceId(Integer deviceId);

}
