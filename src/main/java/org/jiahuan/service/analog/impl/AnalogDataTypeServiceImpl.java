package org.jiahuan.service.analog.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.config.CustomWebSocketConfig;
import org.jiahuan.common.util.DataPackageUtils;
import org.jiahuan.common.util.RandomUtil;
import org.jiahuan.common.util.TimeUtil;
import org.jiahuan.entity.analog.AnalogCode;
import org.jiahuan.entity.analog.AnalogCodeParameter;
import org.jiahuan.entity.analog.AnalogDataType;
import org.jiahuan.entity.analog.AnalogDivisorParameter;
import org.jiahuan.entity.sys.SysDevice;
import org.jiahuan.mapper.analog.AnalogDataTypeMapper;
import org.jiahuan.service.analog.IAnalogCodeParameterService;
import org.jiahuan.service.analog.IAnalogDataTypeService;
import org.jiahuan.service.analog.IAnalogDivisorParameterService;
import org.jiahuan.service.analog.IConnectionObj;
import org.jiahuan.service.sys.ISysDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
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
public class AnalogDataTypeServiceImpl extends ServiceImpl<AnalogDataTypeMapper, AnalogDataType> implements IAnalogDataTypeService {

    private Map<Integer, Boolean> supplyAgainStatus = new HashMap<>();

    @Autowired
    private ISysDeviceService iSysDeviceService;
    @Autowired
    private IAnalogDivisorParameterService iAnalogDivisorParameterService;
    @Autowired
    @Lazy
    private IAnalogDataTypeService iAnalogDataTypeService;
    @Autowired
    private IAnalogCodeParameterService iAnalogCodeParameterService;
    @Autowired
    private CustomWebSocketConfig customWebSocketConfig;
    @Autowired
    private IConnectionObj iConnectionObj;

    @Override
    public AnalogDataType getCounDataTypeByDeviceId(Integer deviceId, Integer dataType) {
        QueryWrapper<AnalogDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        queryWrapper.eq("data_type", dataType);
        AnalogDataType analogDataType = iAnalogDataTypeService.getOne(queryWrapper);
        return analogDataType;
    }

    @Override
    public List<AnalogDataType> getListCounDataTypeByDeviceId(Integer deviceId) {
        QueryWrapper<AnalogDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        List<AnalogDataType> analogDataTypes = iAnalogDataTypeService.list(queryWrapper);
        return analogDataTypes;
    }

    @Override
    public void addInitByDeviceId(Integer deviceId) {
        AnalogDataType analogDataType2011 = new AnalogDataType(deviceId, 1, 0, "none", 30, TimeUtil.getProcessedCurrentTime("second", -30), TimeUtil.getProcessedCurrentTime("second", 0));
        AnalogDataType analogDataType2051 = new AnalogDataType(deviceId, 2, 0, "none", 10, TimeUtil.getProcessedCurrentTime("minute", -10), TimeUtil.getProcessedCurrentTime("minute", 0));
        AnalogDataType analogDataType2061 = new AnalogDataType(deviceId, 3, 0, "none", 1, TimeUtil.getProcessedCurrentTime("hour", -1), TimeUtil.getProcessedCurrentTime("hour", 0));
        AnalogDataType analogDataType2031 = new AnalogDataType(deviceId, 4, 0, "none", 1, TimeUtil.getProcessedCurrentTime("day", -1), TimeUtil.getProcessedCurrentTime("day", 0));

        iAnalogDataTypeService.save(analogDataType2011);
        iAnalogDataTypeService.save(analogDataType2051);
        iAnalogDataTypeService.save(analogDataType2061);
        iAnalogDataTypeService.save(analogDataType2031);
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<AnalogDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        iAnalogDataTypeService.remove(queryWrapper);
    }


