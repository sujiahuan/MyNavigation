package org.jiahuan.service.analog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.analog.AnalogDynamicParameter;
import org.jiahuan.mapper.analog.AnalogDynamicParameterMapper;
import org.jiahuan.service.analog.IAnalogDynamicParameterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
@Service
public class AnalogDynamicParameterServiceImpl extends ServiceImpl<AnalogDynamicParameterMapper, AnalogDynamicParameter> implements IAnalogDynamicParameterService {

    @Resource
    private AnalogDynamicParameterMapper parameterMapper;

    @Override
    public AnalogDynamicParameter getDynamicParameterById(Integer id) {
        return parameterMapper.getDynamicParameterById(id);
    }

    @Override
    public List<AnalogDynamicParameter> getDynamicParameterByDeviceId(Integer deviceId,Integer type) {
        return parameterMapper.getDynamicParameterByDeviceId(deviceId,type);
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<AnalogDynamicParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        parameterMapper.delete(queryWrapper);
    }
}
