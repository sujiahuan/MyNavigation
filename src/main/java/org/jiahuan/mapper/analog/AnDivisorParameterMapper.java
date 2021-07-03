package org.jiahuan.mapper.analog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jiahuan.entity.analog.AnDivisorParameter;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
@Mapper
public interface AnDivisorParameterMapper extends BaseMapper<AnDivisorParameter> {

//    @Select("select t1.*,t2.code as divisorCode from an_divisor_parameter t1 INNER JOIN sys_divisor t2 on t1.divisor_id=t2.id where device_id=${deviceId}")
    List<AnDivisorParameter> getDivisorParameterByDeviceId(@Param("deviceId") Integer deviceId);

}