    @Override
    public void sendRealTime(Integer deviceId, Integer dataType) throws Exception {
        List<String> dataPack=new ArrayList<>();
        SysDevice sysDevice = iSysDeviceService.getById(deviceId);
        List<AnalogDivisorParameter> analogDivisorParameters = iAnalogDivisorParameterService.getCounDivisorByDeviceId(deviceId);

        List<Object> divisorParameters = new ArrayList<>();
        int pnum = 1;
        int pon = 1;
        Date date = new Date();
        //判断是否需要包头
        if (sysDevice.getSubpackage() == 1) {
            if (analogDivisorParameters.size() % sysDevice.getSubpackageNumber() == 0) {
                pnum = analogDivisorParameters.size() / sysDevice.getSubpackageNumber();
            } else {
                pnum = analogDivisorParameters.size() / sysDevice.getSubpackageNumber() + 1;
            }
        }

        //判断是否需要分包
        if (sysDevice.getSubpackage() != 0) {

            for (int i = 1; i <= analogDivisorParameters.size(); i++) {
                divisorParameters.add(analogDivisorParameters.get(i - 1));
                //判断是否满足分包数
                if (i % sysDevice.getSubpackageNumber() == 0) {
                    HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                    divisorParameters.clear();
                    //获取实时数据包
                    String message = getRealTimeDataPackage(sysDevice, date, divisorParameterMap, pnum, pon, dataType, false);
                    iAnalogDataTypeService.sendMessage(sysDevice.getId(), message,dataPack);
                    pon++;
                } else if (i == analogDivisorParameters.size()) {
                    HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                    //获取实时数据包
                    String message = getRealTimeDataPackage(sysDevice, date, divisorParameterMap, pnum, pon, dataType, false);
                    iAnalogDataTypeService.sendMessage(deviceId, message,dataPack);
                }
            }
        } else {
            divisorParameters = new ArrayList<>(analogDivisorParameters);
            HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
            //获取实时数据包
            String message = getRealTimeDataPackage(sysDevice, date, divisorParameterMap, pnum, pon, dataType, false);
            iAnalogDataTypeService.sendMessage(deviceId, message,dataPack);
        }
        customWebSocketConfig.customWebSocketHandler().sendMessageToUser(String.valueOf(sysDevice.getId()), new TextMessage(dataPack.toString()));
    }

    @Override
    public String getDataPackage(Integer deviceId, Integer dataType, boolean is3020) throws IOException {
        SysDevice sysDevice = iSysDeviceService.getById(deviceId);
        List<AnalogDivisorParameter> analogDivisorParameters = iAnalogDivisorParameterService.getCounDivisorByDeviceId(deviceId);
        List<Object> divisorParameters = new ArrayList<>(analogDivisorParameters);
        HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
        //获取实时数据包
        String message = getRealTimeDataPackage(sysDevice, new Date(), divisorParameterMap, 1, 1, dataType, false);
        return message;
    }


