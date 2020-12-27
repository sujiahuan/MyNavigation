package org.jiahuan.service.analog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.analog.AnalogCode;
import org.jiahuan.mapper.analog.AnalogCodeMapper;
import org.jiahuan.service.analog.IAnalogCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.service.analog.IAnalogCodeParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private IAnalogCodeService iAnalogCodeService;

    @Override
    public AnalogCode getCounCodeByDeviceId(Integer deviceId) {
        QueryWrapper<AnalogCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        AnalogCode analogCode = iAnalogCodeService.getOne(queryWrapper);
        return analogCode;
    }

    @Override
    public AnalogCode getCounCodeByCode(String coode) {
        QueryWrapper<AnalogCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",coode);
        AnalogCode analogCode = iAnalogCodeService.getOne(queryWrapper);
        return analogCode;
    }


    @Override
    public void deleteCounCodeByDeviceId(Integer deviceId) {
        QueryWrapper<AnalogCode> queryCodeWrapper = new QueryWrapper<>();
        queryCodeWrapper.eq("device_id",deviceId);
        List<AnalogCode> analogCodes = iAnalogCodeService.list(queryCodeWrapper);
        for (AnalogCode analogCode : analogCodes) {
            iAnalogCodeParameterService.deleteByCodeId(analogCode.getId());
        }
        iAnalogCodeService.remove(queryCodeWrapper);
    }
}
