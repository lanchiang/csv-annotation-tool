package de.hpi.isg.storage;

import de.hpi.isg.elements.AnnotationResults;
import de.hpi.isg.elements.Sheet;
import de.hpi.isg.io.ResultCache;
import de.hpi.isg.pojo.AnnotationPojo;
import de.hpi.isg.pojo.SpreadSheetPojo;
import lombok.Getter;

import java.util.*;

/**
 * @author Lan Jiang
 * @since 9/18/19
 */
public class JsonStore extends Store {

    @Getter
    private final ResultCache<SpreadSheetPojo> resultCache = new ResultCache<>();

    private final Collection<AnnotationResults> resultsInMemory = new LinkedList<>();

    public JsonStore(List<Sheet> spreadsheetPool) {
        super(spreadsheetPool);
    }

    @Override
    public void addAnnotation(AnnotationResults results) {
        resultsInMemory.remove(results);
        resultsInMemory.add(results);
    }

    public void generateResultCache() {
        resultsInMemory.forEach(annotationResults -> {
            resultCache.addResultToCache(convertToResultCacheFormat(annotationResults));
        });
    }

    @Override
    public AnnotationResults getAnnotation(String spreadsheetFullName) {
        String[] nameSplits = spreadsheetFullName.split("@");
        String fileName = nameSplits[0];
        String sheetName = nameSplits[1].split(".csv")[0];
        Optional<AnnotationResults> optionalAnnotationResults = resultsInMemory.stream()
                .filter(annotationResults ->
                        annotationResults.getSheet().getExcelFileName().equals(fileName) && annotationResults.getSheet().getSheetName().equals(sheetName))
                .findFirst();
        if (!optionalAnnotationResults.isPresent()) {
            throw new RuntimeException("The spreadsheet result is not stored yet.");
        }
        return optionalAnnotationResults.get();
    }

    private SpreadSheetPojo convertToResultCacheFormat(AnnotationResults annotationResults) {
        String excelFileName = annotationResults.getSheet().getExcelFileName();
        String spreadsheetName = annotationResults.getSheet().getSheetName();
        long timeExpense = annotationResults.getTimeExpense();

        Collection<AnnotationPojo> results = new LinkedList<>();

        if (annotationResults.getAnnotationResults().size() != 0) {
            String lineType = annotationResults.getAnnotationResults().get(0).getType();
            int startLineNumber = 0;
            int endLineNumber = 0;
            for (int i = 1; i < annotationResults.getAnnotationResults().size(); i++) {
                AnnotationResults.AnnotationResult annotationResult = annotationResults.getAnnotationResults().get(i);
                if (annotationResult.getType().equals(lineType)) {
                    endLineNumber = i;
                } else {
                    results.add(new AnnotationPojo(startLineNumber + 1, endLineNumber + 1, lineType));
                    startLineNumber = i;
                    endLineNumber = i;
                    lineType = annotationResult.getType();
                }
            }
            results.add(new AnnotationPojo(startLineNumber + 1, endLineNumber + 1, lineType));
        }
        return new SpreadSheetPojo(spreadsheetName, excelFileName, timeExpense, annotationResults.isMultitableFile(), results);
    }
}
