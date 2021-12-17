package de.hpi.isg.utils;

/**
 * @author lan
 * @since 2021/1/19
 */
public class TextUtils {

    public static String advancedTrim(String str) {
        return str.trim().replaceAll("(^\\h*)|(\\h*$)", "");
    }
}
