package org.jiahuan.crc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Common {



    private Common() {

    }

    /**
     * 将报文信息发送到平台
     *
     * @param message 报文信息
     */
    public static void send(String message) throws UnknownHostException, IOException {
        Socket socket = new Socket(SendTcpToolsTest.AddressAndPort[0], Integer.valueOf(SendTcpToolsTest.AddressAndPort[1]));
        OutputStream outputStream = socket.getOutputStream();
        message += "\r\n";
        outputStream.write(message.getBytes());
        log.info(message);
        outputStream.close();
        socket.close();
    }

    /**
     * 获取随机数
     *
     * @param min 最小范围值
     * @param max 最大范围值
     * @return 随机值
     */
    public static String getRandom(int min, int max) {
        int index = min + (int) (Math.random() * (max - min + 1));
        return String.valueOf(index);
    }


    /**
     * 正表达式
     *
     * @param msg 要匹配的内容
     * @return 返回匹配内容
     */
    public static String positiveExpression(String msg) {
        String regex;
        Pattern p;
        Matcher m;
        String group;
        try {
            //17协议
            regex = "QN=[\\w\\d;=\\-&.,]+&{2,4}";
            p = Pattern.compile(regex);
            m = p.matcher(msg);
            m.find();
            group = m.group();
            return group;
        } catch (Exception a) {
            try {
                //05协议
                regex = "ST=[\\w\\d;=\\-&.,]+&{2,4}";
                p = Pattern.compile(regex);
                m = p.matcher(msg);
                m.find();
                group = m.group();
                return group;
            } catch (Exception b) {
                try {
                    //油烟博控私有协议
                    regex = "[\\w]*,[\\d]*&&[\\w,\\.;-]*&&";
                    p = Pattern.compile(regex);
                    m = p.matcher(msg);
                    m.find();
                    group = m.group();
                    return group;
                } catch (Exception c) {
                    c.printStackTrace();
                    return null;
                }
            }

        }
    }


    /**
     * 拼接监测因子
     *
     * @param yy  对象
     * @param key 实时（realTime）/非实时，分钟、小时、日（history）/参数（parameter）/状态（status）
     * @param zs  合（join）/分（divide）/没有（none）
     * @return 拼接的监测因子
     */
    public static String getParameterLink(SendTcpToolsTest yy, String key, String zs) {
        Set<String> keySet;
        List<String> coding;
        List<String> li = new ArrayList<String>();
        StringBuffer buffer = new StringBuffer();
        switch (key) {
            case "realTime":
                keySet = yy.pointDivisor.keySet();
                coding = new ArrayList<String>(keySet);
                for (String cod : coding) {
                    JSONObject parseObject = JSON.parseObject(yy.pointDivisor.get(cod));
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
                keySet = yy.pointDivisor.keySet();
                coding = new ArrayList<String>(keySet);
                //遍历因子编码
                for (String cod : coding) {
                    JSONObject parseObject = JSON.parseObject(yy.pointDivisor.get(cod));
                    keySet = parseObject.keySet();
                    ArrayList<String> arrayList = new ArrayList<String>(keySet);
                    String divideZs = "";
                    //遍历avg、max、min等
                    for (String name : keySet) {
                        //最后一个因子编码并且是最后一个属性
                        if (name.equals(arrayList.get(arrayList.size() - 1)) && cod.equals(coding.get(coding.size() - 1))) {
                            //zs是合
                            if ("join".equals(zs)) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name));
                                //zs是分
                            }else if("divide".equals(zs)){
                                if(!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")){
                                    buffer.append(cod + "-" + name + "=" + parseObject.getString(name));
                                }else{
                                    log.error("出现最后一个属性为折算属性");
                                }
                            }else if (!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name));
                            }
                            //不是最后一个因子，但是是最后一个属性
                        } else if (name.equals(arrayList.get(arrayList.size() - 1))) {
                            if ("join".equals(zs)) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ";");
                            }else if("divide".equals(zs)){
                                if(!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")){
                                    buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ";");
                                }else{
                                    log.error("出现最后一个属性为折算属性");
                                }
                            } else if (!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ";");
                            }
                            //不是最后一个因子
                        } else {
                            if ("join".equals(zs)) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ",");
                            }else if("divide".equals(zs)){
                                if(!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")){
                                    buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ",");
                                }else{
                                    divideZs+=cod + "-" + name + "=" + parseObject.getString(name) + ",";
                                }
                            }else if (!name.equals("ZsAvg") && !name.equals("ZsMin") && !name.equals("ZsMax")) {
                                buffer.append(cod + "-" + name + "=" + parseObject.getString(name) + ",");
                            }
                        }
                    }
                    //添加分开的折算因子
                    if(cod.equals(coding.get(coding.size()-1))&&"divide".equals(zs)){
                        divideZs=divideZs.substring(0,divideZs.length()-1);
                        buffer.append(";"+divideZs);
                    }else if("divide".equals(zs)){
                        divideZs=divideZs.substring(0,divideZs.length()-1);
                        buffer.append(divideZs+";");
                    }
                }
                return buffer.toString();
            case "parameter":
                keySet = yy.meterParamStatusValue.keySet();
                coding = new ArrayList<String>(keySet);
                for (int i = 0; i < coding.size(); i++) {
                    if (coding.get(i).indexOf("i12") == -1) {
                        li.add(coding.get(i));
                    }
                }
                for (int i = 0; i < li.size(); i++) {
                    if (li.size() - 1 == i) {
                        buffer.append(li.get(i) + "-Info=" + yy.meterParamStatusValue.get(li.get(i)));
                    } else {
                        buffer.append(li.get(i) + "-Info=" + yy.meterParamStatusValue.get(li.get(i)) + ";");
                    }
                }
                li = null;
                return buffer.toString();
            case "status":
                keySet = yy.meterParamStatusValue.keySet();
                coding = new ArrayList<String>(keySet);
                for (int i = 0; i < coding.size(); i++) {
                    if (coding.get(i).indexOf("i12") != -1) {
                        li.add(coding.get(i));
                    }
                }
                for (int i = 0; i < li.size(); i++) {
                    if (li.size() - 1 == i) {
                        buffer.append(li.get(i) + "-Info=" + yy.meterParamStatusValue.get(li.get(i)));
                    } else {
                        buffer.append(li.get(i) + "-Info=" + yy.meterParamStatusValue.get(li.get(i)) + ";");
                    }
                }
                li = null;
                return buffer.toString();
        }
        li = null;
        return "Sorry 没找到你需要找的内容";
    }

    /**
     * 获取时间
     *
     * @param key 精确到毫秒（millisecond）/秒（second）/分钟（minute）/小时（hour）/日（day）
     * @return 返回格式化后的日期
     */
    public static String getTime(String key) {
        Calendar calendar = Calendar.getInstance();

        if ("millisecond".equals(key)) {
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format = simpleDateFormat.format(date);
            return format;
        } else if ("second".equals(key)) {
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
            String format1 = simpleDateFormat1.format(date);
            return format1;
        } else if ("minute".equals(key)) {
            calendar.add(Calendar.MINUTE, -10);
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmm");
            String format2 = simpleDateFormat2.format(date);
            return format2 + "00";
        } else if ("hour".equals(key)) {
            calendar.add(Calendar.HOUR, -1);
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyyMMddHH");
            String format3 = simpleDateFormat3.format(date);
            return format3 + "0000";
        } else if ("day".equals(key)) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("yyyyMMdd");
            String format4 = simpleDateFormat4.format(date);
            return format4 + "000000";
        } else {
            return null;
        }
    }


}
