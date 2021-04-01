package org.jiahuan.mapper.analog;

import org.apache.ibatis.annotations.Mapper;
import org.jiahuan.entity.analog.AnalogDynamicDivisor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jh
 * @since 2020-08-05
 */
@Mapper
public interface AnalogDynamicDivisorMapper extends BaseMapper<AnalogDynamicDivisor> {

    AnalogDynamicDivisor getDynamicDivisorByDeviceId(Integer deviceId);

    AnalogDynamicDivisor getDynamicDivisorById(Integer id);


}
