package org.jiahuan.service.analog.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.config.CustomWebSocketConfig;
import org.jiahuan.common.util.DataPackageUtils;
import org.jiahuan.common.util.RandomUtil;
import org.jiahuan.common.util.TimeUtil;
import org.jiahuan.entity.analog.AnDynamicDivisor;
import org.jiahuan.entity.analog.AnDynamicParameter;
import org.jiahuan.entity.analog.AnDataType;
import org.jiahuan.entity.analog.AnDivisorParameter;
import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.mapper.analog.AnDataTypeMapper;
import org.jiahuan.netty.NettyClient;
import org.jiahuan.service.analog.*;
import org.jiahuan.service.sys.ISysDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wj
 * @since 2020-08-02
 */
@Service
@Slf4j
public class AnDataTypeServiceImpl extends ServiceImpl<AnDataTypeMapper, AnDataType> implements IAnDataTypeService {

    @Autowired
    private ISysDeviceService iSysDeviceService;
    @Autowired
    private IAnDivisorParameterService iAnDivisorParameterService;
    @Autowired
    private IAnDynamicDivisorService iAnDynamicDivisorService;
    @Autowired
    private IAnDynamicParameterService iAnDynamicParameterService;
    @Autowired
    private CustomWebSocketConfig customWebSocketConfig;
    @Autowired
    private NettyClient nettyClient;
    /**
     * 补发状态map
     */
    private Map<Integer, Boolean> supplyAgainStatus = new HashMap<>();

    @Override
    public void setSupplyAgainStatus(Integer deviceId, boolean supplyStatus) {
        supplyAgainStatus.put(deviceId, supplyStatus);
    }

    @Override
    public boolean getSupplyAgainStatus(Integer deviceId) {
        if (supplyAgainStatus.containsKey(deviceId) && supplyAgainStatus.get(deviceId)) {
            return true;
        }
        return false;
    }

    @Override
    public void waitForTheReissueToComplete(Integer deviceId) throws InterruptedException {
        if (supplyAgainStatus.get(deviceId)) {
            synchronized (deviceId) {
                deviceId.wait();
            }
        }
    }

    @Override
    public AnDataType getCounDataTypeByDeviceId(Integer deviceId, Integer dataType) {
        QueryWrapper<AnDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        queryWrapper.eq("data_type", dataType);
        AnDataType anDataType = this.getOne(queryWrapper);
        return anDataType;
    }

    @Override
    public List<AnDataType> getListCounDataTypeByDeviceId(Integer deviceId) {
        QueryWrapper<AnDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        List<AnDataType> anDataTypes = this.list(queryWrapper);
        return anDataTypes;
    }

    @Override
    public void addInitByDeviceId(Integer deviceId) {
        AnDataType anDataType2011 = new AnDataType(deviceId, 1, 0, "none", 30, TimeUtil.getProcessedCurrentTime("second", -30), TimeUtil.getProcessedCurrentTime("second", 0));
        AnDataType anDataType2051 = new AnDataType(deviceId, 2, 0, "none", 10, TimeUtil.getProcessedCurrentTime("minute", -10), TimeUtil.getProcessedCurrentTime("minute", 0));
        AnDataType anDataType2061 = new AnDataType(deviceId, 3, 0, "none", 1, TimeUtil.getProcessedCurrentTime("hour", -1), TimeUtil.getProcessedCurrentTime("hour", 0));
        AnDataType anDataType2031 = new AnDataType(deviceId, 4, 0, "none", 1, TimeUtil.getProcessedCurrentTime("day", -1), TimeUtil.getProcessedCurrentTime("day", 0));

        this.save(anDataType2011);
        this.save(anDataType2051);
        this.save(anDataType2061);
        this.save(anDataType2031);
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<AnDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        this.remove(queryWrapper);
    }


