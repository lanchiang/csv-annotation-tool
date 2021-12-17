package de.hpi.isg.storage;

import de.hpi.isg.elements.AnnotationResults;
import de.hpi.isg.elements.Sheet;
import de.hpi.isg.features.FileNameSimilarityFeature;
import de.hpi.isg.features.SheetAmountFeature;
import de.hpi.isg.features.SheetNameSimilarityFeature;
import de.hpi.isg.features.SheetSimilarityFeature;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lan Jiang
 * @since 9/18/19
 */
abstract public class Store {

    private final List<Sheet> spreadsheetPool;

    public Store(List<Sheet> spreadsheetPool) {
        this.spreadsheetPool = spreadsheetPool;
    }

    public Sheet findMostSimilarSheet(Sheet currentSheet) {
        removeAnnotatedSheet(currentSheet);

        Set<SheetSimilarityFeature> features = new HashSet<>();
        features.add(new FileNameSimilarityFeature());
        features.add(new SheetNameSimilarityFeature());
        features.add(new SheetAmountFeature());

        features.forEach(feature -> feature.score(currentSheet, spreadsheetPool.stream().filter(sheet -> !sheet.isAnnotated()).collect(Collectors.toList())));

        Map<Sheet, Double> score = new HashMap<>();

        features.stream().map(SheetSimilarityFeature::getScoreMap).forEach(map -> {
            for (Map.Entry<Sheet, Double> entry : map.entrySet()) {
                if (!score.containsKey(entry.getKey())) {
                    score.put(entry.getKey(), entry.getValue());
                } else {
                    score.put(entry.getKey(), score.get(entry.getKey()) + entry.getValue());
                }
            }
        });
        if (score.size() == 0) {
            return null;
        }
        final Map<Sheet, Double> newScore = score.entrySet().stream().sorted(Map.Entry.<Sheet, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return newScore.entrySet().iterator().next().getKey();
    }

    private void removeAnnotatedSheet(Sheet currentSheet) {
        Optional<Sheet> optionalSheet = spreadsheetPool.stream().filter(sheet -> sheet.equals(currentSheet)).findFirst();
        if (!optionalSheet.isPresent()) {
            System.out.println("The current sheet is not in the pool");
            return;
        }
        optionalSheet.get().setAnnotated(true);
    }

    abstract public void addAnnotation(AnnotationResults results);

    abstract public AnnotationResults getAnnotation(String spreadsheetFullName);

    public Sheet getSpreadsheet(String excelName, String spreadsheetName) {
        Optional<Sheet> optionalSheet = spreadsheetPool.stream()
                .filter(element -> element.getExcelFileName().equals(excelName) && element.getSheetName().equals(spreadsheetName)).findFirst();
        return optionalSheet.orElse(null);
    }

    public int getSpreadsheetAmountByExcelFileName(String excelFileName) {
        return (int) spreadsheetPool.stream().filter(element -> element.getExcelFileName().equals(excelFileName)).count();
    }
}
