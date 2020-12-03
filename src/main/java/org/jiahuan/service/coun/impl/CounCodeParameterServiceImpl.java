package org.jiahuan.service.coun.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.coun.CounCodeParameter;
import org.jiahuan.mapper.coun.CounCodeParameterMapper;
import org.jiahuan.service.coun.ICounCodeParameterService;
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
public class CounCodeParameterServiceImpl extends ServiceImpl<CounCodeParameterMapper, CounCodeParameter> implements ICounCodeParameterService {

    @Autowired
    private ICounCodeParameterService iCounCodeParameterService;

    @Override
    public List<CounCodeParameter> getCounParameterByCodeId(Integer codeId) {
        QueryWrapper<CounCodeParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code_id",codeId);
        List<CounCodeParameter> counCodeParameters = iCounCodeParameterService.list(queryWrapper);
        return counCodeParameters;
    }

    @Override
    public void deleteByCodeId(Integer codeId) {
        QueryWrapper<CounCodeParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code_id",codeId);
        iCounCodeParameterService.remove(queryWrapper);
    }
}
