package org.jiahuan.service.coun.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.coun.CounDivisor;
import org.jiahuan.entity.coun.CounParameter;
import org.jiahuan.mapper.coun.CounParameterMapper;
import org.jiahuan.service.coun.ICounDivisorService;
import org.jiahuan.service.coun.ICounParameterService;
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
public class CounParameterServiceImpl extends ServiceImpl<CounParameterMapper, CounParameter> implements ICounParameterService {

    @Autowired
    private ICounParameterService iCounParameterService;

    @Override
    public List<CounParameter> getCounParameterByCodeId(Integer codeId) {
        QueryWrapper<CounParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code_id",codeId);
        List<CounParameter> counParameters = iCounParameterService.list(queryWrapper);
        return counParameters;
    }

    @Override
    public void deleteByCodeId(Integer codeId) {
        QueryWrapper<CounParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code_id",codeId);
        iCounParameterService.remove(queryWrapper);
    }
}