    @Override
    public void sendRealTime(Integer deviceId, Integer dataType) throws Exception {
        List<String> dataPacks = new ArrayList<>();
        SysDevice sysDevice = iSysDeviceService.getById(deviceId);
        List<AnDivisorParameter> anDivisorParameters = iAnDivisorParameterService.getDivisorParameterByDeviceId(deviceId);
        String beforeMN = sysDevice.getMn();
        Integer analogNumber = sysDevice.getAnalogNumber();
        for (int number = 1; number <= analogNumber; number++) {
            String mn = String.format("%0" + analogNumber.toString().length() + "d", number);
            if (!analogNumber.equals(1)) {
                sysDevice.setMn(beforeMN + mn);
            }

            List<Object> divisorParameters = new ArrayList<>();
            int pnum = 1;
            int pon = 1;
            Date date = new Date();
            //判断是否需要包头
            if (sysDevice.getSubpackage() == 1) {
                if (anDivisorParameters.size() % sysDevice.getSubpackageNumber() == 0) {
                    pnum = anDivisorParameters.size() / sysDevice.getSubpackageNumber();
                } else {
                    pnum = anDivisorParameters.size() / sysDevice.getSubpackageNumber() + 1;
                }
            }

            //判断是否需要分包
            if (sysDevice.getSubpackage() != 0) {

                for (int i = 1; i <= anDivisorParameters.size(); i++) {
                    divisorParameters.add(anDivisorParameters.get(i - 1));
                    //判断是否满足分包数
                    if (i % sysDevice.getSubpackageNumber() == 0) {
                        HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                        divisorParameters.clear();
                        //获取实时数据包
                        String message = getRealTimeDataPackage(sysDevice, date, divisorParameterMap, pnum, pon, dataType);
                        this.sendMessage(sysDevice, message, dataPacks);
                        pon++;
                    } else if (i == anDivisorParameters.size()) {
                        HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                        //获取实时数据包
                        String message = getRealTimeDataPackage(sysDevice, date, divisorParameterMap, pnum, pon, dataType);
                        this.sendMessage(sysDevice, message, dataPacks);
                    }
                }
            } else {
                divisorParameters = new ArrayList<>(anDivisorParameters);
                HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                //获取实时数据包
                String message = getRealTimeDataPackage(sysDevice, date, divisorParameterMap, pnum, pon, dataType);
                this.sendMessage(sysDevice, message, dataPacks);
            }
        }
        if (dataPacks.size() > 30) {
            dataPacks = dataPacks.subList(dataPacks.size() - 30, dataPacks.size());
            dataPacks.add(0, "本次发送的数据已超过30条，只保留最后30条数据");
        }
        customWebSocketConfig.customWebSocketHandler().sendMessageToUser(sysDevice.getId(), new TextMessage(dataPacks.toString()));
    }

    @Override
    public String getDataPackage(Integer deviceId, Integer dataType) {
        List<Object> divisorParameters;
        SysDevice sysDevice = iSysDeviceService.getById(deviceId);
        HashMap<String, Map<String, String>> divisorParameterMap;
        if (dataType <= 4) {
            List<AnDivisorParameter> anDivisorParameters = iAnDivisorParameterService.getDivisorParameterByDeviceId(deviceId);
            divisorParameters = new ArrayList<>(anDivisorParameters);
            divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
        } else {
            List<AnDynamicParameter> anDynamicParameters = iAnDynamicParameterService.getDynamicParameterByDeviceId(deviceId, dataType - 4);
            divisorParameters = new ArrayList<>(anDynamicParameters);
            divisorParameterMap = getDivisorParameterMap(divisorParameters, true);
        }

        //获取实时数据包
        String message = getRealTimeDataPackage(sysDevice, new Date(), divisorParameterMap, 1, 1, dataType);
        return message;
    }


