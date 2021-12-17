package de.hpi.isg.utils;

import java.util.regex.Pattern;

/**
 * @author lan
 * @since 2021/1/18
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

    private final static String COMMA_POINT_NUMBER_FORMAT = "([-+])?\\d{1,3}([.]\\d{3})*([,]\\d{1,})?";
    private final static String POINT_COMMA_NUMBER_FORMAT = "([-+])?\\d{1,3}([,]\\d{3})*([.]\\d{1,})?";
    private final static String SPACE_POINT_NUMBER_FORMAT = "([-+])?\\d{1,3}(\\p{Z}\\d{3})*([.]\\d{1,})?";
    private final static String SPACE_COMMA_NUMBER_FORMAT = "([-+])?\\d{1,3}(\\p{Z}\\d{3})*([,]\\d{1,})?";
    private final static String NONE_POINT_NUMBER_FORMAT = "([-+])?\\d+([.]\\d{1,})?";
    private final static String NONE_COMMA_NUMBER_FORMAT = "([-+])?\\d+([,]\\d{1,})?";
    private final static String SPACE_THOUSAND_SEPARATOR_FORMAT = "-?\\d{1,3}(\\p{Z}\\d{3})+((,|.)\\d{1,})?";

    public static Double normalizeNumber(String numberString, String numberFormat) {
        Double normalizedNumber = null;
        if (numberString == null) {
            return normalizedNumber;
        }

        String[] currencyMarkers = {"\\$", "€"};
        String[] nanMarkers = {"NaN"};
        String numberStr = numberString.replaceAll(currencyMarkers[0], "").replaceAll(currencyMarkers[1], "").replace(nanMarkers[0], "");
        String numStr = TextUtils.advancedTrim(numberStr);
        if (numStr.startsWith("(") && numStr.endsWith(")")) {
            numStr = "-" + numStr.substring(1, numStr.length() - 1);
        }
        if ("Comma Point".equals(numberFormat)) {
            numStr = numStr.replaceAll(",", "");
        } else if ("Point Comma".equals(numberFormat)) {
            numStr = numStr.replaceAll("\\.", "").replaceAll(",", ".");
        } else if ("Space Point".equals(numberFormat)) {
            numStr = numStr.replaceAll("\\p{Z}", "");
        } else if ("Space Comma".equals(numberFormat)) {
            numStr = numStr.replaceAll("\\p{Z}", "").replaceAll(",", ".");
        } else if ("None Point".equals(numberFormat)) {
            numStr = numStr;
        } else if ("None Comma".equals(numberFormat)) {
            numStr = numStr.replaceAll(",", ".");
        } else {
            numStr = numStr;
        }
        try {
            normalizedNumber = Double.parseDouble(numStr);
        } catch (NumberFormatException ignored) {}
        return normalizedNumber;
    }

    public static class ErrorBoundMethod {
        public final static int ABSOLUTE_BOUND = 0;
        public final static int RATIO_BOUND = 1;
    }

    public static boolean isMinorError(double expected, double real, double errorBoundRatio, int method) {
        if (method == ErrorBoundMethod.ABSOLUTE_BOUND) {
            return Math.abs(expected - real) <= errorBoundRatio;
        } else if (method == ErrorBoundMethod.RATIO_BOUND) {
            return Math.abs((expected - real) / expected) < errorBoundRatio;
        } else {
            throw new RuntimeException("Specified error measurement not supported.");
        }
    }
}
