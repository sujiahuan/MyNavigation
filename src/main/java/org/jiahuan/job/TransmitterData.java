package org.jiahuan.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.common.util.TimeUtil;
import org.jiahuan.entity.coun.CounCountercharge;
import org.jiahuan.entity.coun.CounDataType;
import org.jiahuan.entity.coun.CounDevice;
import org.jiahuan.service.coun.ICounCounterchargeService;
import org.jiahuan.service.coun.ICounDataTypeService;
import org.jiahuan.service.coun.ICounDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class TransmitterData {

    @Autowired
    private ICounDataTypeService iCounDataTypeService;
    @Autowired
    private ICounDeviceService iCounDeviceService;
    @Autowired
    private ICounCounterchargeService iCounCounterchargeService;

    @Scheduled(cron="0,30 * * * * ?")
    public void send2011Data() {
        System.out.println("实时数据运行啦："+ TimeUtil.getFormatCurrentTime("millisecond",0));
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing",1);
        queryWrapper.eq("data_type",1);
        List<CounDataType> listCountDataType = iCounDataTypeService.list(queryWrapper);
        for (CounDataType counDataType : listCountDataType) {
            CounDevice counDevice = iCounDeviceService.getById(counDataType.getDeviceId());
            try {
                iCounDataTypeService.sendRealTime(counDataType.getDeviceId(),counDevice.getAgreement(),1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(cron="0 0/10 * * * ?")
    public void send2051Data() {
        System.out.println("分钟数据运行啦："+ TimeUtil.getFormatCurrentTime("millisecond",0));
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing",1);
        queryWrapper.eq("data_type",2);
        List<CounDataType> listCountDataType = iCounDataTypeService.list(queryWrapper);
        for (CounDataType counDataType : listCountDataType) {
            CounDevice counDevice = iCounDeviceService.getById(counDataType.getDeviceId());
            try {
                iCounDataTypeService.sendRealTime(counDataType.getDeviceId(),counDevice.getAgreement(),2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(cron="0 0 0/1 * * ?")
    public void send2061Data(){
        System.out.println("小时数据运行啦："+ TimeUtil.getFormatCurrentTime("millisecond",0));
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing",1);
        queryWrapper.eq("data_type",3);
        List<CounDataType> listCountDataType = iCounDataTypeService.list(queryWrapper);
        for (CounDataType counDataType : listCountDataType) {
            CounDevice counDevice = iCounDeviceService.getById(counDataType.getDeviceId());
            try {
                iCounDataTypeService.sendRealTime(counDataType.getDeviceId(),counDevice.getAgreement(),3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(cron="0 0 0 * * ? ")
    public void send2031Data(){
        System.out.println("日数据运行啦："+ TimeUtil.getFormatCurrentTime("millisecond",0));
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing",1);
        queryWrapper.eq("data_type",4);
        List<CounDataType> listCountDataType = iCounDataTypeService.list(queryWrapper);
        for (CounDataType counDataType : listCountDataType) {
            CounDevice counDevice = iCounDeviceService.getById(counDataType.getDeviceId());
            try {
                iCounDataTypeService.sendRealTime(counDataType.getDeviceId(),counDevice.getAgreement(),4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
