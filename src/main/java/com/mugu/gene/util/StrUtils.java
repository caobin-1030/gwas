package com.mugu.gene.util;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/11/1
 */
public class StrUtils {
    /**
     * 查看一个字符串是否可以转换为数字
     *
     * @param str 字符串
     * @return true 可以; false 不可以
     */
    public static boolean isStrNum(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNotStrNum(String str) {
        return !isStrNum(str);
    }

    public static boolean isStrDou(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNotStrDou(String str) {
        return !isStrDou(str);
    }
}
