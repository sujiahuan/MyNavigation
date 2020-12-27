package org.jiahuan.service.sys.impl;

import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.mapper.sys.SysDeviceMapper;
import org.jiahuan.service.analog.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.service.sys.ISysDeviceService;
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
public class SysDeviceServiceImpl extends ServiceImpl<SysDeviceMapper, SysDevice> implements ISysDeviceService {

    @Autowired
    private ISysDeviceService iSysDeviceService;
    @Autowired
    private IAnalogDivisorParameterService iAnalogDivisorParameterService;
    @Autowired
    private IAnalogDataTypeService iAnalogDataTypeService;
    @Autowired
    private IAnalogRemoteCounteraccusationService iAnalogRemoteCounteraccusationService;
    @Autowired
    private IConnectionObj iConnectionObj;

    @Transactional
    @Override
    public void addInitCounDevice(SysDevice counDvice) throws IOException {
        iSysDeviceService.save(counDvice);
        //初始化数据类型
        iAnalogDataTypeService.addInitByDeviceId(counDvice.getId());
        //初始化反控
        iAnalogRemoteCounteraccusationService.addInitByDeviceId(counDvice.getId());
    }

    @Override
    public void updateCounDevice(SysDevice sysDevice) throws IOException {
        iSysDeviceService.updateById(sysDevice);
        iConnectionObj.cleanConnetion(sysDevice.getId(),true);
    }

    @Transactional
    @Override
    public void deleteInitById(Integer deviceId) {
        iAnalogDivisorParameterService.deleteByDeviceId(deviceId);
        iAnalogDataTypeService.deleteByDeviceId(deviceId);
        iAnalogRemoteCounteraccusationService.deleteByDeviceId(deviceId);
        iSysDeviceService.removeById(deviceId);
    }
}
