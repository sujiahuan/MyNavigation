package org.jiahuan.service.analog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.analog.AnDivisorParameter;
import org.jiahuan.mapper.analog.AnDivisorParameterMapper;
import org.jiahuan.service.analog.IAnDivisorParameterService;
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
public class AnDivisorParameterServiceImpl extends ServiceImpl<AnDivisorParameterMapper, AnDivisorParameter> implements IAnDivisorParameterService {

    @Autowired
    private IAnDivisorParameterService iAnDivisorParameterService;
    @Autowired
    private AnDivisorParameterMapper anDivisorParameterMapper;

    @Override
    public List<AnDivisorParameter> getDivisorParameterByDeviceId(Integer deviceId) {
        List<AnDivisorParameter> anDivisorParameters = anDivisorParameterMapper.getDivisorParameterByDeviceId(deviceId);
        return anDivisorParameters;
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<AnDivisorParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        iAnDivisorParameterService.remove(queryWrapper);
    }
}