    @Override
    public void sendSupplyAgain(Integer deviceId, Integer dataType) throws Exception {
        List<String> dataPacks=new ArrayList<>();
        AnalogDataType analogDataType = iAnalogDataTypeService.getCounDataTypeByDeviceId(deviceId, dataType);
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
        startCalendar.setTime(analogDataType.getStartTime());
        endCalendar.setTime(analogDataType.getEndTime());

        if (!supplyAgainStatus.containsKey(deviceId)) {
            supplyAgainStatus.put(deviceId, true);
        }

        List<AnalogDivisorParameter> analogDivisorParameters = iAnalogDivisorParameterService.getCounDivisorByDeviceId(deviceId);

        List<Object> divisorParameters = new ArrayList<>();
        int pnum = 1;
        int pon = 1;
        int count = 0;

        //计算总包数
        if (sysDevice.getSubpackage() == 1) {
            if (analogDivisorParameters.size() % sysDevice.getSubpackageNumber() == 0) {
                pnum = analogDivisorParameters.size() / sysDevice.getSubpackageNumber();
            } else {
                pnum = analogDivisorParameters.size() / sysDevice.getSubpackageNumber() + 1;
            }
        }
        //时间遍历
        while (startCalendar.getTimeInMillis() - endCalendar.getTimeInMillis() <= 0 && supplyAgainStatus.get(deviceId)) {

            for (int i = 1; i <= analogDivisorParameters.size(); i++) {
                divisorParameters.add(analogDivisorParameters.get(i - 1));
                //判断是否需要分包
                if (sysDevice.getSubpackage() != 0) {
                    //判断是否满足分包数
                    if (i % sysDevice.getSubpackageNumber() == 0) {
                        HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                        divisorParameters.clear();
                        //获取补发数据包
                        String dataPackage = getSupplyAgainDataPackage(sysDevice, startCalendar.getTime(), divisorParameterMap, pnum, pon, analogDataType, false);
                        //发送消息
                        this.sendMessage(deviceId, dataPackage,dataPacks);
                        pon++;
                        //最后一个包因子不足则直接发送
                    } else if (i == analogDivisorParameters.size()) {
                        HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                        //获取补发数据包
                        String dataPackage = getSupplyAgainDataPackage(sysDevice, startCalendar.getTime(), divisorParameterMap, pnum, pon, analogDataType, false);
//发送消息
                        this.sendMessage(deviceId, dataPackage,dataPacks);
                    }
                } else if (i == analogDivisorParameters.size()) {
                    HashMap<String, Map<String, String>> divisorParameterMap = getDivisorParameterMap(divisorParameters, false);
                    //获取补发数据包
                    String dataPackage = getSupplyAgainDataPackage(sysDevice, startCalendar.getTime(), divisorParameterMap, pnum, pon, analogDataType, false);
                    //发送消息
                    iAnalogDataTypeService.sendMessage(deviceId, dataPackage,dataPacks);
                }
            }
            //添加时间
            startCalendar.add(field, analogDataType.getDateInterval());
            pon = 1;
            count++;
        }
        if(dataPacks.size()>500){
            dataPacks.subList(dataPacks.size()-500,dataPacks.size());
            dataPacks.add(0, "已超过500条，只保留500条数据");
        }
        customWebSocketConfig.customWebSocketHandler().sendMessageToUser(String.valueOf(sysDevice.getId()), new TextMessage(dataPacks.toString()));
        if (supplyAgainStatus.get(deviceId)) {
            customWebSocketConfig.customWebSocketHandler().sendMessageToUser(String.valueOf(sysDevice.getId()), new TextMessage("发送完成，本次共补发" + dataTypeStr + "数据：" + count + " 条"));
        } else {
            supplyAgainStatus.put(deviceId, true);
            customWebSocketConfig.customWebSocketHandler().sendMessageToUser(String.valueOf(sysDevice.getId()), new TextMessage("终止成功，本次共补发" + dataTypeStr + "数据：" + count + " 条"));
        }

    }


    @Override
    public void cancelSupplyAgain(Integer deviceId) throws IOException {
        supplyAgainStatus.put(deviceId, false);
    }

