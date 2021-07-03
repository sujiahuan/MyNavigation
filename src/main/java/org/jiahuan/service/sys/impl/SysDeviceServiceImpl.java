package org.jiahuan.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.entity.analog.AnDivisorParameter;
import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.entity.sys.SysNavigation;
import org.jiahuan.mapper.sys.SysDeviceMapper;
import org.jiahuan.netty.NettyClient;
import org.jiahuan.service.analog.IAnDataTypeService;
import org.jiahuan.service.analog.IAnDivisorParameterService;
import org.jiahuan.service.analog.IAnRemoteControlService;
import org.jiahuan.service.sys.ISysDeviceNavigationService;
import org.jiahuan.service.sys.ISysDeviceService;
import org.jiahuan.service.sys.ISysNavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private IAnDivisorParameterService iAnDivisorParameterService;
    @Autowired
    private IAnDataTypeService iAnDataTypeService;
    @Autowired
    private IAnRemoteControlService iAnRemoteControlService;
    @Autowired
    private ISysNavigationService iSysNavigationService;
    @Autowired
    private ISysDeviceNavigationService iSysDeviceNavigationService;
    @Autowired
    private NettyClient nettyClient;
    @Resource
    private SysDeviceMapper sysDeviceMapper;

    @Override
    public SysDevice getSysDeviceById(Integer id) {
        return sysDeviceMapper.getSysDeviceById(id);
    }

    @Override
    public List<SysDevice> getListSysDevice() {
        return sysDeviceMapper.getAllSysDevice();
    }


    @Transactional
    @Override
    public void addInitCounDevice(SysDevice counDvice) {
         this.save(counDvice);
         //复制设备因子
        if(null!=counDvice.getCopyDeviceId()){
            List<AnDivisorParameter> divisorParameters = iAnDivisorParameterService.getDivisorParameterByDeviceId(counDvice.getCopyDeviceId());
            for (AnDivisorParameter divisorParameter : divisorParameters) {
                divisorParameter.setDeviceId(counDvice.getId());
            }
            iAnDivisorParameterService.saveBatch(divisorParameters);
        }
        //初始化数据类型
        iAnDataTypeService.addInitByDeviceId(counDvice.getId());
        //初始化反控
        iAnRemoteControlService.addInitByDeviceId(counDvice.getId());
        //添加使用的因子分类
        iSysDeviceNavigationService.udpate(counDvice);
    }

    @Override
    public void updateCounDevice(SysDevice sysDevice)  {
        this.updateById(sysDevice);
        //复制设备因子
        if(null!=sysDevice.getCopyDeviceId()){
            QueryWrapper<AnDivisorParameter> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("device_id", sysDevice.getId());
            iAnDivisorParameterService.remove(queryWrapper);
            List<AnDivisorParameter> divisorParameters = iAnDivisorParameterService.getDivisorParameterByDeviceId(sysDevice.getCopyDeviceId());
            for (AnDivisorParameter divisorParameter : divisorParameters) {
                divisorParameter.setDeviceId(sysDevice.getId());
            }

            iAnDivisorParameterService.saveBatch(divisorParameters);
        }
        //断开socket连接
        nettyClient.closeConnection(sysDevice.getId());
        //更新设备因子类型
        iSysDeviceNavigationService.udpate(sysDevice);
    }

    @Transactional
    @Override
    public void deleteInitById(Integer deviceId) {
        iAnDivisorParameterService.deleteByDeviceId(deviceId);
        iSysDeviceNavigationService.deleteByDeviceId(deviceId);
        iAnDataTypeService.deleteByDeviceId(deviceId);
        iAnRemoteControlService.deleteByDeviceId(deviceId);
        this.removeById(deviceId);
        nettyClient.closeConnection(deviceId);
    }
}
