package de.hpi.isg.dao;

import de.hpi.isg.elements.AnnotationResults;
import de.hpi.isg.elements.Sheet;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * This interface defines a bunch of queries to the database.
 *
 * @author Lan Jiang
 * @since 9/9/19
 */
public interface AbstractQueries {

    void insertLineFunctionAnnotationResults(AnnotationResults results);

    int getExcelFileIdByName(String excelFileName, Connection connection);

    int getSpreadsheetIdByName(String spreadsheetName, String excel_file_name, Connection connection);

    void loadExcelFileStatistics(Map<String, List<String>> sheetNamesByExcelFileName);

    void updateSpreadsheetAnnotationStatus(String spreadsheetName, String excel_file_name);

    List<Sheet> getAllUnannotatedSpreadsheet();

    int getSheetAmountByExcelName(String excelName);

    void insertTimeCost(AnnotationResults results, long duration);
}
