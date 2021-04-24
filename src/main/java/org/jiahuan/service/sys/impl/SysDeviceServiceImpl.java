package org.jiahuan.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.entity.analog.AnalogDivisorParameter;
import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.mapper.sys.SysDeviceMapper;
import org.jiahuan.netty.NettyClient;
import org.jiahuan.service.analog.IAnalogDataTypeService;
import org.jiahuan.service.analog.IAnalogDivisorParameterService;
import org.jiahuan.service.analog.IAnalogRemoteCounteraccusationService;
import org.jiahuan.service.sys.ISysDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private NettyClient nettyClient;

    @Transactional
    @Override
    public void addInitCounDevice(SysDevice counDvice) {
         iSysDeviceService.save(counDvice);
        if(null!=counDvice.getCopyDeviceId()){
            List<AnalogDivisorParameter> divisorParameters = iAnalogDivisorParameterService.getDivisorParameterByDeviceId(counDvice.getCopyDeviceId());
            for (AnalogDivisorParameter divisorParameter : divisorParameters) {
                divisorParameter.setDeviceId(counDvice.getId());
            }
            iAnalogDivisorParameterService.saveBatch(divisorParameters);
        }
        //初始化数据类型
        iAnalogDataTypeService.addInitByDeviceId(counDvice.getId());
        //初始化反控
        iAnalogRemoteCounteraccusationService.addInitByDeviceId(counDvice.getId());
    }

    @Override
    public void updateCounDevice(SysDevice sysDevice)  {
        iSysDeviceService.updateById(sysDevice);
        if(null!=sysDevice.getCopyDeviceId()){
            QueryWrapper<AnalogDivisorParameter> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("device_id", sysDevice.getId());
            iAnalogDivisorParameterService.remove(queryWrapper);
            List<AnalogDivisorParameter> divisorParameters = iAnalogDivisorParameterService.getDivisorParameterByDeviceId(sysDevice.getCopyDeviceId());
            for (AnalogDivisorParameter divisorParameter : divisorParameters) {
                divisorParameter.setDeviceId(sysDevice.getId());
            }

            iAnalogDivisorParameterService.saveBatch(divisorParameters);
        }
        nettyClient.closeConnection(sysDevice.getId());
    }

    @Transactional
    @Override
    public void deleteInitById(Integer deviceId) {
        iAnalogDivisorParameterService.deleteByDeviceId(deviceId);
        iAnalogDataTypeService.deleteByDeviceId(deviceId);
        iAnalogRemoteCounteraccusationService.deleteByDeviceId(deviceId);
        iSysDeviceService.removeById(deviceId);
        nettyClient.closeConnection(deviceId);
    }
}