    @Override
    public void sendSupplyAgain(Integer deviceId, Integer dataType) throws Exception {
        List<String> dataPacks = new ArrayList<>();
        AnDataType anDataType = this.getCounDataTypeByDeviceId(deviceId, dataType);
        SysDevice sysDevice = iSysDeviceService.getById(deviceId);
        int field = 13;
        String dataTypeStr = "";

        switch (dataType) {
            case 1:
                field = Calendar.SECOND;
                dataTypeStr = "实时";
                break;
            case 2:
                field = Calendar.MINUTE;
                dataTypeStr = "分钟";
                break;
            case 3:
                field = Calendar.HOUR;
                dataTypeStr = "小时";
                break;
            case 4:
                field = Calendar.DAY_OF_MONTH;
                dataTypeStr = "日";
                break;
        }
        //处理时间
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(anDataType.getStartTime());
        endCalendar.setTime(anDataType.getEndTime());

        this.setSupplyAgainStatus(deviceId, true);

        try {

            List<AnDivisorParameter> anDivisorParameters = iAnDivisorParameterService.getDivisorParameterByDeviceId(deviceId);

            List<Object> divisorParameters = new ArrayList<>();

            int pnum = 1;
            int pon = 1;
            int count = 0;

            //计算总包数
            if (sysDevice.getSubpackage() == 1) {
                if (anDivisorParameters.size() % sysDevice.getSubpackageNumber() == 0) {
                    pnum = anDivisorParameters.size() / sysDevice.getSubpackageNumber();
                } else {
                    pnum = anDivisorParameters.size() / sysDevice.getSubpackageNumber() + 1;
                }
            }

            String beforeMN = sysDevice.getMn();
            Integer analogNumber = sysDevice.getAnalogNumber();

            //时间遍历
            while (startCalendar.getTimeInMillis() - endCalendar.getTimeInMillis() <= 0 && this.getSupplyAgainStatus(deviceId)) {
                for (int number = 1; number <= analogNumber; number++) {
                    String mn = String.format("%0" + analogNumber.toString().length() + "d", number);
                    if (!analogNumber.equals(1)) {
                        sysDevice.setMn(beforeMN + mn);
                    }
                    for (int i = 1; i <= anDivisorParameters.size(); i++) {
                        divisorParameters.add(anDivisorParameters.get(i - 1));
                        //判断是否需要分包
                        if (sysDevice.getSubpackage() != 0) {
                            //判断是否满足分包数
                            if (i % sysDevice.getSubpackageNumber() == 0) {
                                HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                                divisorParameters.clear();
                                //获取补发数据包
                                String dataPackage = getSupplyAgainDataPackage(sysDevice, startCalendar.getTime(), divisorParameterMap, pnum, pon, anDataType);
                                //发送消息
                                this.sendMessage(sysDevice, dataPackage, dataPacks);
                                pon++;
                                //最后一个包因子不足则直接发送
                            } else if (i == anDivisorParameters.size()) {
                                HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                                divisorParameters.clear();
                                //获取补发数据包
                                String dataPackage = getSupplyAgainDataPackage(sysDevice, startCalendar.getTime(), divisorParameterMap, pnum, pon, anDataType);
//发送消息
                                this.sendMessage(sysDevice, dataPackage, dataPacks);
                            }
                        } else if (i == anDivisorParameters.size()) {
                            HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                            divisorParameters.clear();
                            //获取补发数据包
                            String dataPackage = getSupplyAgainDataPackage(sysDevice, startCalendar.getTime(), divisorParameterMap, pnum, pon, anDataType);
                            //发送消息
                            this.sendMessage(sysDevice, dataPackage, dataPacks);
                        }
                    }
                }
                //添加时间
                startCalendar.add(field, anDataType.getDateInterval());
                pon = 1;
                count++;
            }

            if (dataPacks.size() > 30) {
                dataPacks = dataPacks.subList(dataPacks.size() - 30, dataPacks.size());
                dataPacks.add(0, "本次补发已超过30条，只保留最后30条数据");
            }
            customWebSocketConfig.customWebSocketHandler().sendMessageToUser(sysDevice.getId(), new TextMessage(dataPacks.toString()));
            if (supplyAgainStatus.get(deviceId)) {
                customWebSocketConfig.customWebSocketHandler().sendMessageToUser(sysDevice.getId(), new TextMessage("发送完成，本次补发" + analogNumber + "个设备的" + dataTypeStr + "数据，每个设备补发：" + count + " 条数据，每条数据分：" + pnum + " 包，最终发送了：" + analogNumber * count * pnum + "条数据\r\n\r\n"));
            } else {
                customWebSocketConfig.customWebSocketHandler().sendMessageToUser(sysDevice.getId(), new TextMessage("终止成功，本次补发" + analogNumber + "个设备的" + dataTypeStr + "数据，每个设备补发：" + count + " 条数据，每条数据分：" + pnum + " 包，最终发送了：" + analogNumber * count * pnum + "条数据\r\n\r\n"
                ));
            }
        } finally {
            this.setSupplyAgainStatus(deviceId, false);
            synchronized (deviceId) {
                deviceId.notifyAll();
            }
        }

    }

