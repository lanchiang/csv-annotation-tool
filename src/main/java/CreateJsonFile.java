import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * @author lan
 * @since 2020/9/28
 */
public class CreateJsonFile {

    public static void main(String[] args) throws IOException {
        String inputFileFolder = "/Users/lan/Documents/hpi/code/line-type-classification/src/main/resources/data340";

        String annotationFilePath = "/Users/lan/Documents/hpi/code/line-type-classification/src/main/resources/annotation_result.csv";

        CSVParser csvParser = new CSVParserBuilder().build();
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(annotationFilePath)).withCSVParser(csvParser).build();

        List<String[]> allLines = csvReader.readAll();

        List<File> files = Arrays.stream(Objects.requireNonNull(new File(inputFileFolder).listFiles()))
                .filter(file -> !file.getName().equals(".DS_Store")).collect(Collectors.toList());

        for (File file : files) {
            csvParser = new CSVParserBuilder().build();
            csvReader = new CSVReaderBuilder(new FileReader(annotationFilePath)).withCSVParser(csvParser).build();

            List<String[]> csvFile = csvReader.readAll();
            int file_length = csvFile.size();
            int file_width = csvFile.get(0).length;
        }

    }
}
