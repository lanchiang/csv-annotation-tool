package de.hpi.isg.elements;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * This entity represents a sheet.
 *
 * @author Lan Jiang
 * @since 8/28/19
 */
public class Sheet {

    @Getter
    private final String sheetName;

    @Getter
    private final String excelFileName;

    /**
     * Number of spreadsheets in the excel file that contains this spreadsheet.
     */
    @Getter
    private final int numOfSpreadsheetsOfExcelFile;

    @Getter @Setter
    private boolean isAnnotated = false;

    public Sheet(String sheetName, String excelFileName) {
        this(sheetName, excelFileName, 0);
    }

    public Sheet(String sheetName, String excelFileName, int numOfSpreadsheetsOfExcelFile) {
        this.sheetName = sheetName;
        this.excelFileName = excelFileName;
        this.numOfSpreadsheetsOfExcelFile = numOfSpreadsheetsOfExcelFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sheet sheet = (Sheet) o;
        return numOfSpreadsheetsOfExcelFile == sheet.numOfSpreadsheetsOfExcelFile &&
                Objects.equals(sheetName, sheet.sheetName) &&
                Objects.equals(excelFileName, sheet.excelFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sheetName, excelFileName, numOfSpreadsheetsOfExcelFile);
    }
}
