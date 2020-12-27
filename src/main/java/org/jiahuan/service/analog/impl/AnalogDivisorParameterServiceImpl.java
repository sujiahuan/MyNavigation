package org.jiahuan.service.analog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.analog.AnalogDivisorParameter;
import org.jiahuan.mapper.analog.AnalogDivisorParameterMapper;
import org.jiahuan.service.analog.IAnalogDivisorParameterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class AnalogDivisorParameterServiceImpl extends ServiceImpl<AnalogDivisorParameterMapper, AnalogDivisorParameter> implements IAnalogDivisorParameterService {

    @Autowired
    private IAnalogDivisorParameterService iAnalogDivisorParameterService;
    @Autowired
    private AnalogDivisorParameterMapper analogDivisorParameterMapper;

    @Override
    public List<AnalogDivisorParameter> getCounDivisorByDeviceId(Integer deviceId) {
//        QueryWrapper<AnalogDivisorParameter> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("device_id",deviceId);
//        List<AnalogDivisorParameter> analogDivisorParameters = iAnalogDivisorParameterService.list(queryWrapper);
        List<AnalogDivisorParameter> analogDivisorParameters = analogDivisorParameterMapper.getCounDivisorByDeviceId(deviceId);
        return analogDivisorParameters;
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<AnalogDivisorParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        iAnalogDivisorParameterService.remove(queryWrapper);
    }
}
