package de.hpi.isg.utils;

/**
 * @author Lan Jiang
 * @since 10/11/19
 */
public class GeneralUtils {

    private final static String FILE_SHEET_SEPARATOR = "@";

    private final static String FULL_NAME_POSTFIX = ".csv";

    public static String createFullName(final String excelName, final String sheetName) {
        return excelName + FILE_SHEET_SEPARATOR + sheetName + FULL_NAME_POSTFIX;
    }

    /**
     *
     * @param fullName
     * @return an array of two strings. The first string is the excel file name, the second is the spreadsheet name.
     */
    public static String[] splitFullName(final String fullName) {
        String[] nameSplits = fullName.split("@");
        nameSplits[1] = nameSplits[1].split(".csv")[0];
        return nameSplits;
    }
}
