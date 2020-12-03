package org.jiahuan.service.coun.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.coun.CounCode;
import org.jiahuan.mapper.coun.CounCodeMapper;
import org.jiahuan.service.coun.ICounCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.service.coun.ICounCodeParameterService;
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
public class CounCodeServiceImpl extends ServiceImpl<CounCodeMapper, CounCode> implements ICounCodeService {

    @Autowired
    private ICounCodeParameterService iCounCodeParameterService;
    @Autowired
    private ICounCodeService iCounCodeService;

    @Override
    public CounCode getCounCodeByDeviceId(Integer deviceId) {
        QueryWrapper<CounCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        CounCode counCode = iCounCodeService.getOne(queryWrapper);
        return counCode;
    }

    @Override
    public CounCode getCounCodeByCode(String coode) {
        QueryWrapper<CounCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",coode);
        CounCode counCode = iCounCodeService.getOne(queryWrapper);
        return counCode;
    }


    @Override
    public void deleteCounCodeByDeviceId(Integer deviceId) {
        QueryWrapper<CounCode> queryCodeWrapper = new QueryWrapper<>();
        queryCodeWrapper.eq("device_id",deviceId);
        List<CounCode> counCodes = iCounCodeService.list(queryCodeWrapper);
        for (CounCode counCode : counCodes) {
            iCounCodeParameterService.deleteByCodeId(counCode.getId());
        }
        iCounCodeService.remove(queryCodeWrapper);
    }
}
