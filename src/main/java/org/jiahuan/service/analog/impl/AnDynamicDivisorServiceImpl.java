package org.jiahuan.service.analog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.analog.AnDynamicDivisor;
import org.jiahuan.mapper.analog.AnDynamicDivisorMapper;
import org.jiahuan.service.analog.IAnDynamicDivisorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class AnDynamicDivisorServiceImpl extends ServiceImpl<AnDynamicDivisorMapper, AnDynamicDivisor> implements IAnDynamicDivisorService {

    @Resource
    private AnDynamicDivisorMapper anDynamicDivisorMapper;

    @Override
    public AnDynamicDivisor getDynamicDivisorByDeviceId(Integer deviceId) {
        List<AnDynamicDivisor> anDynamicDivisors = anDynamicDivisorMapper.getDynamicDivisorByDeviceId(deviceId);
        if(anDynamicDivisors.size()==0){
            return null;
        }
        for (int i = 1; i < anDynamicDivisors.size(); i++) {
            anDynamicDivisorMapper.deleteById(anDynamicDivisors.get(i).getId());
        }
        return anDynamicDivisors.get(0);
    }

    @Override
    public AnDynamicDivisor getDynamicDivisorById(Integer id) {
        return anDynamicDivisorMapper.getDynamicDivisorById(id);
    }


    @Override
    public void deleteDynamicDivisorByDeviceId(Integer deviceId) {
        QueryWrapper<AnDynamicDivisor> codeQueryWrapper = new QueryWrapper<>();
        codeQueryWrapper.eq("device_id", deviceId);
        anDynamicDivisorMapper.delete(codeQueryWrapper);
    }
}
