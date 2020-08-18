package org.jiahuan.service.coun.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.util.DataPackageUtils;
import org.jiahuan.crc.Common;
import org.jiahuan.entity.coun.CounCountercharge;
import org.jiahuan.entity.coun.CounDevice;
import org.jiahuan.mapper.coun.CounCounterchargeMapper;
import org.jiahuan.service.coun.ICounCounterchargeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jiahuan.service.coun.ICounDataTypeService;
import org.jiahuan.service.coun.ICounDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jh
 * @since 2020-08-07
 */
@Service
@Slf4j
public class CounCounterchargeServiceImpl extends ServiceImpl<CounCounterchargeMapper, CounCountercharge> implements ICounCounterchargeService {

    @Autowired
    private ICounDeviceService iCounDeviceService;
    @Autowired
    private ICounDataTypeService iCounDataTypeService;
    @Autowired
    private ICounCounterchargeService iCounCounterchargeService;

//    private static Map<String,Socket> socketMap=new HashMap<>();
    private static Map<String,Integer> connetionStatus=new HashMap<>();

    @Override
    public CounCountercharge getCounCounterchargeByDeviceId(Integer deviceId) {
        QueryWrapper<CounCountercharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        CounCountercharge countercharge = iCounCounterchargeService.getOne(queryWrapper);
        return countercharge;
    }

    @Override
    public void openConnection(Integer deviceId) throws IOException {
        CounDevice counDevice = iCounDeviceService.getById(deviceId);
        CounCountercharge countercharge = iCounCounterchargeService.getCounCounterchargeByDeviceId(deviceId);
        Socket socket = new Socket(counDevice.getIp(),counDevice.getPort());
//        socketMap.put(counDevice.getMn(),socket);
        connetionStatus.put(counDevice.getMn(),1);
//        log.info("连接成功:{}", mnTypeAgreement);
        //获取输入流和输出流
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);

