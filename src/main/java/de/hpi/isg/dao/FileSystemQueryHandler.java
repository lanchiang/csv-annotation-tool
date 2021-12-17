package de.hpi.isg.dao;

import de.hpi.isg.elements.AnnotationResults;
import de.hpi.isg.elements.Sheet;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author Lan Jiang
 * @since 9/20/19
 */
public class FileSystemQueryHandler implements AbstractQueries {

    private final List<Sheet> sheets;

    public FileSystemQueryHandler(List<Sheet> sheets) {
        this.sheets = sheets;
    }

    @Override
    public void insertLineFunctionAnnotationResults(AnnotationResults results) {

    }

    @Override
    public int getExcelFileIdByName(String excelFileName, Connection connection) {
        return 0;
    }

    @Override
    public int getSpreadsheetIdByName(String spreadsheetName, String excel_file_name, Connection connection) {
        return 0;
    }

    @Override
    public void loadExcelFileStatistics(Map<String, List<String>> sheetNamesByExcelFileName) {

    }

    @Override
    public void updateSpreadsheetAnnotationStatus(String spreadsheetName, String excel_file_name) {

    }

    @Override
    public List<Sheet> getAllUnannotatedSpreadsheet() {
        return null;
    }

    @Override
    public int getSheetAmountByExcelName(String excelName) {
        return 0;
    }

    @Override
    public void insertTimeCost(AnnotationResults results, long duration) {

    }
}
