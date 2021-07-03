package org.jiahuan.service.analog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.analog.AnDynamicParameter;
import org.jiahuan.mapper.analog.AnDynamicParameterMapper;
import org.jiahuan.service.analog.IAnDynamicParameterService;
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
public class AnDynamicParameterServiceImpl extends ServiceImpl<AnDynamicParameterMapper, AnDynamicParameter> implements IAnDynamicParameterService {

    @Resource
    private AnDynamicParameterMapper parameterMapper;

    @Override
    public AnDynamicParameter getDynamicParameterById(Integer id) {
        return parameterMapper.getDynamicParameterById(id);
    }

    @Override
    public List<AnDynamicParameter> getDynamicParameterByDeviceId(Integer deviceId, Integer type) {
        return parameterMapper.getDynamicParameterByDeviceId(deviceId,type);
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<AnDynamicParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        parameterMapper.delete(queryWrapper);
    }
}
