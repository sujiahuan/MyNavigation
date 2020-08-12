package org.jiahuan.common.util;

public class RandomUtil {

    /**
     * 获取随机整数
     *
     * @param min 最小范围值
     * @param max 最大范围值
     * @return 随机值
     */
    public static String getRandomInt(int min, int max) {
        int index = min + (int) (Math.random() * (max - min + 1));
        return String.valueOf(index);
    }
}
