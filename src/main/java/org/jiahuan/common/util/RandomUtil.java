package org.jiahuan.common.util;

import java.math.BigDecimal;
import java.util.Random;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

public class RandomUtil {

    /**
     * 获取随机整数
     *@param precimal 精确小数位数
     * @param min 最小范围值
     * @param max 最大范围值
     * @return 随机值
     */
    public static String getRandomString(int precimal,Double min, Double max) {
        double value = new Random().nextDouble() * (max-min) + min;
        String plainString = new BigDecimal(value).setScale(precimal, ROUND_HALF_DOWN).toPlainString();
        return plainString;
    }
}
