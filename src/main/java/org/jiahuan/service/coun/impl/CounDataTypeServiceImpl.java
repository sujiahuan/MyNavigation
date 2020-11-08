package org.jiahuan.service.coun.impl;

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
import org.jiahuan.entity.coun.*;
import org.jiahuan.mapper.coun.CounDataTypeMapper;
import org.jiahuan.service.coun.*;
import org.jiahuan.service.sys.ISysDivisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
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
public class CounDataTypeServiceImpl extends ServiceImpl<CounDataTypeMapper, CounDataType> implements ICounDataTypeService {

    private Map<Integer, Boolean> supplyAgainStatus = new HashMap<>();

    @Autowired
    private ICounDeviceService iCounDeviceService;
    @Autowired
    private ICounDivisorService iCounDivisorService;
    @Autowired
    @Lazy
    private ICounDataTypeService iCounDataTypeService;
    @Autowired
    private ICounParameterService iCounParameterService;
    @Autowired
    private ICounCodeService iCounCodeService;
    @Autowired
    private CustomWebSocketConfig customWebSocketConfig;
    @Autowired
    private ISysDivisorService iSysDivisorService;
    @Autowired
    private IConnectionObj iConnectionObj;
    @Autowired
    private ICounCounterchargeService iCounCounterchargeService;

    @Override
    public CounDataType getCounDataTypeByDeviceId(Integer deviceId, Integer dataType) {
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        queryWrapper.eq("data_type", dataType);
        CounDataType counDataType = iCounDataTypeService.getOne(queryWrapper);
        return counDataType;
    }

    @Override
    public List<CounDataType> getListCounDataTypeByDeviceId(Integer deviceId) {
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        List<CounDataType> counDataTypes = iCounDataTypeService.list(queryWrapper);
        return counDataTypes;
    }

    @Override
    public void addInitByDeviceId(Integer deviceId) {
        CounDataType counDataType2011 = new CounDataType(deviceId, 1, 0, "none", 30, TimeUtil.getProcessedCurrentTime("second", -30), TimeUtil.getProcessedCurrentTime("second", 0));
        CounDataType counDataType2051 = new CounDataType(deviceId, 2, 0, "none", 10, TimeUtil.getProcessedCurrentTime("minute", -10), TimeUtil.getProcessedCurrentTime("minute", 0));
        CounDataType counDataType2061 = new CounDataType(deviceId, 3, 0, "none", 1, TimeUtil.getProcessedCurrentTime("hour", -1), TimeUtil.getProcessedCurrentTime("hour", 0));
        CounDataType counDataType2031 = new CounDataType(deviceId, 4, 0, "none", 1, TimeUtil.getProcessedCurrentTime("day", -1), TimeUtil.getProcessedCurrentTime("day", 0));

        iCounDataTypeService.save(counDataType2011);
        iCounDataTypeService.save(counDataType2051);
        iCounDataTypeService.save(counDataType2061);
        iCounDataTypeService.save(counDataType2031);
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<CounDataType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        iCounDataTypeService.remove(queryWrapper);
    }


    @Override
    public void sendRealTime(Integer deviceId, String agreement, Integer dataType) throws IOException {
        CounDevice counDevice = iCounDeviceService.getById(deviceId);
        //获取实时数据包
        String message = getRealTimeDataPackage(counDevice, agreement, dataType, false);
        iCounDataTypeService.sendMessage(counDevice, message);
    }


    @Override
    public void sendSupplyAgain(Integer deviceId, String agreement, Integer dataType) throws IOException {

        CounDataType counDataType = iCounDataTypeService.getCounDataTypeByDeviceId(deviceId, dataType);
        CounDevice counDevice = iCounDeviceService.getById(deviceId);
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
        startCalendar.setTime(counDataType.getStartTime());
        endCalendar.setTime(counDataType.getEndTime());

        if (!supplyAgainStatus.containsKey(deviceId)) {
            supplyAgainStatus.put(deviceId, true);
        }

        int count = 0;
        //时间遍历
        while (startCalendar.getTimeInMillis() - endCalendar.getTimeInMillis() <= 0 && supplyAgainStatus.get(deviceId)) {
            //获取补发数据包
            String dataPackage = getSupplyAgainDataPackage(counDevice, startCalendar.getTime(), agreement, dataType, false);
            //发送消息
            iCounDataTypeService.sendMessage(counDevice, dataPackage);
            //添加时间
            startCalendar.add(field, counDataType.getDateInterval());
            count++;
        }

        if (supplyAgainStatus.get(deviceId)) {
            customWebSocketConfig.customWebSocketHandler().sendMessageToUser(String.valueOf(counDevice.getId()), new TextMessage("发送完成，本次共补发" + dataTypeStr + "数据：" + count + " 条"));
        } else {
            supplyAgainStatus.put(deviceId, true);
            customWebSocketConfig.customWebSocketHandler().sendMessageToUser(String.valueOf(counDevice.getId()), new TextMessage("终止成功，本次共补发" + dataTypeStr + "数据：" + count + " 条"));
        }

    }


