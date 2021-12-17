package de.hpi.isg.modules;

import de.hpi.isg.elements.Cell;
import de.hpi.isg.json.JsonSheetEntry;
import de.hpi.isg.elements.PageComponents;
import de.hpi.isg.io.FileLoader;
import de.hpi.isg.json.SimpleJsonReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lan
 * @since 1/15/21
 */
public abstract class Module {

    protected String outputPath;

    protected List<JsonSheetEntry> loadedJsonSheetEntries;
    protected List<JSONObject> loadedFilesAsJson;

    protected JsonSheetEntry selectedJsonSheetEntry;

    protected Cell[][] cells;

    protected abstract void initializePageComponents(PageComponents pageComponents);

    public abstract void renderFile(ListSelectionModel selectionModel);

    public abstract void mouseOperationOnFileDisplayTable(MouseEvent e);

    public abstract void keyOperationOnFileDisplayTable(KeyEvent e);

    public abstract void storeAnnotationResults();

    /**
     * Load the selected dataset into RAM, and display the loaded files in the file review table.
     */
    public void loadDataset(JButton button, JTable displayTable) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("Select Input File Folder");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int choiceCode = chooser.showOpenDialog(button);
        if (choiceCode == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String loadedDatsetName = selectedFile.getName().split(".gz")[0];
            String path = selectedFile.getAbsolutePath().substring(0, selectedFile.getAbsolutePath().lastIndexOf(File.separator));
            this.outputPath = path + File.separator + loadedDatsetName;
            List<String> lines = new ArrayList<>();
            if (selectedFile.isFile()) {
                FileLoader loader = new FileLoader();
                lines.addAll(loader.loadGzipToLines(selectedFile));
            } else {
                FileLoader loader = new FileLoader();
                List<File> files = Arrays.stream(Objects.requireNonNull(selectedFile.listFiles()))
                        .filter(file -> !file.getName().equals(".DS_Store")).collect(Collectors.toList());
                for (File file : files) {
                    lines.addAll(loader.loadGzipToLines(file));
                }
            }
            loadedJsonSheetEntries = new ArrayList<>();
            SimpleJsonReader jsonReader = new SimpleJsonReader();
            loadedFilesAsJson = lines.stream().map(jsonReader::getJsonObject).collect(Collectors.toList());
            loadedFilesAsJson.sort((o1, o2) -> {
                String o1FileName = o1.get("file_name").toString();
                String o2FileName = o2.get("file_name").toString();
                return o1FileName.compareTo(o2FileName);
            });

            DefaultTableModel tableModel = new DefaultTableModel(0, 1) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            loadedFilesAsJson.forEach(jsonObject -> {
                String url = Objects.toString(jsonObject.get("url"), null);
                String fileName = Objects.toString(jsonObject.get("file_name"), null);
                String sheetName = Objects.toString(jsonObject.get("table_id"), null);
                String dictionary = Objects.toString(jsonObject.get("dict"), null);
                JSONArray table_array = (JSONArray) jsonObject.get("table_array");
                JSONArray feature_array = (JSONArray) jsonObject.get("feature_array");
                JSONArray feature_names = (JSONArray) jsonObject.get("feature_names");
                JSONArray annotations = (JSONArray) jsonObject.get("annotations");
                int numOfRows = Integer.parseInt(Objects.toString(jsonObject.get("num_rows"), "0"));
                int numOfColumns = Integer.parseInt(Objects.toString(jsonObject.get("num_cols"), "0"));
                JSONArray tok_tarr = (JSONArray) jsonObject.get("tok_tarr");
                JSONArray tok_tarr_reg = (JSONArray) jsonObject.get("tok_tarr_reg");
                JSONArray aggregation_annotations = jsonObject.get("aggregation_annotations") == null ?
                        null : (JSONArray) jsonObject.get("aggregation_annotations");
                String numberFormat = Objects.toString(jsonObject.get("number_format"), null);

                loadedJsonSheetEntries.add(new JsonSheetEntry(url, sheetName, fileName, dictionary, table_array, feature_array, feature_names,
                        annotations, numOfRows, numOfColumns, tok_tarr, tok_tarr_reg, aggregation_annotations, numberFormat));

                tableModel.addRow(new String[]{fileName + "@" + sheetName + ".csv"});
            });
            displayTable.setModel(tableModel);
            displayTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            displayTable.getTableHeader().setUI(null);
        }
    }
}