    @Override
    public int getSupplyAgainCount(Integer deviceId, Integer dataType) {
        AnDataType anDataType = this.getCounDataTypeByDeviceId(deviceId, dataType);
        //处理时间
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(anDataType.getStartTime());
        endCalendar.setTime(anDataType.getEndTime());

        int field = 13;
        String dataTypeStr = "";

        switch (dataType) {
            case 1:
                field = Calendar.SECOND;
                dataTypeStr = "实时";
                break;
            case 2:
                field = Calendar.MINUTE;
                dataTypeStr = "分钟";
                break;
            case 3:
                field = Calendar.HOUR;
                dataTypeStr = "小时";
                break;
            case 4:
                field = Calendar.DAY_OF_MONTH;
                dataTypeStr = "日";
                break;
        }

        int count = 0;
        //时间遍历
        while (startCalendar.getTimeInMillis() - endCalendar.getTimeInMillis() <= 0) {
            //添加时间
            startCalendar.add(field, anDataType.getDateInterval());
            count++;
        }
        return count;
    }


    @Override
    public void sendParam3020(Integer deviceId, Integer dataType) throws Exception {
        String dataPackage = this.getDataPackage(deviceId, dataType);
        List<String> dataPacks = new ArrayList<>();
        this.sendMessage(iSysDeviceService.getById(deviceId), dataPackage, dataPacks);
        customWebSocketConfig.customWebSocketHandler().sendMessageToUser(deviceId, new TextMessage(dataPacks.toString()));
    }

    @Override
    public void sendMessage(SysDevice sysDevice, String message, List<String> dataPack) throws Exception {
        if (!nettyClient.isConnection(sysDevice.getId())) {
            if (!sysDevice.isAutoConnection()) {
                throw new Exception("连接已断开，请连接");
            }
            if (!nettyClient.connection(sysDevice)) {
                throw new Exception("连接失败，请检查连接");
            }
        }

        if (message.indexOf("\r\n") == -1) {
            message += "\r\n";
        }

        if (nettyClient.sendMessage(sysDevice.getId(), message)) {
            dataPack.add(message);
        } else {
            supplyAgainStatus.put(sysDevice.getId(), false);
            throw new Exception("发送失败，请检查连接");
        }

    }

    @Override
    public void sendCustomizeMessage(Integer deviceId, String message) throws Exception {
        ArrayList<String> dataPack = new ArrayList<>();
        SysDevice sysDevice = iSysDeviceService.getById(deviceId);
        this.sendMessage(sysDevice, message, dataPack);
        customWebSocketConfig.customWebSocketHandler().sendMessageToUser(deviceId, new TextMessage(dataPack.toString()));
    }

    /**
     * 获取因子参数map
     *
     * @param parameters 3020则传code对象，否则传因子参数对象
     * @param isDynamic  是否动态报文
     * @return 返回组装好的因子参数map key：w00001...,value：{'max':'1'、'min':'2'、'i12001':'3'...}
     */
    public HashMap<String, Map<String, String>> getDivisorParameterMap(List<Object> parameters, boolean isDynamic) {
        HashMap<String, Map<String, String>> divisorParameter = new HashMap<>();

        if (isDynamic) {
            List<AnDynamicParameter> anDynamicParameters = new ArrayList(parameters);
            HashMap<String, String> property = new HashMap<>();
            for (AnDynamicParameter anDynamicParameter : anDynamicParameters) {
                property.put(anDynamicParameter.getDivisorCode(), RandomUtil.getRandomString(4, anDynamicParameter.getValueMin(), anDynamicParameter.getValueMax()));
            }
            if (anDynamicParameters.size() != 0) {
                AnDynamicDivisor anDynamicDivisor = iAnDynamicDivisorService.getDynamicDivisorByDeviceId(anDynamicParameters.get(0).getDeviceId());
                divisorParameter.put(anDynamicDivisor.getDivisorCode(), property);
            } else {
                divisorParameter.put("null", property);
            }
        } else {
            List<AnDivisorParameter> anDivisorParameters = new ArrayList(parameters);
            for (AnDivisorParameter anDivisorParameter : anDivisorParameters) {
                HashMap<String, String> property = new HashMap<>();
                property.put("Avg", RandomUtil.getRandomString(4, anDivisorParameter.getAvgMin(), anDivisorParameter.getAvgMax()));
                property.put("Max", String.valueOf(anDivisorParameter.getMax()));
                property.put("Min", String.valueOf(anDivisorParameter.getMin()));
                property.put("Cou", String.valueOf(anDivisorParameter.getCou()));
                property.put("ZsAvg", String.valueOf(anDivisorParameter.getZavg()));
                property.put("ZsMax", String.valueOf(anDivisorParameter.getZmax()));
                property.put("ZsMin", String.valueOf(anDivisorParameter.getZmin()));
                property.put("Flag", anDivisorParameter.getFlag());

                divisorParameter.put(anDivisorParameter.getDivisorCode(), property);
            }
        }
        return divisorParameter;
    }

