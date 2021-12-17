package de.hpi.isg.dao;

import de.hpi.isg.elements.AnnotationResults;
import de.hpi.isg.elements.Sheet;
import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class implements the queries specified by the {@link AbstractQueries} in relational DBMS environment.
 *
 * @author Lan Jiang
 * @since 9/10/19
 */
public class DatabaseQueryHandler implements AbstractQueries {

    @Getter
    private final DatabaseConnector databaseConnector;

    public DatabaseQueryHandler() {
//        this.databaseConnector = new DatabaseConnector();
        this.databaseConnector = null;
    }

    public void close() {
        if (this.databaseConnector == null) {
            return;
        }
        try {
            this.databaseConnector.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertLineFunctionAnnotationResults(AnnotationResults annotationResults) {
        Connection connection = databaseConnector.getConnection();

        Sheet sheet = annotationResults.getSheet();
        String fileName = sheet.getExcelFileName().replace("'", "''");
        String spreadSheetName = sheet.getSheetName();

        final int spreadsheet_id = getSpreadsheetIdByName(spreadSheetName, fileName, connection);

        List<AnnotationResults.AnnotationResult> results = annotationResults.getAnnotationResults();
        for (AnnotationResults.AnnotationResult result : results) {
            int lineNumber = result.getLineNumber();
            String lineType = result.getType();

            executeUpdate(String.format("insert into line_function (spreadsheet_id, line_number, line_type) values (%d, %d, \'%s\')",
                    spreadsheet_id, lineNumber, lineType), databaseConnector.getConnection());
        }
//        System.out.println("Records created successfully");
    }

    @Override
    public int getExcelFileIdByName(String excelFileName, Connection connection) {
        String query = String.format("select id from excel_file where excel_file_name = \'%s\'", excelFileName);
        return getId(query, connection);
    }

    @Override
    public int getSpreadsheetIdByName(String spreadsheetName, String excel_file_name, Connection connection) {
        String query = String.format("select spreadsheet.id from spreadsheet, excel_file where excel_file.excel_file_name = '%s' " +
                "and excel_file.id = spreadsheet.excel_file_id and" +
                " spreadsheet.spread_sheet_name = \'%s\'", excel_file_name, spreadsheetName);
        return getId(query, connection);
    }

    private int getId(String query, Connection connection) {
        ResultSet resultSet;

        int id = 0;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void loadExcelFileStatistics(Map<String, List<String>> sheetNamesByExcelFileName) {
        Connection connection;
        connection = databaseConnector.getConnection();

        for (Map.Entry<String, List<String>> entry : sheetNamesByExcelFileName.entrySet()) {
            String excelFileName = entry.getKey().replace("'", "''");
            int spreadSheetAmount = entry.getValue().size();
            executeUpdate(String.format("insert into excel_file (excel_file_name, spreadsheet_number) values (\'%s\', %d)",
                    excelFileName, spreadSheetAmount), connection);

            final int excel_file_id = getExcelFileIdByName(excelFileName, connection);
//                System.out.println(excel_file_id);

            for (String spreadSheetName : entry.getValue()) {
                String query = String.format("insert into spreadsheet (excel_file_id, spread_sheet_name) values (%d, \'%s\')", excel_file_id, spreadSheetName.replace("'", "''"));
                executeUpdate(query, connection);
            }
        }
    }

    @Override
    public void updateSpreadsheetAnnotationStatus(String spreadsheetName, String excel_file_name) {
        Connection connection = databaseConnector.getConnection();

        int spreadsheet_id = getSpreadsheetIdByName(spreadsheetName, excel_file_name, connection);
        String query = String.format("update spreadsheet set has_annotated = TRUE where id = %d", spreadsheet_id);
        executeUpdate(query, connection);
    }

    @Override
    public List<Sheet> getAllUnannotatedSpreadsheet() {
        Connection connection = databaseConnector.getConnection();

        String query = "select spreadsheet.spread_sheet_name, excel_file.excel_file_name, excel_file.spreadsheet_number from spreadsheet, excel_file " +
                "where spreadsheet.excel_file_id = excel_file.id and spreadsheet.has_annotated = FALSE";

        List<Sheet> results = new LinkedList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String spreadsheetName = resultSet.getString("spread_sheet_name");
                String excelFileName = resultSet.getString("excel_file_name");
                int spreadSheetAmount = resultSet.getInt("spreadsheet_number");
                Sheet sheet = new Sheet(spreadsheetName, excelFileName, spreadSheetAmount);
                results.add(sheet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public int getSheetAmountByExcelName(String excelName) {
        Connection connection = databaseConnector.getConnection();

        String query = String.format("select spreadsheet_number from excel_file where excel_file_name = '%s'", excelName);

        int sheetAmount = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                sheetAmount = resultSet.getInt("spreadsheet_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sheetAmount;
    }

    @Override
    public void insertTimeCost(AnnotationResults results, long duration) {
        Connection connection = databaseConnector.getConnection();

        String sheetName = results.getSheet().getSheetName();
        String excelFileName = results.getSheet().getExcelFileName();

        int spreadsheetId = getSpreadsheetIdByName(sheetName, excelFileName, connection);

        String query = String.format("insert into annotation_time_cost (spreadsheet_id, time_cost) values (%d, %d)", spreadsheetId, duration);
        executeUpdate(query, connection);
    }

    private void executeUpdate(String query, Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            statement.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
