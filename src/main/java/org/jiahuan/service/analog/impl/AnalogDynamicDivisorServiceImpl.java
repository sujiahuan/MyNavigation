package org.jiahuan.service.analog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.analog.AnalogDynamicDivisor;
import org.jiahuan.mapper.analog.AnalogDynamicDivisorMapper;
import org.jiahuan.service.analog.IAnalogDynamicDivisorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.service.analog.IAnalogDynamicParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jh
 * @since 2020-08-05
 */
@Service
public class AnalogDynamicDivisorServiceImpl extends ServiceImpl<AnalogDynamicDivisorMapper, AnalogDynamicDivisor> implements IAnalogDynamicDivisorService {

    @Resource
    private AnalogDynamicDivisorMapper analogDynamicDivisorMapper;

    @Override
    public AnalogDynamicDivisor getDynamicDivisorByDeviceId(Integer deviceId) {
        List<AnalogDynamicDivisor> analogDynamicDivisors = analogDynamicDivisorMapper.getDynamicDivisorByDeviceId(deviceId);
        if(analogDynamicDivisors.size()==0){
            return null;
        }
        for (int i = 1; i < analogDynamicDivisors.size(); i++) {
            analogDynamicDivisorMapper.deleteById(analogDynamicDivisors.get(i).getId());
        }
        return analogDynamicDivisors.get(0);
    }

    @Override
    public AnalogDynamicDivisor getDynamicDivisorById(Integer id) {
        return analogDynamicDivisorMapper.getDynamicDivisorById(id);
    }


    @Override
    public void deleteDynamicDivisorByDeviceId(Integer deviceId) {
        QueryWrapper<AnalogDynamicDivisor> codeQueryWrapper = new QueryWrapper<>();
        codeQueryWrapper.eq("device_id", deviceId);
        analogDynamicDivisorMapper.delete(codeQueryWrapper);
    }
}
