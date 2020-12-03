package org.jiahuan.service.coun.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.coun.CounDivisorParameter;
import org.jiahuan.mapper.coun.CounDivisorParameterMapper;
import org.jiahuan.service.coun.ICounDivisorParameterService;
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
public class CounDivisorParameterServiceImpl extends ServiceImpl<CounDivisorParameterMapper, CounDivisorParameter> implements ICounDivisorParameterService {

    @Autowired
    private ICounDivisorParameterService iCounDivisorParameterService;

    @Override
    public List<CounDivisorParameter> getCounDivisorByDeviceId(Integer deviceId) {
        QueryWrapper<CounDivisorParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        List<CounDivisorParameter> counDivisorParameters = iCounDivisorParameterService.list(queryWrapper);
        return counDivisorParameters;
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<CounDivisorParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        iCounDivisorParameterService.remove(queryWrapper);
    }
}
