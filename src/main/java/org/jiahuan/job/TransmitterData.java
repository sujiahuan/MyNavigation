package org.jiahuan.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.util.TimeUtil;
import org.jiahuan.entity.analog.AnDataType;
import org.jiahuan.service.analog.IAnDataTypeService;
import org.jiahuan.service.analog.IAnRemoteControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class TransmitterData {

    @Autowired
    private IAnDataTypeService iAnDataTypeService;
    @Autowired
    private IAnRemoteControlService iAnRemoteControlService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

        @Scheduled(cron = "0,30 * * * * ?")
    public void send2011Data() {
        log.info("实时数据运行啦：" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0));
        QueryWrapper<AnDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing", 1);
        queryWrapper.eq("data_type", 1);
        List<AnDataType> listCountDataType = iAnDataTypeService.list(queryWrapper);
        for (AnDataType anDataType : listCountDataType) {
            executorService.submit(() -> {
                try {
                    iAnDataTypeService.sendRealTime(anDataType.getDeviceId(), 1);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            });
        }
    }

        @Scheduled(cron = "0 0/10 * * * ?")
    public void send2051Data() {
        log.info("分钟数据运行啦：" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0));
        QueryWrapper<AnDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_type", 2);
        queryWrapper.and(wrapper->wrapper.eq("is_timing", 1).or().in("device_id", iAnRemoteControlService.getAllControlDeviceId()));
//        queryWrapper.eq("is_timing", 1);
//        queryWrapper.or().in("device_id", iAnalogRemoteCounteraccusationService.getAllControlDeviceId());
        List<AnDataType> listCountDataType = iAnDataTypeService.list(queryWrapper);
        for (AnDataType anDataType : listCountDataType) {
            executorService.submit(() -> {
                try {
                    iAnDataTypeService.sendRealTime(anDataType.getDeviceId(), 2);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            });
        }
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void send2061Data() {
        log.info("小时数据运行啦：" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0));
        QueryWrapper<AnDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing", 1);
        queryWrapper.eq("data_type", 3);
        List<AnDataType> listCountDataType = iAnDataTypeService.list(queryWrapper);
        for (AnDataType anDataType : listCountDataType) {
            executorService.submit(() -> {
                try {
                    iAnDataTypeService.sendRealTime(anDataType.getDeviceId(), 3);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            });
        }
    }

    @Scheduled(cron = "0 0 0 * * ? ")
    public void send2031Data() {
        log.info("日数据运行啦：" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0));
        QueryWrapper<AnDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_timing", 1);
        queryWrapper.eq("data_type", 4);
        List<AnDataType> listCountDataType = iAnDataTypeService.list(queryWrapper);
        for (AnDataType anDataType : listCountDataType) {
            executorService.submit(() -> {
                try {
                    iAnDataTypeService.sendRealTime(anDataType.getDeviceId(), 4);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            });
        }
    }

}
