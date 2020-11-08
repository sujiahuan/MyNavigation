package org.jiahuan.service.coun.impl;

import org.jiahuan.entity.coun.CounDevice;
import org.jiahuan.mapper.coun.CounDeviceMapper;
import org.jiahuan.service.coun.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

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
    @Autowired
    private IConnectionObj iConnectionObj;

    @Transactional
    @Override
    public void addInitCounDevice(CounDevice counDvice) throws IOException {
        iCounDeviceService.save(counDvice);
        //初始化数据类型
        iCounDataTypeService.addInitByDeviceId(counDvice.getId());
        //初始化反控
        iCounCounterchargeService.addInitByDeviceId(counDvice.getId());
    }

    @Override
    public void updateCounDevice(CounDevice counDevice) throws IOException {
        iCounCounterchargeService.closeConnection(counDevice.getId());
        iCounDeviceService.updateById(counDevice);
        iConnectionObj.cleanConnetion(counDevice.getId(),true);
    }

    @Transactional
    @Override
    public void deleteInitById(Integer deviceId) {
        iCounDivisorService.deleteByDeviceId(deviceId);
        iCounDataTypeService.deleteByDeviceId(deviceId);
        iCounCounterchargeService.deleteByDeviceId(deviceId);
        iCounDeviceService.removeById(deviceId);
    }
}