    @Override
    public int getSupplyAgainCount(Integer deviceId, Integer dataType) {
        AnalogDataType analogDataType = iAnalogDataTypeService.getCounDataTypeByDeviceId(deviceId, dataType);
        //处理时间
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(analogDataType.getStartTime());
        endCalendar.setTime(analogDataType.getEndTime());

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
            startCalendar.add(field, analogDataType.getDateInterval());
            count++;
        }
        return count;
    }


    @SneakyThrows
    @Override
    public void sendParam3020(Integer deviceId, Integer dataType) {
        SysDevice sysDevice = iSysDeviceService.getById(deviceId);
        //获取3020数据包
//        String message = getRealTimeDataPackage(counDevice , dataType, true);
//        iCounDataTypeService.sendMessage(counDevice, message);
    }

    @Override
    public void sendMessage(Integer deviceId, String message,List<String> dataPack) throws Exception {
        OutputStream outputStream = iConnectionObj.getOutputStream(deviceId);
        if (message.indexOf("\r\n") == -1) {
            message += "\r\n";
        }
        try {
            outputStream.write(message.getBytes());
            dataPack.add(message);
        } catch (Exception e) {
            iConnectionObj.cleanConnetion(deviceId, true);
            throw e;
        }
    }

    /**
     * 发送到服务器
     *
     * @param ip      服务器地址：192.168.1.1
     * @param port    8080
     * @param message crc校验过的内容
     * @throws IOException
     */
    private void sendData(String ip, int port, String message) throws IOException {
        Socket socket = new Socket(ip, port);
        OutputStream outputStream = socket.getOutputStream();
        message += "\r\n";
        outputStream.write(message.getBytes());
        log.info(message);
        outputStream.close();
        socket.close();
    }

    /**
     * 获取因子参数map
     *
     * @param parameters 3020则传code对象，否则传因子参数对象
     * @param is3020     是否是3020数据
     * @return 返回组装好的因子参数map key：w00001...,value：{'max':'1'、'min':'2'、'i12001':'3'...}
     */
    public HashMap<String, Map<String, String>> getDivisorParameterMap(List<Object> parameters, boolean is3020) {
        HashMap<String, Map<String, String>> divisorParameter = new HashMap<>();
        if (is3020) {
            AnalogCode analogCode = (AnalogCode) parameters.get(0);
            List<AnalogCodeParameter> analogCodeParameters = iAnalogCodeParameterService.getCounParameterByCodeId(analogCode.getId());
            HashMap<String, String> property = new HashMap<>();
            for (AnalogCodeParameter analogCodeParameter : analogCodeParameters) {
                property.put(analogCodeParameter.getKey(), analogCodeParameter.getValue());
            }
            divisorParameter.put(analogCode.getCode(), property);
        } else {
            List<AnalogDivisorParameter> analogDivisorParameters = new ArrayList(parameters);
            for (AnalogDivisorParameter analogDivisorParameter : analogDivisorParameters) {
                HashMap<String, String> property = new HashMap<>();
                property.put("Avg", RandomUtil.getRandomString(4, analogDivisorParameter.getAvgMin(), analogDivisorParameter.getAvgMax()));
                property.put("Max", String.valueOf(analogDivisorParameter.getMax()));
                property.put("Min", String.valueOf(analogDivisorParameter.getMin()));
                property.put("Cou", String.valueOf(analogDivisorParameter.getCou()));
                property.put("ZsAvg", String.valueOf(analogDivisorParameter.getZavg()));
                property.put("ZsMax", String.valueOf(analogDivisorParameter.getZmax()));
                property.put("ZsMin", String.valueOf(analogDivisorParameter.getZmin()));
                property.put("Flag", analogDivisorParameter.getFlag());

                divisorParameter.put(analogDivisorParameter.getDivisorCode(), property);
            }
        }
        return divisorParameter;
    }

    /**
     * 获取实时数据组装报文
     *
     * @param sysDevice 设备对象
     * @param dataType  实时（1）\分钟（2）\小时（3）\日（4）\参数（5）\状态（6）
     * @param is3020    是否是3020数据
     * @return 返回组装好的数据包包
     */
    public String getRealTimeDataPackage(SysDevice sysDevice, Date date, HashMap<String, Map<String, String>> divisorParameter, Integer pnum, Integer pno, Integer dataType, boolean is3020) {
        AnalogDataType analogDataType = iAnalogDataTypeService.getCounDataTypeByDeviceId(sysDevice.getId(), dataType);
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
                        + getParameterPackage(divisorParameter, "realTime", analogDataType.getZs()) + "&&B381";
                break;
            case 2:
                link = "##0178QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2051;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "minute", -10) + ";"
                        + getParameterPackage(divisorParameter, "history", analogDataType.getZs()) + "&&B381";
                break;
            case 3:
                link = "##0160QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2061;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "hour", -1) + ";" + getParameterPackage(divisorParameter, "history", analogDataType.getZs())
                        + "&&B381";
                break;
            case 4:
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2031;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "day", -1) + ";" + getParameterPackage(divisorParameter, "history", analogDataType.getZs())
                        + "&&B381";
                break;
            case 5:
                //获取编码，只考虑一个的情况
                polId = divisorParameter.keySet().iterator().next();
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "second", 0) + ";PolId=" + polId + ";"
                        + getParameterPackage(divisorParameter, "parameter", analogDataType.getZs()) + "&&B381";
                break;
            case 6:
                //获取编码，只考虑一个的情况
                polId = divisorParameter.keySet().iterator().next();
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatCurrentTime(date, "second", 0) + ";PolId=" + polId + ";"
                        + getParameterPackage(divisorParameter, "status", analogDataType.getZs()) + "&&B381";
                break;
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
     * @param sysDevice 设备对象
     * @param date      补发时间
     * @param analogDataType  数据类型对象
     * @param is3020    是否是3020数据包
     * @return 返回组装好的数据包
     */
    private String getSupplyAgainDataPackage(SysDevice sysDevice, Date date, HashMap<String, Map<String, String>> divisorParameter, Integer pnum, Integer pno, AnalogDataType analogDataType, boolean is3020) {
        String link = null;
        String polId;
        String subpackage = "";
        if (sysDevice.getSubpackage() == 1) {
            subpackage = "PNUM=" + pnum + ";PNO=" + pno + ";";
        }
        switch (analogDataType.getDataType()) {
            case 1:
                link = "##0235QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2011;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatTime(date, "second") + ";"
                        + getParameterPackage(divisorParameter, "realTime", analogDataType.getZs()) + "&&B381";
                break;
            case 2:
                link = "##0178QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2051;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatTime(date, "minute") + ";"
                        + getParameterPackage(divisorParameter, "history", analogDataType.getZs()) + "&&B381";
                break;
            case 3:
                link = "##0160QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2061;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatTime(date, "hour") + ";" + getParameterPackage(divisorParameter, "history", analogDataType.getZs())
                        + "&&B381";
                break;
            case 4:
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=2031;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;" + subpackage + "CP=&&DataTime=" + TimeUtil.getFormatTime(date, "day") + ";" + getParameterPackage(divisorParameter, "history", analogDataType.getZs())
                        + "&&B381";
                break;
            case 5:
                //获取编码，只考虑一个的情况
                polId = divisorParameter.keySet().iterator().next();
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatTime(date, "second") + ";PolId=" + polId + ";"
                        + getParameterPackage(divisorParameter, "parameter", analogDataType.getZs()) + "&&B381";
                break;
            case 6:
                //获取编码，只考虑一个的情况
                polId = divisorParameter.keySet().iterator().next();
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime(new Date(), "millisecond", 0) + ";ST=" + sysDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
                        + sysDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatTime(date, "second") + ";PolId=" + polId + ";"
                        + getParameterPackage(divisorParameter, "status", analogDataType.getZs()) + "&&B381";
                break;
        }
        if (link != null && "05".equals(sysDevice.getAgreement()) && 1 == analogDataType.getDataType()) {
            int indexOf = link.indexOf("ST=");
            link = link.substring(indexOf, link.length());
        } else if (link != null && "05".equals(sysDevice.getAgreement()) && 1 != analogDataType.getDataType()) {
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
     * @param key              实时（realTime）/非实时，分钟、小时、日（history）/参数（parameter）/状态（status）
     * @param zs               合（join）/分（divide）/没有（none）
     * @return 拼接的监测因子
     */
    private String getParameterPackage(Map<String, Map<String, String>> divisorParameter, String key, String zs) {
        Set<String> keySet;
        List<String> coding;
        List<String> li = new ArrayList<>();
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
            case "parameter":
                keySet = divisorParameter.keySet();
                coding = new ArrayList<>(keySet);
                //遍历因子编码
                for (String cod : coding) {
                    String toJSONString = JSON.toJSONString(divisorParameter.get(cod));
                    JSONObject jsonObject = JSON.parseObject(toJSONString);
                    keySet = jsonObject.keySet();
                    ArrayList<String> arrayList = new ArrayList<>(keySet);
                    //遍历i12001、i13001等
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).indexOf("i12") == -1) {
                            li.add(arrayList.get(i));
                        }
                    }
                    for (int i = 0; i < li.size(); i++) {
                        if (li.size() - 1 == i) {
                            buffer.append(li.get(i) + "-Info=" + jsonObject.getString(li.get(i)));
                        } else {
                            buffer.append(li.get(i) + "-Info=" + jsonObject.getString(li.get(i)) + ";");
                        }
                    }
                }
                return buffer.toString();
            case "status":
                keySet = divisorParameter.keySet();
                coding = new ArrayList<>(keySet);
                for (String cod : coding) {
                    String toJSONString = JSON.toJSONString(divisorParameter.get(cod));
                    JSONObject jsonObject = JSON.parseObject(toJSONString);
                    keySet = jsonObject.keySet();
                    ArrayList<String> arrayList = new ArrayList<>(keySet);
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).indexOf("i12") != -1) {
                            li.add(arrayList.get(i));
                        }
                    }
                    for (int i = 0; i < li.size(); i++) {
                        if (li.size() - 1 == i) {
                            buffer.append(li.get(i) + "-Info=" + jsonObject.getString(li.get(i)));
                        } else {
                            buffer.append(li.get(i) + "-Info=" + jsonObject.getString(li.get(i)) + ";");
                        }
                    }
                }
                return buffer.toString();
        }
        return "Sorry 没找到你需要找的内容";
    }
}
