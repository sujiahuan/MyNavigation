package org.jiahuan.service.analog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.analog.AnalogCodeParameter;
import org.jiahuan.mapper.analog.AnalogCodeParameterMapper;
import org.jiahuan.service.analog.IAnalogCodeParameterService;
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
public class AnalogCodeParameterServiceImpl extends ServiceImpl<AnalogCodeParameterMapper, AnalogCodeParameter> implements IAnalogCodeParameterService {

    @Autowired
    private IAnalogCodeParameterService iAnalogCodeParameterService;

    @Override
    public List<AnalogCodeParameter> getCounParameterByCodeId(Integer codeId) {
        QueryWrapper<AnalogCodeParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code_id",codeId);
        List<AnalogCodeParameter> analogCodeParameters = iAnalogCodeParameterService.list(queryWrapper);
        return analogCodeParameters;
    }

    @Override
    public void deleteByCodeId(Integer codeId) {
        QueryWrapper<AnalogCodeParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code_id",codeId);
        iAnalogCodeParameterService.remove(queryWrapper);
    }
}
