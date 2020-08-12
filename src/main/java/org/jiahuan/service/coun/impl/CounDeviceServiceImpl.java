package org.jiahuan.service.coun.impl;

import org.jiahuan.entity.coun.CounDevice;
import org.jiahuan.mapper.coun.CounDeviceMapper;
import org.jiahuan.service.coun.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
@Service
public class CounDeviceServiceImpl extends ServiceImpl<CounDeviceMapper, CounDevice> implements ICounDeviceService {

    @Autowired
    private ICounDeviceService iCounDeviceService;
    @Autowired
    private ICounDivisorService iCounDivisorService;
    @Autowired
    private ICounDataTypeService iCounDataTypeService;
    @Autowired
    private ICounCounterchargeService iCounCounterchargeService;

    @Override
    public void addInitCounDevice(CounDevice counDvice) {
        iCounDeviceService.save(counDvice);
        //初始化数据类型
        iCounDataTypeService.addInitByDeviceId(counDvice.getId());
        //初始化反控
        iCounCounterchargeService.addInitByDeviceId(counDvice.getId());
    }

    @Override
    public void deleteAllById(Integer deviceId) {
        iCounDivisorService.deleteByDeviceId(deviceId);
        iCounDataTypeService.deleteByDeviceId(deviceId);
        iCounCounterchargeService.deleteByDeviceId(deviceId);
        iCounDeviceService.removeById(deviceId);
    }
}
