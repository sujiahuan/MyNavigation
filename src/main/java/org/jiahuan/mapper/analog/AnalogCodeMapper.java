package org.jiahuan.mapper.analog;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.jiahuan.entity.analog.AnalogCode;
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
public interface AnalogCodeMapper extends BaseMapper<AnalogCode> {

    AnalogCode getAnalogCodeByDeviceId(Integer deviceId);

    AnalogCode getAnalogCodeByDivisorName(String divisorName);


}
