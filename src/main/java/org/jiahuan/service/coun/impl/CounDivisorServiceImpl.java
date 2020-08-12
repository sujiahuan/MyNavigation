package org.jiahuan.service.coun.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.coun.CounDivisor;
import org.jiahuan.mapper.coun.CounDivisorMapper;
import org.jiahuan.service.coun.ICounDivisorService;
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
public class CounDivisorServiceImpl extends ServiceImpl<CounDivisorMapper, CounDivisor> implements ICounDivisorService {

    @Autowired
    private ICounDivisorService iCounDivisorService;

    @Override
    public List<CounDivisor> getCounDivisorByDeviceId(Integer deviceId) {
        QueryWrapper<CounDivisor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        List<CounDivisor> counDivisors = iCounDivisorService.list(queryWrapper);
        return counDivisors;
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<CounDivisor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        iCounDivisorService.remove(queryWrapper);
    }
}
