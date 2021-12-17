package de.hpi.isg.storage;

import de.hpi.isg.dao.DatabaseQueryHandler;
import de.hpi.isg.elements.AnnotationResults;
import de.hpi.isg.elements.Sheet;

import java.util.List;

/**
 * @author Lan Jiang
 * @since 9/18/19
 */
public class RDMBSStore extends Store {

    private final DatabaseQueryHandler databaseQueryHandler;

    public RDMBSStore(List<Sheet> spreadsheetPool, DatabaseQueryHandler databaseQueryHandler) {
        super(spreadsheetPool);
        this.databaseQueryHandler = databaseQueryHandler;
    }

    @Override
    public void addAnnotation(AnnotationResults results) {
        this.databaseQueryHandler.insertLineFunctionAnnotationResults(results);
        this.databaseQueryHandler.updateSpreadsheetAnnotationStatus(results.getSheet().getSheetName(), results.getSheet().getExcelFileName());
        this.databaseQueryHandler.insertTimeCost(results, results.getTimeExpense());
    }

    @Override
    public AnnotationResults getAnnotation(String spreadsheetFullName) {
        return null;
    }
}
