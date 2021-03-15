package org.jiahuan.service.analog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.analog.AnalogCode;
import org.jiahuan.entity.analog.AnalogCodeParameter;
import org.jiahuan.mapper.analog.AnalogCodeMapper;
import org.jiahuan.service.analog.IAnalogCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.service.analog.IAnalogCodeParameterService;
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
public class AnalogCodeServiceImpl extends ServiceImpl<AnalogCodeMapper, AnalogCode> implements IAnalogCodeService {

    @Autowired
    private IAnalogCodeParameterService iAnalogCodeParameterService;
    @Resource
    private AnalogCodeMapper analogCodeMapper;

    @Override
    public AnalogCode getAnalogCodeByDeviceId(Integer deviceId) {
        return analogCodeMapper.getAnalogCodeByDeviceId(deviceId);
    }

    @Override
    public AnalogCode getAnalogCodeByDivisorName(String divisorName) {
        return analogCodeMapper.getAnalogCodeByDivisorName(divisorName);
    }


    @Override
    public void deleteAnalogCodeByDeviceId(Integer deviceId) {
        AnalogCode analogCode = analogCodeMapper.getAnalogCodeByDeviceId(deviceId);
        iAnalogCodeParameterService.deleteByCodeId(analogCode.getId());
        QueryWrapper<AnalogCode> codeQueryWrapper = new QueryWrapper<>();
        codeQueryWrapper.eq("device_id", deviceId);
        analogCodeMapper.delete(codeQueryWrapper);
    }
}
