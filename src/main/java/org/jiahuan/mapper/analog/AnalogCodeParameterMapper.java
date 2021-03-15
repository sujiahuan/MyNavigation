package org.jiahuan.mapper.analog;

import org.jiahuan.entity.analog.AnalogCodeParameter;
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
public interface AnalogCodeParameterMapper extends BaseMapper<AnalogCodeParameter> {

    List<AnalogCodeParameter> getAnalogParameterByCodeId(Integer codeId);

}
