package org.jiahuan.mapper.analog;

import org.apache.ibatis.annotations.Mapper;
import org.jiahuan.entity.analog.AnDynamicDivisor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jh
 * @since 2020-08-05
 */
@Mapper
public interface AnDynamicDivisorMapper extends BaseMapper<AnDynamicDivisor> {

    List<AnDynamicDivisor> getDynamicDivisorByDeviceId(Integer deviceId);

    AnDynamicDivisor getDynamicDivisorById(Integer id);


}
