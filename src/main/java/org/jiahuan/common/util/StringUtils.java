package org.jiahuan.common.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 操作字符串工具类
 *
 * @author wangjian 创建时间：2017-7-26 下午2:03:51 修改人： 修改时间：2017-7-26 下午2:03:51 修改备注：
 * @version V1.0
 */
public class StringUtils {
    static Pattern compile = Pattern.compile("[0-9]*");

    /**
     *
     * @Title: isNumeric
     * @Description: (判断字符串是否存在数字)
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Matcher isNum = compile("[0-9]*").matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 自定义截取字符串
     *
     * @param keqflow AAbbbCC
     * @param start   AA
     * @param end     CC
     * @return bbb
     */
    public static String mySubString(String keqflow, String start, String end) {
        String sub = keqflow.split(start)[1];
        int index = sub.indexOf(end);
        return sub.substring(0, index);
    }

    /**
     * 自定义截取字符串
     *
     * @param keqflow AAbbbCC
     * @param start   bbb
     * @return CC
     */
    public static String mySubString(String keqflow, String start) {
        return keqflow.split(start)[1];
    }

    /**
     * 字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String s) {
        String nullStr = "null";
        String str = "";
        if (null == s || str.equals(s.trim()) || nullStr.equalsIgnoreCase(s)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 不为空
     *
     * @param value
     * @return
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     */
    public static String stringToUnicode(String string) throws UnsupportedEncodingException {
        String unicode = "";
        if (string != null) {
            char[] cs = string.toCharArray();
            for (int i = 0; i < cs.length; i++) {
                unicode += "&#";
                unicode += String.valueOf((int) cs[i]);
                unicode += ";";
            }
        }
        return unicode;
    }

    /**
     * 字符串日期格式加0 2018-8-8 To 2018-08-08
     *
     * @param data
     * @return
     */
    public static String stringDateTo(String data) {
        int i = 1;
        int j = 2;
        String[] dataString = data.split("-");
        if (dataString[i].length() == 1) {
            data = dataString[0] + "-0" + dataString[1];
            if (dataString[j].length() == 1) {
                data += "-0" + dataString[2];
            } else {
                data += "-" + dataString[2];
            }
        } else {
            if (dataString[j].length() == 1) {
                data = dataString[0] + "-" + dataString[1] + "-0" + dataString[2];
            }
        }
        return data;
    }

    public static boolean canParseInt(String str) {
        // 验证是否为空
        if (str == null || "".equals(str)) {
            return false;
        }
        // 使用正则表达式判断该字符串是否为数字，第一个\是转义符，\d+表示匹配1个或 //多个连续数字，"+"和"*"类似，"*"表示0个或多个
        return str.matches("\\d+");

    }

    public static String gbkUtf8(String str) {
        if (str != null) {
            try {
                return new String(str.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 替换空字符
     *
     * @param src
     * @return
     */
    public static String replaceEmpty(String src) {
        if (null == src || "".equals(src))
            return "";
        else {
            if ("NULL".equals(src.trim().toUpperCase()))
                return "";
            return src.trim();
        }

    }
}