    @Override
    public void cancelSupplyAgain(Integer deviceId) throws IOException {
        supplyAgainStatus.put(deviceId, false);
    }

    @Override
    public int getSupplyAgainCount(Integer deviceId, Integer dataType) {
        CounDataType counDataType = iCounDataTypeService.getCounDataTypeByDeviceId(deviceId, dataType);
        //处理时间
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(counDataType.getStartTime());
        endCalendar.setTime(counDataType.getEndTime());

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
            startCalendar.add(field, counDataType.getDateInterval());
            count++;
        }
        return count;
    }


    @SneakyThrows
    @Override
    public void sendParam3020(Integer deviceId, String agreement, Integer dataType) {
        CounDevice counDevice = iCounDeviceService.getById(deviceId);
        //获取3020数据包
        String message = getRealTimeDataPackage(counDevice, agreement, dataType, true);
        iCounDataTypeService.sendMessage(counDevice, message);
    }


    @Override
    public void sendMessage(CounDevice counDevice, String message) throws IOException {
        OutputStream outputStream = iConnectionObj.getOutputStream(counDevice);
        if(message.indexOf("\r\n")==-1){
            message +="\r\n";
        }
        try{
            outputStream.write(message.getBytes());
        }catch (SocketException e) {
            if(e.getMessage().equals("Software caused connection abort: socket write error")){
               iConnectionObj.cleanConnetion(counDevice.getId(),true);
               iCounCounterchargeService.closeConnection(counDevice.getId());
            }
            throw e;
        }
        customWebSocketConfig.customWebSocketHandler().sendMessageToUser(String.valueOf(counDevice.getId()), new TextMessage(message+"\r\n"));
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
     * @param deviceId 设备id
     * @param is3020   是否是3020数据
     * @return 返回组装好的因子参数map key：w00001...,value：{'max':'1'、'min':'2'、'i12001':'3'...}
     */
    private HashMap<String, Map<String, String>> getDivisorParameterMap(Integer deviceId, boolean is3020) {
        HashMap<String, Map<String, String>> divisorParameter = new HashMap<>();
        if (is3020) {
            CounCode counCode = iCounCodeService.getCounCodeByDeviceId(deviceId);
            List<CounParameter> counParameters = iCounParameterService.getCounParameterByCodeId(counCode.getId());
            HashMap<String, String> property = new HashMap<>();
            for (CounParameter counParameter : counParameters) {
                property.put(counParameter.getKey(), counParameter.getValue());
            }
            divisorParameter.put(counCode.getCode(), property);
        } else {
            List<CounDivisor> counDivisors = iCounDivisorService.getCounDivisorByDeviceId(deviceId);
            for (CounDivisor counDivisor : counDivisors) {
                HashMap<String, String> property = new HashMap<>();
                property.put("Avg", RandomUtil.getRandomString(4, counDivisor.getAvgMin(), counDivisor.getAvgMax()));
                property.put("Max", String.valueOf(counDivisor.getMax()));
                property.put("Min", String.valueOf(counDivisor.getMin()));
                property.put("Cou", String.valueOf(counDivisor.getCou()));
                property.put("ZsAvg", String.valueOf(counDivisor.getZavg()));
                property.put("ZsMax", String.valueOf(counDivisor.getZmax()));
                property.put("ZsMin", String.valueOf(counDivisor.getZmin()));
                property.put("Flag", counDivisor.getFlag());

                divisorParameter.put(iSysDivisorService.getById(counDivisor.getCodeId()).getCode(), property);
            }
        }
        return divisorParameter;
    }

    /**
     * 获取实时数据组装报文
     *
     * @param counDevice 设备对象
     * @param agreement  协议
     * @param dataType   实时（1）\分钟（2）\小时（3）\日（4）\参数（5）\状态（6）
     * @param is3020     是否是3020数据
     * @return 返回组装好的数据包包
     */
    public String getRealTimeDataPackage(CounDevice counDevice, String agreement, Integer dataType, boolean is3020) {
        HashMap<String, Map<String, String>> divisorParameter = getDivisorParameterMap(counDevice.getId(), is3020);
        CounDataType counDataType = iCounDataTypeService.getCounDataTypeByDeviceId(counDevice.getId(), dataType);
        String link = null;
        String polId;
        switch (dataType) {
            case 1:
                link = "##0235QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=2011;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatCurrentTime("second", -30) + ";"
                        + getParameterPackage(divisorParameter, "realTime", counDataType.getZs()) + "&&B381";
                break;
            case 2:
                link = "##0178QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=2051;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatCurrentTime("minute", -10) + ";"
                        + getParameterPackage(divisorParameter, "history", counDataType.getZs()) + "&&B381";
                break;
            case 3:
                link = "##0160QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=2061;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatCurrentTime("hour", -1) + ";" + getParameterPackage(divisorParameter, "history", counDataType.getZs())
                        + "&&B381";
                break;
            case 4:
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=2031;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatCurrentTime("day", -1) + ";" + getParameterPackage(divisorParameter, "history", counDataType.getZs())
                        + "&&B381";
                break;
            case 5:
                //获取编码，只考虑一个的情况
                polId = divisorParameter.keySet().iterator().next();
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatCurrentTime("second", 0) + ";PolId=" + polId + ";"
                        + getParameterPackage(divisorParameter, "parameter", counDataType.getZs()) + "&&B381";
                break;
            case 6:
                //获取编码，只考虑一个的情况
                polId = divisorParameter.keySet().iterator().next();
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatCurrentTime("second", 0) + ";PolId=" + polId + ";"
                        + getParameterPackage(divisorParameter, "status", counDataType.getZs()) + "&&B381";
                break;
        }
        if (link != null && "05".equals(agreement) && 1 == dataType) {
            int indexOf = link.indexOf("ST=");
            link = link.substring(indexOf, link.length());
        } else if (link != null && "05".equals(agreement) && 1 != dataType) {
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
     * 获取报文内容
     *
     * @param counDevice 设备对象
     * @param date       补发时间
     * @param dataType   实时（1）\分钟（2）\小时（3）\日（4）\参数（5）\状态（6）
     * @param is3020     是否是3020数据包
     * @return 返回组装好的数据包
     */
    private String getSupplyAgainDataPackage(CounDevice counDevice, Date date, String agreement, Integer dataType, boolean is3020) {
        HashMap<String, Map<String, String>> divisorParameter = getDivisorParameterMap(counDevice.getId(), is3020);
        CounDataType counDataType = iCounDataTypeService.getCounDataTypeByDeviceId(counDevice.getId(), dataType);
        String link = null;
        String polId;
        switch (dataType) {
            case 1:
                link = "##0235QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=2011;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatTime(date, "second") + ";"
                        + getParameterPackage(divisorParameter, "realTime", counDataType.getZs()) + "&&B381";
                break;
            case 2:
                link = "##0178QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=2051;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatTime(date, "minute") + ";"
                        + getParameterPackage(divisorParameter, "history", counDataType.getZs()) + "&&B381";
                break;
            case 3:
                link = "##0160QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=2061;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatTime(date, "hour") + ";" + getParameterPackage(divisorParameter, "history", counDataType.getZs())
                        + "&&B381";
                break;
            case 4:
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=2031;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatTime(date, "day") + ";" + getParameterPackage(divisorParameter, "history", counDataType.getZs())
                        + "&&B381";
                break;
            case 5:
                //获取编码，只考虑一个的情况
                polId = divisorParameter.keySet().iterator().next();
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatTime(date, "second") + ";PolId=" + polId + ";"
                        + getParameterPackage(divisorParameter, "parameter", counDataType.getZs()) + "&&B381";
                break;
            case 6:
                //获取编码，只考虑一个的情况
                polId = divisorParameter.keySet().iterator().next();
                link = "##0171QN=" + TimeUtil.getFormatCurrentTime("millisecond", 0) + ";ST=" + counDevice.getMonitoringType() + ";CN=3020;PW=123456;MN="
                        + counDevice.getMn() + ";Flag=4;CP=&&DataTime=" + TimeUtil.getFormatTime(date, "second") + ";PolId=" + polId + ";"
                        + getParameterPackage(divisorParameter, "status", counDataType.getZs()) + "&&B381";
                break;
        }
        if (link != null && "05".equals(agreement) && 1 == dataType) {
            int indexOf = link.indexOf("ST=");
            link = link.substring(indexOf, link.length());
        } else if (link != null && "05".equals(agreement) && 1 != dataType) {
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
