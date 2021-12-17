package de.hpi.isg.features;

import de.hpi.isg.elements.Sheet;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lan Jiang
 * @since 8/28/19
 */
public class FileNameSimilarityFeature extends SheetSimilarityFeature {

    private final NormalizedLevenshtein levenshtein = new NormalizedLevenshtein();

    @Override
    public double score(File file1, File file2, Map<String, Sheet> sheets) {
        String fileName1 = sheets.get(file1.getName()).getExcelFileName();
        String fileName2 = sheets.get(file2.getName()).getExcelFileName();
        return levenshtein.similarity(fileName1, fileName2);
    }

    @Override
    public void score(Sheet current, List<Sheet> candidateSheets) {
        scoreMap = new HashMap<>();
        candidateSheets.forEach(sheet -> {
            String currentExcelName = current.getExcelFileName();
            String excelName = sheet.getExcelFileName();
            scoreMap.putIfAbsent(sheet, levenshtein.similarity(currentExcelName, excelName));
        });
    }
}
