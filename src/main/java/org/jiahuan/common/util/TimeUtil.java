package org.jiahuan.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    /**
     * 获取格式化后的当前时间
     *
     * @param key 精确到毫秒（millisecond）/秒（second）/分钟（minute）/小时（hour）/日（day）
     * @param Modified 在当前时间上加减时间，加则输入整数，减则输入负整数。分钟按分钟加、小时按小时加。。。
     * @return 返回格式化后的日期
     */
    public static String getFormatCurrentTime(String key,int Modified) {
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
            calendar.add(Calendar.MINUTE, Modified);
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmm");
            String format2 = simpleDateFormat2.format(date);
            return format2 + "00";
        } else if ("hour".equals(key)) {
            calendar.add(Calendar.HOUR, Modified);
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyyMMddHH");
            String format3 = simpleDateFormat3.format(date);
            return format3 + "0000";
        } else if ("day".equals(key)) {
            calendar.add(Calendar.DAY_OF_MONTH, Modified);
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("yyyyMMdd");
            String format4 = simpleDateFormat4.format(date);
            return format4 + "000000";
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
        Date date = calendar.getTime();
        switch (key) {
            case "millisecond":
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String format = simpleDateFormat.format(date);
                return format;
            case "second":
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
                String format1 = simpleDateFormat1.format(date);
                return format1;
            case "minute":
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmm");
                String format2 = simpleDateFormat2.format(date);
                return format2 + "00";
            case "hour":
                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyyMMddHH");
                String format3 = simpleDateFormat3.format(date);
                return format3 + "0000";
            case "day":
                SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("yyyyMMdd");
                String format4 = simpleDateFormat4.format(date);
                return format4 + "000000";
            default:
                return null;
        }
    }
}