    /**
     * 获取实时数据组装报文
     *
     * @param sysDevice 设备对象
     * @param dataType  实时（1）\分钟（2）\小时（3）\日（4）\参数（5）\状态（6）
     * @return 返回组装好的数据包包
     */
    public String getRealTimeDataPackage(SysDevice sysDevice, Date date, HashMap<String, Map<String, String>> divisorParameter, Integer pnum, Integer pno, Integer dataType) {
        AnDataType anDataType = this.getCounDataTypeByDeviceId(sysDevice.getId(), dataType);
        String link = null;
        String polId;
        String subpackage = "";
        if (sysDevice.getSubpackage() == 1) {
            subpackage = "PNUM=" + pnum + ";PNO=" + pno + ";";
        }
        switch (dataType) {
            case 1:
                link = "##0235QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2011;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "second", -30) + ";"
                        + getParameterPackage(divisorParameter, "realTime", anDataType.getZs()) + "&&B381";
                break;
            case 2:
                link = "##0178QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2051;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "minute", -10) + ";"
                        + getParameterPackage(divisorParameter, "history", anDataType.getZs()) + "&&B381";
                break;
            case 3:
                link = "##0160QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2061;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "hour", -1) + ";" + getParameterPackage(divisorParameter, "history", anDataType.getZs())
                        + "&&B381";
                break;
            case 4:
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2031;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "day", -1) + ";" + getParameterPackage(divisorParameter, "history", anDataType.getZs())
                        + "&&B381";
                break;
            case 5:
            case 6:
                //获取编码，只考虑一个的情况
                polId = divisorParameter.keySet().iterator().next();
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "second", 0) + ";PolId=" + polId + ";"
                        + getParameterPackage(divisorParameter, "dynamic", null) + "&&B381";
                break;
//            case 6:
//                //获取编码，只考虑一个的情况
//                polId = divisorParameter.keySet().iterator().next();
//                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
//                        + sysDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "second", 0) + ";PolId=" + polId + ";"
//                        + getParameterPackage(divisorParameter, "status", analogDataType.getZs()) + "&&B381";
//                break;
        }
        if (link != null && "05".equals(sysDevice.getAgreement()) && 1 == dataType) {
            int indexOf = link.indexOf("ST=");
            link = link.substring(indexOf, link.length());
        } else if (link != null && "05".equals(sysDevice.getAgreement()) && 1 != dataType) {
            int indexOf = link.indexOf("ST=");
            link = link.substring(indexOf, link.length());
            String regex = "[,;]\\w+-Flag=[a-zA-Z]";
            link = link.replaceAll(regex, "");
        }
        //校验数据包和进行crc计算
        link = DataPackageUtils.composeDataPackage(DataPackageUtils.positiveExpression(link), false);
        return link;
    }

    /**
     * 获取补发报文内容
     *
     * @param sysDevice      设备对象
     * @param date           补发时间
     * @param anDataType 数据类型对象
     * @return 返回组装好的数据包
     */
    private String getSupplyAgainDataPackage(SysDevice sysDevice, Date date, HashMap<String, Map<String, String>> divisorParameter, Integer pnum, Integer pno, AnDataType anDataType) {
        String link = null;
        String polId;
        String subpackage = "";
        if (sysDevice.getSubpackage() == 1) {
            subpackage = "PNUM=" + pnum + ";PNO=" + pno + ";";
        }
        switch (anDataType.getDataType()) {
            case 1:
                link = "##0235QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2011;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatTime(date, "second") + ";"
                        + getParameterPackage(divisorParameter, "realTime", anDataType.getZs()) + "&&B381";
                break;
            case 2:
                link = "##0178QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2051;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatTime(date, "minute") + ";"
                        + getParameterPackage(divisorParameter, "history", anDataType.getZs()) + "&&B381";
                break;
            case 3:
                link = "##0160QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2061;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatTime(date, "hour") + ";" + getParameterPackage(divisorParameter, "history", anDataType.getZs())
                        + "&&B381";
                break;
            case 4:
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2031;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatTime(date, "day") + ";" + getParameterPackage(divisorParameter, "history", anDataType.getZs())
                        + "&&B381";
                break;
            case 5:
                //获取编码，只考虑一个的情况
                polId = divisorParameter.keySet().iterator().next();
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatTime(date, "second") + ";PolId=" + polId + ";"
                        + getParameterPackage(divisorParameter, "parameter", anDataType.getZs()) + "&&B381";
                break;
//            case 6:
//                //获取编码，只考虑一个的情况
//                polId = divisorParameter.keySet().iterator().next();
//                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
//                        + sysDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatTime(date, "second") + ";PolId=" + polId + ";"
//                        + getParameterPackage(divisorParameter, "status", analogDataType.getZs()) + "&&B381";
//                break;
        }
        if (link != null && "05".equals(sysDevice.getAgreement()) && 1 == anDataType.getDataType()) {
            int indexOf = link.indexOf("ST=");
            link = link.substring(indexOf, link.length());
        } else if (link != null && "05".equals(sysDevice.getAgreement()) && 1 != anDataType.getDataType()) {
            int indexOf = link.indexOf("ST=");
            link = link.substring(indexOf, link.length());
            String regex = "[,;]\\w+-Flag=[a-zA-Z]";
            link = link.replaceAll(regex, "");
        }
        //校验数据包和进行crc计算
        link = DataPackageUtils.composeDataPackage(DataPackageUtils.positiveExpression(link), false);
        return link;
    }

    /**
     * 拼接监测因子
     *
     * @param divisorParameter 因子参数 key：w00001...,value：{'max':'1'、'min':'2'、'i12001':'3'...}
     * @param key              实时（realTime）/非实时：分钟、小时、日（history）/动态：参数、状态（dynamic）
     * @param zs               合（join）/分（divide）/没有（none）
     * @return 拼接的监测因子
     */
    private String getParameterPackage(Map<String, Map<String, String>> divisorParameter, String key, String zs) {
        Set<String> keySet;
        List<String> coding;
        StringBuffer buffer = new StringBuffer();
        switch (key) {
            case "realTime":
                keySet = divisorParameter.keySet();
                coding = new ArrayList<>(keySet);
                for (String cod : coding) {
                    String toJSONString = JSON.toJSONString(divisorParameter.get(cod));
                    JSONObject parseObject = JSON.parseObject(toJSONString);
                    if (cod.equals(coding.get(coding.size() - 1))) {
                        if ("divide".equals(zs)) {
                            buffer.append(cod + "-ZsRtd=" + parseObject.getString("ZsAvg") + ";");
                        }
                        buffer.append(cod + "-Rtd=" + parseObject.getString("Avg") + ",");
                        if ("join".equals(zs)) {
                            buffer.append(cod + "-ZsRtd=" + parseObject.getString("ZsAvg") + ",");
                        }
                        buffer.append(cod + "-Flag=" + parseObject.getString("Flag"));
                    } else {
                        if ("divide".equals(zs)) {
                            buffer.append(cod + "-ZsRtd=" + parseObject.getString("ZsAvg") + ";");
                        }
                        buffer.append(cod + "-Rtd=" + parseObject.getString("Avg") + ",");
                        if ("join".equals(zs)) {
                            buffer.append(cod + "-ZsRtd=" + parseObject.getString("ZsAvg") + ",");
                        }
                        buffer.append(cod + "-Flag=" + parseObject.getString("Flag") + ";");
                    }
                }
                return buffer.toString();
            case "history":
                keySet = divisorParameter.keySet();
                coding = new ArrayList<>(keySet);
                //遍历因子编码
                for (String cod : coding) {
                    String toJSONString = JSON.toJSONString(divisorParameter.get(cod));
                    JSONObject parseObject = JSON.parseObject(toJSONString);
                    keySet = parseObject.keySet();
                    ArrayList<String> arrayList = new ArrayList<>(keySet);
                    String divideZs = "";
                    //遍历avg、max、min等
                    for (String name : keySet) {
                        //最后一个因子编码并且是最后一个属性
                        if (name.equals(arrayList.get(arrayList.size() - 1)) && cod.equals(coding.get(coding.size() - 1))) {
                            //zs是合
                            if ("join".equals(zs)) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name));
                                //zs是分
                            } else if ("divide".equals(zs)) {
                                if (!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")) {
                                    buffer.append(cod + "-" + name + "=" + parseObject.getString(name));
                                } else {
                                    log.error("出现最后一个属性为折算属性");
                                }
                            } else if (!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name));
                            }
                            //不是最后一个因子，但是是最后一个属性
                        } else if (name.equals(arrayList.get(arrayList.size() - 1))) {
                            if ("join".equals(zs)) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ";");
                            } else if ("divide".equals(zs)) {
                                if (!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")) {
                                    buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ";");
                                } else {
                                    log.error("出现最后一个属性为折算属性");
                                }
                            } else if (!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ";");
                            }
                            //不是最后一个因子
                        } else {
                            if ("join".equals(zs)) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ",");
                            } else if ("divide".equals(zs)) {
                                if (!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")) {
                                    buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ",");
                                } else {
                                    divideZs += cod + "-" + name + "=" + parseObject.getString(name) + ",";
                                }
                            } else if (!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ",");
                            }
                        }
                    }
                    //添加分开的折算因子
                    if (cod.equals(coding.get(coding.size() - 1)) && "divide".equals(zs)) {
                        divideZs = divideZs.substring(0, divideZs.length() - 1);
                        buffer.append(";" + divideZs);
                    } else if ("divide".equals(zs)) {
                        divideZs = divideZs.substring(0, divideZs.length() - 1);
                        buffer.append(divideZs + ";");
                    }
                }
                return buffer.toString();
            case "dynamic":
                String next = divisorParameter.keySet().iterator().next();
                Map<String, String> stringStringMap = divisorParameter.get(next);
                keySet = stringStringMap.keySet();
                Iterator<String> iterator = keySet.iterator();
                //遍历因子编码
                while (iterator.hasNext()) {
                    String parameterKey = iterator.next();
                    String parameterValue = stringStringMap.get(parameterKey);
                    buffer.append(parameterKey + "-Info=" + parameterValue + ";");
                }
                if (buffer.length() != 0) {
                    buffer.deleteCharAt(buffer.length() - 1);
                }


                return buffer.toString();
//            case "status":
//                keySet = divisorParameter.keySet();
//                coding = new ArrayList<>(keySet);
//                for (String cod : coding) {
//                    String toJSONString = JSON.toJSONString(divisorParameter.get(cod));
//                    JSONObject jsonObject = JSON.parseObject(toJSONString);
//                    keySet = jsonObject.keySet();
//                    ArrayList<String> arrayList = new ArrayList<>(keySet);
//                    for (int i = 0; i < arrayList.size(); i++) {
//                        if (arrayList.get(i).indexOf("i12") != -1) {
//                            li.add(arrayList.get(i));
//                        }
//                    }
//                    for (int i = 0; i < li.size(); i++) {
//                        if (li.size() - 1 == i) {
//                            buffer.append(li.get(i) + "-Info=" + jsonObject.getString(li.get(i)));
//                        } else {
//                            buffer.append(li.get(i) + "-Info=" + jsonObject.getString(li.get(i)) + ";");
//                        }
//                    }
//                }
//                return buffer.toString();
        }
        return "Sorry 没找到你需要找的内容";
    }
}
