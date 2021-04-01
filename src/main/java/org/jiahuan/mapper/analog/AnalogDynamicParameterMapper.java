package org.jiahuan.mapper.analog;

import org.jiahuan.entity.analog.AnalogDynamicParameter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
public interface AnalogDynamicParameterMapper extends BaseMapper<AnalogDynamicParameter> {

    AnalogDynamicParameter getDynamicParameterById(Integer id);

    /**
     * 通过设备获取动态因子
     * @param deviceId
     * @param type 类型：0全部/1参数/2状态
     * @return
     */
    List<AnalogDynamicParameter> getDynamicParameterByDeviceId(Integer deviceId, Integer type);

}
