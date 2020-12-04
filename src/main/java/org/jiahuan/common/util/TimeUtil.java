package org.jiahuan.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    /**
     * 获取格式化后的当前时间
     *
     * @param key 精确到毫秒（millisecond）/秒（second）/分钟（minute）/小时（hour）/日（day）
     * @param modified 在当前时间上加减时间，加则输入整数，减则输入负整数。分钟按分钟加、小时按小时加。。。
     * @return 返回格式化后的日期
     */
    public static String getFormatCurrentTime(Date date,String key,int modified) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat simpleDateFormat;
        if ("millisecond".equals(key)) {
            simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            return simpleDateFormat.format(calendar.getTime());
        } else if ("second".equals(key)) {
            calendar.add(Calendar.SECOND, modified);
            simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            return simpleDateFormat.format(calendar.getTime());
        } else if ("minute".equals(key)) {
            calendar.add(Calendar.MINUTE, modified);
            simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            return simpleDateFormat.format(calendar.getTime()) + "00";
        } else if ("hour".equals(key)) {
            calendar.add(Calendar.HOUR, modified);
            simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
            return simpleDateFormat.format(calendar.getTime()) + "0000";
        } else if ("day".equals(key)) {
            calendar.add(Calendar.DAY_OF_MONTH, modified);
            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            return simpleDateFormat.format(calendar.getTime()) + "000000";
        } else {
            return null;
        }
    }

    /**
     * 获取转化后格式化时间
     *
     * @param key 精确到毫秒（millisecond）/秒（second）/分钟（minute）/小时（hour）/日（day）
     * @return 返回格式化后的日期
     */
    public static String getFormatTime(Date ReissueDate,String key) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ReissueDate);
        SimpleDateFormat simpleDateFormat;
        switch (key) {
            case "millisecond":
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                return simpleDateFormat.format(calendar.getTime());
            case "second":
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                return simpleDateFormat.format(calendar.getTime());
            case "minute":
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
                return simpleDateFormat.format(calendar.getTime()) + "00";
            case "hour":
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
                return simpleDateFormat.format(calendar.getTime()) + "0000";
            case "day":
                simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                return simpleDateFormat.format(calendar.getTime()) + "000000";
            default:
                return null;
        }
    }

    /**
     * 获取格式化后的当前时间
     *
     * @param key 精确到毫秒（millisecond）/秒（second）/分钟（minute）/小时（hour）/日（day）
     * @param modified 在当前时间上加减时间，加则输入整数，减则输入负整数。分钟按分钟加、小时按小时加。。。
     * @return 返回格式化后的日期
     */
    public static Date getProcessedCurrentTime(String key,int modified) {
        Calendar calendar = Calendar.getInstance();

        try{
            if ("millisecond".equals(key)) {
                Date date = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String format = simpleDateFormat.format(date);
                Date parse = simpleDateFormat.parse(format);
                return parse;
            } else if ("second".equals(key)) {
                calendar.add(Calendar.SECOND, modified);
                Date date = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String format = simpleDateFormat.format(date);
                Date parse = simpleDateFormat.parse(format);
                return parse;
            } else if ("minute".equals(key)) {
                calendar.add(Calendar.MINUTE, modified);
                Date date = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
                String format= simpleDateFormat.format(date);
                Date parse = simpleDateFormat.parse(format);
                return parse;
            } else if ("hour".equals(key)) {
                calendar.add(Calendar.HOUR, modified);
                Date date = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
                String format = simpleDateFormat.format(date);
                Date parse = simpleDateFormat.parse(format);
                return parse;
            } else if ("day".equals(key)) {
                calendar.add(Calendar.DAY_OF_MONTH, modified);
                Date date = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                String format = simpleDateFormat.format(date);
                Date parse = simpleDateFormat.parse(format);
                return parse;
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }


}
