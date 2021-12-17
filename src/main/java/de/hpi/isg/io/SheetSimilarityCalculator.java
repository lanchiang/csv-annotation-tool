package de.hpi.isg.io;

import de.hpi.isg.elements.Sheet;
import de.hpi.isg.features.FileNameSimilarityFeature;
import de.hpi.isg.features.SheetAmountFeature;
import de.hpi.isg.features.SheetNameSimilarityFeature;
import de.hpi.isg.features.SheetSimilarityFeature;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Calculate the similarity between each two sheet files.
 *
 * @author Lan Jiang
 * @since 8/27/19
 */
public class SheetSimilarityCalculator {

    private final static int K = 5;

    private final List<File> files;

    private final Map<String, Sheet> sheets;

    private final Map<File, Map<File, Double>> topSimilarFilesByFile = new HashMap<>();

    @Getter
    private final Map<String, List<String>> sheetNamesByFileName = new HashMap<>();

    @Getter @Setter
    private Map<String, Map<String, Double>> similarities;

    public SheetSimilarityCalculator(File[] files) {
        this.files = Arrays.stream(files).filter(file -> !file.getName().equals(".DS_Store")).collect(Collectors.toList());

        this.sheets = new HashMap<>();
        this.files.forEach(file -> {
            String[] nameSplits = file.getName().split("@");
            String fileName = nameSplits[0];
            String sheetName = nameSplits[1].split(".csv")[0];

            sheets.putIfAbsent(file.getName(), new Sheet(sheetName, fileName));

            sheetNamesByFileName.putIfAbsent(fileName, new LinkedList<>());
            sheetNamesByFileName.get(fileName).add(sheetName);
        });
    }

    public void calculate() {
        Set<SheetSimilarityFeature> features = new HashSet<>();
        features.add(new FileNameSimilarityFeature());
        features.add(new SheetNameSimilarityFeature());
        features.add(new SheetAmountFeature(sheetNamesByFileName));

        for (int i = 0; i < files.size(); i++) {
            System.out.println(i);
            Map<File, Double> partialScores = new HashMap<>();
            for (int j = 0; j < files.size(); j++) {
                if (i == j) {
                    continue;
                }
                int finalI = i;
                int finalJ = j;
                List<Double> scores = features.stream().map(feature -> feature.score(files.get(finalI), files.get(finalJ), sheets)).collect(Collectors.toList());

                // average the scores
                double score = scores.stream().mapToDouble(d -> d).sum() / (double) scores.size();
                partialScores.putIfAbsent(files.get(j), score);
            }
            partialScores = partialScores.entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            partialScores = partialScores.entrySet()
                    .stream()
                    .limit(K)
                    .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);

            topSimilarFilesByFile.putIfAbsent(files.get(i), partialScores);
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("sheetSimilarity.txt"));
            topSimilarFilesByFile.entrySet()
                    .forEach(entry -> {
                        String line = "";
                        File file1 = entry.getKey();
                        line += file1.getName() + "\t";
                        for (Map.Entry<File, Double> scores : entry.getValue().entrySet()) {
                            try {
                                bufferedWriter.write(line + scores.getKey().getName() + "\t" + scores.getValue());
                                bufferedWriter.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