        new Thread(()->{

            try {
                closeConnection(deviceId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        //获取一条实时数据包
        String dataPackage = iCounDataTypeService.getRealTimeDataPackage(counDevice, counDevice.getAgreement(), 1, false);
        //发送
        outputStream.write(dataPackage.getBytes());

        //修改连接状态
        CounCountercharge counCountercharge = iCounCounterchargeService.getCounCounterchargeByDeviceId(deviceId);
        counCountercharge.setConnetionStatus(1);
        iCounCounterchargeService.save(counCountercharge);

        //
        while (connetionStatus.get(counDevice.getMn())==1) {
            StringBuffer stringBuffer = new StringBuffer();

            while (inputStream.available() != 0) {
                stringBuffer.append(bufferedReader.readLine());
            }

            //判断是否取到平台命令
            if ("".equals(stringBuffer.toString())) {
                continue;
            }

            log.info("获取到平台下发的反控命令：{}", stringBuffer.toString());

            //校验平台命令
            switch (counDevice.getAgreement()) {
                case "05":
                    if (!check05ControlCommand(counDevice,stringBuffer.toString())) {
                        log.error("校验平台命令格式失败:{}", stringBuffer.toString());
                        continue;
                    }
                    break;
                case "17":
                    if (!check17ControlCommand(counDevice, stringBuffer.toString())) {
                        log.error("校验平台命令格式失败:{}", stringBuffer.toString());
                        continue;
                    }
                    break;
            }
            log.info("校验平台下发的命令格式正确：{}", stringBuffer.toString());

            //校验CN号
            if (!"".equals(countercharge.getVerifyCn())) {
                if (!getLinkConstant("CN", stringBuffer.toString()).equals(countercharge.getVerifyCn())) {
                    log.error("校验CN号失败:{}", countercharge.getVerifyCn());
                    continue;
                }
                log.info("校验指定CN号成功：", countercharge.getVerifyCn());
            } else {
                log.info("不需要校验CN号", countercharge.getVerifyCn());
            }

            switch (counDevice.getAgreement()) {
                case "05":

                    if ("2012".equals(getLinkConstant("CN", stringBuffer.toString())) || "2022".equals(getLinkConstant("CN", stringBuffer.toString()))) {
                        String agreement9013ControlCommand = get05Agreement9013ControlCommand(stringBuffer.toString());
                        if (!"".equals(agreement9013ControlCommand)) {
                            outputStream.write(agreement9013ControlCommand.getBytes());
                            log.info("发送9013成功:{}", agreement9013ControlCommand);
                        } else {
                            log.warn("9013命令为空:{}", agreement9013ControlCommand);
                        }
                        break;
                    }

                    if (counCountercharge.getResponseParameter() == 9011) {
                        String agreement9011ControlCommand = get05Agreement9011ControlCommand(stringBuffer.toString(), counCountercharge.getResponseStatus());
                        if (!"".equals(agreement9011ControlCommand)) {
                            outputStream.write(agreement9011ControlCommand.getBytes());
                            log.info("发送9011命令成功:{}", agreement9011ControlCommand);
                        } else {
                            log.warn("获取9011反控命令为空:{}", stringBuffer.toString());
                        }
                    } else if (counCountercharge.getResponseParameter() == 9012) {
                        String agreement9011ControlCommand = get05Agreement9011ControlCommand(stringBuffer.toString(), 1);
                        String agreementDataReportedCommand = get05AgreementDataReportedCommand(stringBuffer.toString());
                        String agreement9012ControlCommand = get05Agreement9012ControlCommand(stringBuffer.toString(), counCountercharge.getResponseStatus());


                        if (!"".equals(agreement9011ControlCommand)) {
                            outputStream.write(agreement9011ControlCommand.getBytes());
                            log.info("发送9011成功:{}", agreement9011ControlCommand);
                        } else {
                            log.warn("获取9011命令为空:{}", agreement9011ControlCommand);
                        }

                        if (!"".equals(agreementDataReportedCommand) && 1 == counCountercharge.getResponseStatus()) {
                            outputStream.write(agreementDataReportedCommand.getBytes());
                            log.info("发送提取数据成功:{}", agreementDataReportedCommand);
                        } else if (1 != counCountercharge.getResponseStatus()) {
                            log.info("9012状态不等于1，不发送提取数据:{}", agreementDataReportedCommand);
                        } else {
                            log.warn("本次为设置反控，所以没有提取数据，请核对:{}", agreementDataReportedCommand);
                        }

                        if (!"".equals(agreement9012ControlCommand)) {
                            outputStream.write(agreement9012ControlCommand.getBytes());
                            log.info("发送9012成功:{}", agreement9012ControlCommand);
                        } else {
                            log.warn("9012命令为空:{}", agreement9012ControlCommand);
                        }

                    } else {
                        log.warn("传的RequestResponseCommand参数有问题，请核对:{}", counCountercharge.getResponseParameter());
                    }
                    break;

                case "17":

                    if ("2012".equals(getLinkConstant("CN", stringBuffer.toString())) || "2022".equals(getLinkConstant("CN", stringBuffer.toString()))) {
                        String agreement9013ControlCommand = get17Agreement9013ControlCommand(stringBuffer.toString());
                        if (!"".equals(agreement9013ControlCommand)) {
                            outputStream.write(agreement9013ControlCommand.getBytes());
                            log.info("发送9013成功:{}", agreement9013ControlCommand);
                        } else {
                            log.warn("9013命令为空:{}", agreement9013ControlCommand);
                        }
                        break;
                    }

                    if (counCountercharge.getResponseParameter() == 9011) {
                        String agreement9011ControlCommand = get17Agreement9011ControlCommand(stringBuffer.toString(),  counCountercharge.getResponseStatus());
                        if (!"".equals(agreement9011ControlCommand)) {
                            outputStream.write(agreement9011ControlCommand.getBytes());
                            log.info("发送9011命令成功:{}", agreement9011ControlCommand);
                        } else {
                            log.warn("获取9011反控命令为空:{}", stringBuffer.toString());
                        }
                    } else if (counCountercharge.getResponseParameter() == 9012) {
                        String agreement9011ControlCommand = get17Agreement9011ControlCommand(stringBuffer.toString(), 1);
                        String agreementDataReportedCommand = get17AgreementDataReportedCommand(stringBuffer.toString());
                        String agreement9012ControlCommand = get17Agreement9012ControlCommand(stringBuffer.toString(),  counCountercharge.getResponseStatus());

                        if (!"".equals(agreement9011ControlCommand)) {
                            outputStream.write(agreement9011ControlCommand.getBytes());
                            log.info("发送9011成功:{}", agreement9011ControlCommand);
                        } else {
                            log.warn("获取9011命令为空:{}", agreement9011ControlCommand);
                        }

                        if (!"".equals(agreementDataReportedCommand) && 1 ==  counCountercharge.getResponseStatus()) {
                            outputStream.write(agreementDataReportedCommand.getBytes());
                            log.info("发送提取数据成功:{}", agreementDataReportedCommand);
                        } else if (1 !=  counCountercharge.getResponseStatus()) {
                            log.info("9012状态不等于1，不发送提取数据:{}", agreementDataReportedCommand);
                        } else {
                            log.warn("本次为设置反控，所以没有提取数据，请核对:{}", agreementDataReportedCommand);
                        }

                        if (!"".equals(agreement9012ControlCommand)) {
                            outputStream.write(agreement9012ControlCommand.getBytes());
                            log.info("发送9012成功:{}", agreement9012ControlCommand);
                        } else {
                            log.warn("9012命令为空:{}", agreement9012ControlCommand);
                        }

                    } else {
                        log.warn("传的RequestResponseCommand参数有问题，请核对:{}", counCountercharge.getResponseParameter());
                    }
                    break;
            }

        }

        bufferedReader.close();
        reader.close();
        inputStream.close();
        outputStream.close();
        socket.close();
        connetionStatus.remove(counDevice.getMn());
    }

    @Override
    public void closeConnection(Integer deviceId) throws IOException {
        CounDevice counDevice = iCounDeviceService.getById(deviceId);
        connetionStatus.put(counDevice.getMn(),0);
        CounCountercharge countercharge = iCounCounterchargeService.getCounCounterchargeByDeviceId(deviceId);
        countercharge.setConnetionStatus(0);
        iCounCounterchargeService.save(countercharge);
    }

    @Override
    public void addInitByDeviceId(Integer deviceId) {
        CounCountercharge counCountercharge = new CounCountercharge(deviceId, "", 9012, 1, 0);
        iCounCounterchargeService.save(counCountercharge);
    }

    @Override
    public void deleteByDeviceId(Integer deviceId) {
        QueryWrapper<CounCountercharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        iCounCounterchargeService.remove(queryWrapper);
    }


    private  boolean check05ControlCommand(CounDevice counDevice, String datagram) {
        String regex = "";

        switch (getLinkConstant("CN", datagram)){
            case "1011":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1011;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&&&";
                break;
            case "1012":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1012;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&SystemTime=[\\d]{14}&&";
                break;
            case "1021":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1021;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&(PolId=[\\w]+;?)+&&";
                break;
            case "1022":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1022;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&([\\w]+-LowValue=[\\d\\.]+,[\\w]+-UpValue=[\\d\\.]+;?)+&&";
                break;
            case "1031":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1031;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&&&";
                break;
            case "1032":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1032;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&AlarmTarget=[\\d]+&&";
                break;
            case "1041":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1041;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&&&";
                break;
            case "1042":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1042;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&ReportTime=[\\d]{4}&&";
                break;
            case "1061":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1061;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&&&";
                break;
            case "1062":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1062;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&RtdInterval=[\\d]+&&";
                break;
            case "1072":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1072;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&PW=[\\w]+&&";
                break;
            case "2011":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2011;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&&&";
                break;
            case "2012":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2012;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&&&";
                break;
            case "2021":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2021;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&&&";
                break;
            case "2022":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2022;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&&&";
                break;
            case "2031":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2031;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&BeginTime=[\\d]{14},EndTime=[\\d]{14}&&";
                break;
            case "2041":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1041;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&BeginTime=[\\d]{14},EndTime=[\\d]{14}&&";
                break;
            case "2051":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2051;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&BeginTime=[\\d]{14},EndTime=[\\d]{14}&&";
                break;
            case "2061":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2061;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&BeginTime=[\\d]{14},EndTime=[\\d]{14}&&";
                break;
            case "2071":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2071;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&BeginTime=[\\d]{14},EndTime=[\\d]{14}&&";
                break;
            case "3011":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3011;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&PolID=[\\w]+&&";
                break;
            case "3012":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1031;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&PolID=[\\w]+&&";
                break;
            case "3013":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3013;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&PolID=[\\w]+&&";
                break;
            case "3014":
                regex="QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3014;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=3;CP=&&PolID=[\\w]+(,CTime=[\\d]{2})+&&";
                break;
        }

        if (!"".equals(regex)) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(datagram);
            return m.find();
        }
        return false;

    }


    private  boolean check17ControlCommand(CounDevice counDevice, String datagram) {
        String regex = "";

        switch (getLinkConstant("CN", datagram)) {
            //参数命令
            case "1011":
//                regex = "QN=[\\d]{17};ST=[\\d]{2};CN=1011;PW=123456;MN=[\\w]+;Flag=[\\d];CP=&&(PolId=[\\w]+)?&&";
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1011;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&(PolId=[\\w\\u4E00-\\u9FA5]+)?&&";
                break;
            case "1012":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1012;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&(PolId=[\\w\\u4E00-\\u9FA5]+;)?SystemTime=[\\d]{14}&&";
                break;
            case "1061":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1061;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&&&";
                break;
            case "1062":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1062;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&RtdInterval=[\\d]+&&";
                break;
            case "1063":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1063;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&&&";
                break;
            case "1064":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1064;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&MinInterval=[\\d]+&&";
                break;
            case "1072":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=1072;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&NewPW=[\\d]+&&";
                break;
            //数据命令
            case "2011":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2011;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&&&";
                break;
            case "2012":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2012;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&&&";
                break;
            case "2021":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2021;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&&&";
                break;
            case "2022":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2022;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&&&";
                break;
            case "2031":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2031;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&BeginTime=[\\d]{14};EndTime=[\\d]{14}&&";
                break;
            case "2041":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2041;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&BeginTime=[\\d]{14};EndTime=[\\d]{14}&&";
                break;
            case "2051":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2051;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&BeginTime=[\\d]{14};EndTime=[\\d]{14}&&";
                break;
            case "2061":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=2061;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&BeginTime=[\\d]{14};EndTime=[\\d]{14}&&";
                break;
            //控制命令
            case "3011":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3011;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&PolId=[\\w\\u4E00-\\u9FA5]+&&";
                break;
            case "3012":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3012;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&PolId=[\\w\\u4E00-\\u9FA5]+&&";
                break;
            case "3013":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3013;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&PolId=[\\w\\u4E00-\\u9FA5]+&&";
                break;
            case "3014":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3014;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&PolId=[\\w\\u4E00-\\u9FA5]+&&";
                break;
            case "3015":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3015;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&&&";
                break;
            case "3016":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3016;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&PolId=[\\w\\u4E00-\\u9FA5]+;CstartTime=[\\d]{6};CTime=[\\d]+&&";
                break;
            case "3017":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3017;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&PolId=[\\w\\u4E00-\\u9FA5]+&&";
                break;
            case "3018":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3018;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&PolId=[\\w\\u4E00-\\u9FA5]+&&";
                break;
            case "3019":
                regex = "QN=[\\d]{17};ST=" + counDevice.getMonitoringType() + ";CN=3019;PW=[\\w]+;MN=" + counDevice.getMn() + ";Flag=5;CP=&&PolId=[\\w\\u4E00-\\u9FA5]+&&";
                break;
            case "3020":
//                regex = "QN=[\\d]{17};ST="+counDevice.getMonitoringType()+";CN=3020;PW=[\\w]+;MN="+counDevice.getMn()+";Flag=5;CP=&&PolId=[\\w\\u4E00-\\u9FA5]+;InfoId=[\\w]+;BeginTime=[\\d]{14},EndTime=[\\d]{14}&&";
                break;
            case "3021":
//                regex = "QN=[\\d]{17};ST="+counDevice.getMonitoringType()+";CN=3020;PW=[\\w]+;MN="+counDevice.getMn()+";Flag=5;CP=&&PolId=[\\w\\u4E00-\\u9FA5]+;InfoId=[\\w]+&&";
                break;
        }

        if (!"".equals(regex)) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(datagram);
            return m.find();
        }
        return false;
    }


    /**
     * 获取反控指令常量
     *
     * @param para     获取QN/CN/MN/PolId
     * @param datagram 数据报文
     * @return 要取的常量值
     */
    private String getLinkConstant(String para, String datagram) {
        String regex = "QN=([\\d]{17});ST=([\\d]{2});CN=([\\d]{4});PW=[\\w]+;MN=([\\w]+);Flag=[\\d];CP=&&(PolId=([\\w\\u4E00-\\u9FA5]+))?";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(datagram);
        if (m.find()) {
            switch (para) {
                case "QN":
                    return m.group(1);
                case "ST":
                    return m.group(2);
                case "CN":
                    return m.group(3);
                case "MN":
                    return m.group(4);
                case "PolId":
                    return m.group(6);
            }
        }
        log.error("提取反控命令里面的常量提取不到，请检查一下是否匹配");
        return null;
    }

    private  String get05Agreement9011ControlCommand(String datagram, int status) {
        String msg = "";
        switch (getLinkConstant("CN", datagram)) {
            case "2012":
            case "2022":
                break;
            default:
                msg = "ST=91;CN=9011;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=0;CP=&&QN=" + getLinkConstant("QN", datagram)+";QnRtn="+ status + "&&";
                break;
        }
        if ("".equals(msg)) {
            return msg;
        }
        return DataPackageUtils.composeDataPackage(msg, false);
    }

    /**
     * 获取17协议9011反控命令
     *
     * @param datagram 数据报文
     * @param status   1（执行成功）/2（执行失败，但不知道原因）/3（命令请求条件错误）/4（通讯超时）/5（系统繁忙不能执行）/6（系统故障）/100（没有数据）
     * @return 有则返回CNC校验反控命令，无则返回为空
     */
    private  String get17Agreement9011ControlCommand(String datagram, int status) {
        String msg = "";
        switch (getLinkConstant("CN", datagram)) {
            case "2012":
            case "2022":
                break;
            default:
                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=91;CN=9011;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&QnRtn=" + status + "&&";
                break;
        }
        if ("".equals(msg)) {
            return msg;
        }
        return DataPackageUtils.composeDataPackage(msg, false);
    }

    private  String get05AgreementDataReportedCommand(String datagram) {
        String msg = "";
        switch (getLinkConstant("CN", datagram)){
            case "1011":
                msg = "ST=" + getLinkConstant("ST", datagram) + ";CN=1011;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&QN=" + getLinkConstant("QN", datagram) + ";SystemTime="+ Common.getTime("second")+"&&";
                break;
            case "1021":
                msg = "ST=" + getLinkConstant("ST", datagram) + ";CN=1021;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&QN=" + getLinkConstant("QN", datagram) + ";"+getLinkConstant("PolId",datagram)+"-LowValue=1.1,"+getLinkConstant("PolId",datagram)+"-UpValue=9.9&&";
                break;
            case "1031":
                msg = "ST=" + getLinkConstant("ST", datagram) + ";CN=1031;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&QN=" + getLinkConstant("QN", datagram) + ";AlarmTarget=6666666&&";
                break;
            case "1041":
                msg = "ST=" + getLinkConstant("ST", datagram) + ";CN=1041;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&QN=" + getLinkConstant("QN", datagram) + ";ReportTime=0606&&";
                break;
            case "1061":
                msg = "ST=" + getLinkConstant("ST", datagram) + ";CN=1061;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&QN=" + getLinkConstant("QN", datagram) + ";RtdInterval=30&&";
                break;
            case "2011":
                //提取实时数据，暂不考虑
                break;
            case "2021":
                msg = "ST=" + getLinkConstant("ST", datagram) + ";CN=1011;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&QN=" + getLinkConstant("QN", datagram) + ";SB1-RS=1;SB2-RS=0&&";
                break;
            case "2031":
                //提取日数据，暂不考虑
                break;
            case "2041":
                //提取日数据，暂不考虑
                break;
            case "2051":
                //提取分钟数据，暂不考虑
                break;
            case "2061":
                //提取小数数据，暂不考虑
                break;
            case "2071":
                msg = "ST=" + getLinkConstant("ST", datagram) + ";CN=1011;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&QN=" + getLinkConstant("QN", datagram) + ";"+getLinkConstant("PolId",datagram)+"-Ala=6.6&&";
                break;
        }
        if ("".equals(msg)) {
            return "";
        }
        return DataPackageUtils.composeDataPackage(msg, false);
    }

    private  String get17AgreementDataReportedCommand(String datagram) {
        String msg = "";
        switch (getLinkConstant("CN", datagram)) {
            //参数命令
            case "1011":
                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=" + getLinkConstant("ST", datagram) + ";CN=1011;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&" + (null != getLinkConstant("PolId", datagram) ? "PolId=" + getLinkConstant("PolId", datagram) + ";" : "") + "SystemTime=" + Common.getTime("second") + "&&";
                break;
            case "1061":
                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=" + getLinkConstant("ST", datagram) + ";CN=1061;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&RtdInterval=30&&";
                break;
            case "1063":
                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=" + getLinkConstant("ST", datagram) + ";CN=1063;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&MinInterval=10&&";
                break;
            //数据命令
            case "2031":
                //补发日数据，暂不做
                break;
            case "2041"://取日数据，暂时不考虑
//                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=" + getLinkConstant("ST", datagram) + ";CN=2041;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&MinInterval=10&&";
                break;
            case "2051":
                //补发分钟数据，暂不做
                break;
            case "2061":
                //补发小时数据，暂不做
                break;
            //控制命令
            case "3015":
                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=" + getLinkConstant("ST", datagram) + ";CN=3015;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&DataTime=" + Common.getTime("second") + ";VaseNo=1&&";
                break;
            case "3017":
                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=" + getLinkConstant("ST", datagram) + ";CN=3017;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&PolId=" + getLinkConstant("PolId", datagram) + ";CstartTime=060606;CTime=6&&";
                break;
            case "3018":
                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=" + getLinkConstant("ST", datagram) + ";CN=3018;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&PolId=" + getLinkConstant("PolId", datagram) + ";Stime=60&&";
                break;
            case "3019":
                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=" + getLinkConstant("ST", datagram) + ";CN=3019;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&PolId=" + getLinkConstant("PolId", datagram) + ";" + getLinkConstant("PolId", datagram) + "-SN=12345678910&&";
                break;
            case "3020":
                //因有三种，暂不做
                break;
        }
        if ("".equals(msg)) {
            return "";
        }
        return DataPackageUtils.composeDataPackage(msg, false);
    }

    private  String get05Agreement9012ControlCommand(String datagram, int status) {
        String msg = "";
        switch (getLinkConstant("CN", datagram)) {
            case "2012":
            case "2022":
                break;
            default:
                msg = "ST=91;CN=9012;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&QN=" + getLinkConstant("QN", datagram) +";ExeRtn=" + status + "&&";
                break;
        }
        if ("".equals(msg)) {
            return msg;
        }

        return DataPackageUtils.composeDataPackage(msg, false);
    }

    private  String get17Agreement9012ControlCommand(String datagram, int status) {
        String msg = "";
        switch (getLinkConstant("CN", datagram)) {
            case "2012":
            case "2022":
                break;
            default:
                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=91;CN=9012;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&ExeRtn=" + status + "&&";
                break;
        }
        if ("".equals(msg)) {
            return msg;
        }

        return DataPackageUtils.composeDataPackage(msg, false);
    }

    private  String get05Agreement9013ControlCommand(String datagram) {
        String msg = "";
        switch (getLinkConstant("CN", datagram)) {
            case "2012":
            case "2022":
                msg = "ST=91;CN=9013;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";CP=&&QN="+ getLinkConstant("QN", datagram)+"&&";
                break;
        }
        if ("".equals(msg)) {
            return msg;
        }
        return DataPackageUtils.composeDataPackage(msg, false);
    }


    private  String get17Agreement9013ControlCommand(String datagram) {
        String msg = "";
        switch (getLinkConstant("CN", datagram)) {
            case "2012":
            case "2022":
                msg = "QN=" + getLinkConstant("QN", datagram) + ";ST=91;CN=9013;PW=123456;MN=" + getLinkConstant("MN", datagram) + ";Flag=4;CP=&&&&";
                break;
        }
        if ("".equals(msg)) {
            return msg;
        }
        return DataPackageUtils.composeDataPackage(msg, false);
    }
}