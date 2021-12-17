package de.hpi.isg.features;

import de.hpi.isg.elements.Sheet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lan Jiang
 * @since 8/28/19
 */
public class SheetAmountFeature extends SheetSimilarityFeature {

    private final Map<String, List<String>> sheetNamesByFileName;

    public SheetAmountFeature(Map<String, List<String>> sheetAmountByFileName) {
        this.sheetNamesByFileName = sheetAmountByFileName;
    }

    public SheetAmountFeature() {
        sheetNamesByFileName = new HashMap<>();
    }

    @Override
    public double score(File file1, File file2, Map<String, Sheet> sheets) {
        String fileName1 = sheets.get(file1.getName()).getExcelFileName();
        String fileName2 = sheets.get(file2.getName()).getExcelFileName();
        int sheetAmount1 = sheetNamesByFileName.get(fileName1).size();
        int sheetAmount2 = sheetNamesByFileName.get(fileName2).size();

        if (sheetAmount1 == 0 || sheetAmount2 == 0) {
            System.out.println("Sheet amount equals to zero.");
            return 0;
        }

        double score;
        if (sheetAmount1 > sheetAmount2) {
            score = (double) sheetAmount2 / (double) sheetAmount1;
        } else {
            score = (double) sheetAmount1 / (double) sheetAmount2;
        }
        return score;
    }

    @Override
    public void score(Sheet current, List<Sheet> candidateSheets) {
        scoreMap = new HashMap<>();
        candidateSheets.forEach(sheet -> {
            int currentSheetAmount = current.getNumOfSpreadsheetsOfExcelFile();
            int sheetAmount = sheet.getNumOfSpreadsheetsOfExcelFile();

            if (currentSheetAmount == 0 || sheetAmount == 0) {
                System.out.println("Sheet amount equals to zero.");
                scoreMap.putIfAbsent(sheet, 0.0);
            }

            double score;
            if (currentSheetAmount > sheetAmount) {
                score = (double) sheetAmount / (double) currentSheetAmount;
            } else {
                score = (double) currentSheetAmount / (double) sheetAmount;
            }

            scoreMap.putIfAbsent(sheet, score);
        });
    }
}
