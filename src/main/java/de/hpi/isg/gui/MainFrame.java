package de.hpi.isg.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.opencsv.CSVReader;
import de.hpi.isg.dao.DatabaseQueryHandler;
import de.hpi.isg.elements.*;
import de.hpi.isg.json.JsonReader;
import de.hpi.isg.json.JsonSheetEntry;
import de.hpi.isg.json.JsonWriter;
import de.hpi.isg.modules.AnnotateAggregationCellModule;
import de.hpi.isg.modules.AnnotateCellClassModule;
import de.hpi.isg.pojo.AnnotationPojo;
import de.hpi.isg.pojo.ResultPojo;
import de.hpi.isg.pojo.SpreadSheetPojo;
import de.hpi.isg.storage.JsonStore;
import de.hpi.isg.storage.Store;
import de.hpi.isg.swing.*;
import de.hpi.isg.utils.ColorSolution;
import de.hpi.isg.utils.GeneralUtils;
import lombok.Getter;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Lan Jiang
 * @since 8/26/19
 */
public class MainFrame {

    private JPanel mainPagePanel;
    private JLabel startLine;
    private JLabel endLine;
    private JLabel lineTypeDisplay;
    private JButton submitAllResultButton;
    private JTable sheetDisplayTable;
    private JTable sheetDisplayCellTable;
    private JPanel labelOperatingPanel;
    private JLabel startLineLabel;
    private JLabel endLineLabel;
    private JLabel lineTypeLabel;
    private JPanel submitPanel;
    private JScrollPane sheetDisplayPane;
    private JButton loadAllFilesButton;
    private JLabel loadedFileLabel;
    private JLabel loadedFileNumberLabel;
    private JLabel numOfLines;
    private JLabel numOfLinesLabel;
    private JLabel numOfColumns;
    private JPanel sheetStatPanel;
    private JPanel annotationPanel;
    private JTabbedPane menuTab;
    private JButton copyPatternButton;
    private JButton pastePatternButton;
    private JPanel loadFilePanel;
    private JLabel preambleColorLabel;
    private JLabel headerColorLabel;
    private JLabel dataColorLabel;
    private JLabel aggregationColorLabel;
    private JLabel footnoteColorLabel;
    private JLabel groupHeaderColorLabel;
    private JLabel emptyColorLabel;
    private JButton markAsMultitableButton;
    private JButton nextFileButton;
    private JPanel startEndJPanel;
    private JLabel preambleDesc;
    private JLabel headerDesc;
    private JLabel dataDesc;
    private JLabel aggregationDesc;
    private JLabel footnoteDesc;
    private JLabel groupheaderDesc;
    private JPanel lineTypeDescriptionPanel;
    private JLabel groupheaderExample;
    private JPanel howToUsePanel;
    private JPanel colorInstructionPanel;
    private JPanel patternOperationPanel;
    private JPanel operatingPanel;
    private JPanel exampleFigurePanel;
    private JLabel emptyDesc;
    private JTable annotationReviewTable;
    private JScrollPane annotationReviewScrollPane;
    private JButton returnToCurrentButton;
    private JScrollPane sheetDisplayCellPane;
    private JPanel operatingCellPanel;
    private JButton loadAnnotationButton;
    private JLabel preambleCellLabel;
    private JLabel headerCellLabel;
    private JLabel dataCellLabel;
    private JLabel aggregationCellLabel;
    private JLabel footnoteCellLabel;
    private JLabel groupHeaderCellLabel;
    private JLabel emptyCellLabel;
    private JTable annoReviewCellTable;
    private JLabel numOfColumnsCell;
    private JLabel numOfLinesCell;
    private JLabel topLeftCellText;
    private JLabel bottomRightCellText;
    private JLabel cellBlockTypeText;
    private JScrollPane annoReviewCellScrollPane;
    private JButton storeAllResultsButton;
    private JLabel selectedCellValue;
    private JButton submitResultsButton;
    private JRadioButton sumRadioButton;
    private JRadioButton relativeChangeRadioButton;
    private JRadioButton averageRadioButton;
    private JRadioButton divisionRadioButton;
    private JPanel operatingAggrPanel;
    private JButton aggrAnnotationLoadDatasetButton;
    private JTable fileReviewTable;
    private JScrollPane fileDisplayAggrPane;
    private JTextField errorTextField;
    private JPanel cellAnnotationMainPanel;
    private JPanel aggrAnnotationMainPanel;
    private JLabel numRowsAggr;
    private JLabel numColumnsAggr;
    private JLabel topleftIndexAggr;
    private JLabel bottomRightIndexAggr;
    private JPanel aggrFuncParasPanel;
    private JLabel operandOneCellRange;
    private JLabel operandTwoCellRange;
    private JRadioButton operandOneRadioButton;
    private JRadioButton operandTwoRadioButton;
    private JLabel errorMessageLabel;
    private JCheckBox hopSelectionModeCheckBox;
    private JButton calculateHopsButton;
    private JLabel modeHintLabel;
    private JButton nextAnnotationPreviewButton;
    private JButton lastAnnotationPreviewButton;
    private JRadioButton commaPointNumberFormat;
    private JRadioButton pointCommaNumberFormat;
    private JRadioButton spacePointNumberFormat;
    private JRadioButton spaceCommaNumberFormat;
    private JRadioButton nonePointNumberFormat;
    private JRadioButton noneCommaNumberFormat;
    private JRadioButton othersRadioButton;
    private JTable fileDisplayTableAggr;

    private int annotatedFileAmount = 0;

    private File[] loadedFiles;

    private List<JSONObject> loadedFilesAsJson;

    private List<JsonSheetEntry> loadedJsonSheetEntries;

    private String datasetName;

    private File currentFile;
    private SheetDisplayTableModel currentFileTableModel;
    private Sheet currentSheet;
    private boolean currentFileIsMultiTable;

    private long startTime;

    private long endTime;

    private Store store;

    private Color[] colorPattern;

    private String inputFileFolder;

    private String annotationReviewTableSelection;

    private AnnotateCellClassModule annotateCellClassModule;
    private AnnotateAggregationCellModule annotateAggregationCellModule;

    @Getter
    private DatabaseQueryHandler queryHandler = new DatabaseQueryHandler();

    public MainFrame() {

        $$$setupUI$$$();
        submitAllResultButton.addActionListener(e -> {
            // write the results into a json file.
            DefaultTableModel tableModel = (DefaultTableModel) sheetDisplayTable.getModel();
            if (tableModel.getColumnCount() != 0 || tableModel.getRowCount() != 0) {
                storeResultInMemory();

                saveResults();

                this.queryHandler.close();

                disableAllButtons();
            }
        });
        loadAllFilesButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setDialogTitle("Select Input File Folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

            int choiceCode = chooser.showOpenDialog(loadAllFilesButton);
            if (choiceCode == JFileChooser.APPROVE_OPTION) {
                File selectedDir = chooser.getSelectedFile();
                inputFileFolder = chooser.getSelectedFile().getPath();
                loadedFiles = selectedDir.listFiles();
                assert loadedFiles != null;
                List<File> fileList = Arrays.stream(loadedFiles).filter(file -> !file.getName().equals(".DS_Store")).collect(Collectors.toList());
                loadedFiles = fileList.toArray(new File[0]);

                final Map<String, List<String>> sheetNamesByFileName = new HashMap<>();
                fileList.forEach(file -> {
                    System.out.println(file.getName());
                    String[] nameSplits = GeneralUtils.splitFullName(file.getName());
                    sheetNamesByFileName.putIfAbsent(nameSplits[0], new LinkedList<>());
                    sheetNamesByFileName.get(nameSplits[0]).add(nameSplits[1]);
                });

                List<Sheet> sheets = new ArrayList<>();
                sheetNamesByFileName.forEach((key, value) -> value.forEach(sheetList -> {
                    sheets.add(new Sheet(sheetList, key, value.size()));
                }));
                store = new JsonStore(sheets);

                JsonReader jsonReader = new JsonReader();
                ResultPojo resultPojo = null;
                try {
                    resultPojo = jsonReader.read();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (resultPojo != null) {
                    resultPojo.getSpreadSheetPojos()
                            .forEach(spreadSheetPojo -> {
                                if (spreadSheetPojo.getIsMultitableFile().equals("true")) {
                                    AnnotationResults annotationResults = new AnnotationResults(
                                            spreadSheetPojo.getExcelFileName(),
                                            spreadSheetPojo.getSpreadsheetName(),
                                            spreadSheetPojo.getTimeExpense(),
                                            true
                                    );
                                    this.store.getSpreadsheet(spreadSheetPojo.getExcelFileName(), spreadSheetPojo.getSpreadsheetName()).setAnnotated(true);
                                    this.store.addAnnotation(annotationResults);
                                    return;
                                }

                                String fullName = GeneralUtils.createFullName(spreadSheetPojo.getExcelFileName(), spreadSheetPojo.getSpreadsheetName());
                                addToAnnotationReviewTable(fullName); // add one piece to the review table.

                                Optional<AnnotationPojo> optional = spreadSheetPojo.getAnnotationPojos().stream()
                                        .max(Comparator.comparingInt(AnnotationPojo::getEndLineNumber));
                                if (!optional.isPresent()) {
                                    throw new RuntimeException("The row count can not be obtained.");
                                }
                                int rowCount = optional.get().getEndLineNumber();
                                Color[] colors = new Color[rowCount];
                                spreadSheetPojo.getAnnotationPojos().forEach(annotationPojo -> {
                                    int start = annotationPojo.getStartLineNumber();
                                    int end = annotationPojo.getEndLineNumber();
                                    String lineType = annotationPojo.getLineType();
                                    for (int i = start; i <= end; i++) {
                                        colors[i - 1] = ColorSolution.getColor(lineType);
                                    }
                                });

                                AnnotationResults annotationResults = new AnnotationResults(
                                        spreadSheetPojo.getExcelFileName(),
                                        spreadSheetPojo.getSpreadsheetName(),
                                        spreadSheetPojo.getTimeExpense());
                                annotationResults.annotate(colors);
                                this.store.getSpreadsheet(spreadSheetPojo.getExcelFileName(), spreadSheetPojo.getSpreadsheetName()).setAnnotated(true);
                                this.store.addAnnotation(annotationResults);
                                this.currentFile = new File(this.inputFileFolder + "/" + fullName);
                                this.currentSheet = this.store.getSpreadsheet(spreadSheetPojo.getExcelFileName(), spreadSheetPojo.getSpreadsheetName());
                            });

                    annotatedFileAmount = resultPojo.getSpreadSheetPojos().size();
                }
                loadedFileNumberLabel.setText(annotatedFileAmount + "/" + loadedFiles.length);

                // if previously all the files have been annotated
                if (annotatedFileAmount == loadedFiles.length) {
                    nextFileButton.setEnabled(false);
                    returnToCurrentButton.setEnabled(false);
                    // do not load the next, because there is no next.
//                    try {
//                        loadFile(this.currentFile);
//                        drawTableBackgroundColor(this.currentFile.getName());
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
                } else {
                    nextFileButton.setEnabled(true);
                    loadNextFile();
                }

                markAsMultitableButton.setEnabled(true);
                submitAllResultButton.setEnabled(true);

                startTime = System.currentTimeMillis();
            }
        });
        ListSelectionModel sheetDisplayTableSelectionModel = sheetDisplayTable.getSelectionModel();
        sheetDisplayTableSelectionModel.addListSelectionListener(e -> {
            if (!sheetDisplayTableSelectionModel.isSelectionEmpty()) {
                int startIndex = sheetDisplayTableSelectionModel.getMinSelectionIndex() + 1;
                int endIndex = sheetDisplayTableSelectionModel.getMaxSelectionIndex() + 1;
                this.endLine.setText(String.valueOf(endIndex));
                this.startLine.setText(String.valueOf(startIndex));

                SheetDisplayTableModel sheetDisplayTableModel = (SheetDisplayTableModel) sheetDisplayTable.getModel();
                final Color currentColor = sheetDisplayTableModel.getRowColor(sheetDisplayTableSelectionModel.getMinSelectionIndex());
                String lineType = ColorSolution.getLineType(currentColor);
                if (lineType != null) {
                    lineTypeDisplay.setText(lineType);
                } else {
                    lineTypeDisplay.setText("n/a");
                }
            }
        });

        annoReviewCellTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                this.annotateCellClassModule.renderFile(annoReviewCellTable.getSelectionModel());
            }
        });

        fileReviewTable.getSelectionModel().addListSelectionListener(e -> {
//            System.out.println(e.getValueIsAdjusting());
            if (!e.getValueIsAdjusting()) {
                this.annotateAggregationCellModule.renderFile(fileReviewTable.getSelectionModel());
            }
        });

        ListSelectionModel annotationReviewTableSelectionModel = annotationReviewTable.getSelectionModel();
        annotationReviewTableSelectionModel.addListSelectionListener(e -> {
            if (!annotationReviewTableSelectionModel.isSelectionEmpty()) {
                this.nextFileButton.setEnabled(false);
//                this.submitAllResultButton.setEnabled(false);

                int selectedIndex = annotationReviewTableSelectionModel.getMinSelectionIndex();

                DefaultTableModel defaultTableModel = (DefaultTableModel) annotationReviewTable.getModel();
                String fileName = (String) defaultTableModel.getValueAt(selectedIndex, 0);

                if (this.currentFileTableModel == null) {
                    if (annotatedFileAmount == loadedFiles.length) {
                        try {
                            loadFile(new File(this.inputFileFolder + "/" + fileName));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    this.currentFileTableModel = (SheetDisplayTableModel) sheetDisplayTable.getModel();
                }

                if (annotationReviewTableSelection != null) {
                    if (!this.currentFileIsMultiTable) {
                        storeRevision();
                    }
                }

                try {
                    loadFile(new File(this.inputFileFolder + "/" + fileName));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
//                this.currentFileTableModel = (SheetDisplayTableModel) sheetDisplayTable.getModel();

                drawTableBackgroundColor(fileName);

                annotationReviewTableSelection = fileName;

                if (annotatedFileAmount == loadedFiles.length) {
                    this.returnToCurrentButton.setEnabled(false);
                } else {
                    returnToCurrentButton.setEnabled(true);
                }
            }
        });

        sheetDisplayCellTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                annotateCellClassModule.mouseOperationOnFileDisplayTable(e);
            }
        });

        sheetDisplayCellTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                annotateCellClassModule.keyOperationOnFileDisplayTable(e);
            }
        });

        fileDisplayTableAggr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                annotateAggregationCellModule.mouseOperationOnFileDisplayTable(e);
            }
        });

        fileDisplayTableAggr.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                annotateAggregationCellModule.keyOperationOnFileDisplayTable(e);
            }
        });

        ActionListener aggrFuncRadioGroupListener = e -> {
            this.annotateAggregationCellModule.prepareAggregationFunctionSetting(e.getActionCommand());
        };
        sumRadioButton.addActionListener(aggrFuncRadioGroupListener);
        relativeChangeRadioButton.addActionListener(aggrFuncRadioGroupListener);
        averageRadioButton.addActionListener(aggrFuncRadioGroupListener);
        divisionRadioButton.addActionListener(aggrFuncRadioGroupListener);
        othersRadioButton.addActionListener(aggrFuncRadioGroupListener);

        ActionListener numberFormatRadioGroupListener = e -> {
            this.annotateAggregationCellModule.selectNumberFormat(e.getActionCommand());
        };
        commaPointNumberFormat.addActionListener(numberFormatRadioGroupListener);
        pointCommaNumberFormat.addActionListener(numberFormatRadioGroupListener);
        spacePointNumberFormat.addActionListener(numberFormatRadioGroupListener);
        spaceCommaNumberFormat.addActionListener(numberFormatRadioGroupListener);
        noneCommaNumberFormat.addActionListener(numberFormatRadioGroupListener);
        nonePointNumberFormat.addActionListener(numberFormatRadioGroupListener);

        JPopupMenu lineTypePopupMenu = getLineTypePopupMenu();

        sheetDisplayTable.setComponentPopupMenu(lineTypePopupMenu);

        sheetDisplayTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ListSelectionModel sheetDisplayTableSelectionModel = sheetDisplayTable.getSelectionModel();
                if (!sheetDisplayTableSelectionModel.isSelectionEmpty()) {
                    int startIndex = sheetDisplayTableSelectionModel.getMinSelectionIndex() + 1;
                    int endIndex = sheetDisplayTableSelectionModel.getMaxSelectionIndex() + 1;
                    endLine.setText(String.valueOf(endIndex));
                    startLine.setText(String.valueOf(startIndex));

                    SheetDisplayTableModel sheetDisplayTableModel = (SheetDisplayTableModel) sheetDisplayTable.getModel();
                    final Color currentColor = sheetDisplayTableModel.getRowColor(sheetDisplayTableSelectionModel.getMinSelectionIndex());
                    String lineType = ColorSolution.getLineType(currentColor);
                    if (lineType != null) {
                        lineTypeDisplay.setText(lineType);
                    } else {
                        lineTypeDisplay.setText("n/a");
                    }
                }
            }
        });

        sheetDisplayTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.isControlDown() || e.isMetaDown()) {
                    return;
                }
                int startLineNumber = Integer.parseInt(startLine.getText());
                int endLineNumber = Integer.parseInt(endLine.getText());

                char pressedKeyChar = e.getKeyChar();
                SheetDisplayTableModel tableModel = (SheetDisplayTableModel) sheetDisplayTable.getModel();
                Color selectedColor;
                switch (pressedKeyChar) {
                    case 'P':
                    case 'p': {
                        selectedColor = ColorSolution.PREAMBLE_BACKGROUND_COLOR;
                        break;
                    }
                    case 'H':
                    case 'h': {
                        selectedColor = ColorSolution.HEADER_BACKGROUND_COLOR;
                        break;
                    }
                    case 'D':
                    case 'd': {
                        selectedColor = ColorSolution.DATA_BACKGROUND_COLOR;
                        break;
                    }
                    case 'A':
                    case 'a': {
                        selectedColor = ColorSolution.AGGREGATION_BACKGROUND_COLOR;
                        break;
                    }
                    case 'F':
                    case 'f': {
                        selectedColor = ColorSolution.FOOTNOTE_BACKGROUND_COLOR;
                        break;
                    }
                    case 'G':
                    case 'g': {
                        selectedColor = ColorSolution.GROUND_HEADER_BACKGROUND_COLOR;
                        break;
                    }
                    case 'E':
                    case 'e': {
                        selectedColor = ColorSolution.EMPTY_LINE_BACKGROUND_COLOR;
                        break;
                    }
                    default:
                        return;
                }
                tableModel.setRowsBackgroundColor(startLineNumber, endLineNumber, selectedColor);
                if (endLineNumber < tableModel.getRowCount()) {
                    sheetDisplayTable.requestFocus();
                    sheetDisplayTable.changeSelection(endLineNumber, 0, false, false);
                }
            }
        });
        copyPatternButton.addActionListener(e -> {
            int startLineIndex = sheetDisplayTableSelectionModel.getMinSelectionIndex();
            int endLineIndex = sheetDisplayTableSelectionModel.getMaxSelectionIndex();

            colorPattern = new Color[endLineIndex - startLineIndex + 1];
            SheetDisplayTableModel sheetDisplayTableModel = (SheetDisplayTableModel) this.sheetDisplayTable.getModel();
            for (int i = startLineIndex, j = 0; i <= endLineIndex; i++, j++) {
                colorPattern[j] = sheetDisplayTableModel.getRowColor(i);
            }
            System.out.println("Color pattern is copied");
            this.pastePatternButton.setEnabled(true);
        });
        pastePatternButton.addActionListener(e -> {
            if (colorPattern == null) {
                return;
            }
            int startLineIndex = sheetDisplayTableSelectionModel.getMinSelectionIndex() + 1;

            SheetDisplayTableModel sheetDisplayTableModel = (SheetDisplayTableModel) this.sheetDisplayTable.getModel();

            for (int i = 0; i < colorPattern.length; i++) {
                if (startLineIndex + i <= sheetDisplayTableModel.getRowCount()) {
                    sheetDisplayTableModel.setRowsBackgroundColor(startLineIndex + i, startLineIndex + i, colorPattern[i]);
                }
            }
            sheetDisplayTableModel.fireTableDataChanged();

            int endLineIndex = (startLineIndex - 1) + colorPattern.length;
            if (endLineIndex < sheetDisplayTableModel.getRowCount()) {
                sheetDisplayTable.requestFocus();
                sheetDisplayTable.changeSelection(endLineIndex, 0, false, false);
            }
        });
        markAsMultitableButton.addActionListener(e -> {
            endTime = System.currentTimeMillis();
            int selectCode = JOptionPane.showConfirmDialog(null,
                    "Are you sure to mark this spreadsheet as multi-table sheet?");
            if (selectCode != JOptionPane.OK_OPTION) {
                return;
            }

            String[] nameSplits;
            if (!annotationReviewTable.getSelectionModel().isSelectionEmpty()) {
                int selectionIndex = annotationReviewTable.getSelectionModel().getMinSelectionIndex();
                DefaultTableModel defaultTableModel = (DefaultTableModel) annotationReviewTable.getModel();
                String fileName = (String) defaultTableModel.getValueAt(selectionIndex, 0);
                nameSplits = GeneralUtils.splitFullName(fileName);
            } else {
                nameSplits = GeneralUtils.splitFullName(currentFile.getName());
            }
            AnnotationResults results = new AnnotationResults(nameSplits[0], nameSplits[1], endTime - startTime, true);

            this.store.addAnnotation(results);
            this.currentFileIsMultiTable = true;

            if (!annotationReviewTableSelectionModel.isSelectionEmpty()) {
                int selectionIndex = annotationReviewTableSelectionModel.getMinSelectionIndex();
                DefaultTableModel tableModel = (DefaultTableModel) annotationReviewTable.getModel();
                tableModel.removeRow(selectionIndex);
            }

            startTime = System.currentTimeMillis();
        });
        nextFileButton.addActionListener(e -> {
            // when this button is clicked, first do submit, after that load the next file.
            endTime = System.currentTimeMillis();
            DefaultTableModel tableModel = (DefaultTableModel) sheetDisplayTable.getModel();
            if (tableModel.getColumnCount() != 0 || tableModel.getRowCount() != 0) {
                if (!storeResultInMemory()) {
                    return;
                }
            } else {
                throw new RuntimeException("There is no sheet being displayed.");
            }

            this.currentFileTableModel = null;

            annotatedFileAmount++;
            this.loadedFileNumberLabel.setText(annotatedFileAmount + "/" + loadedFiles.length);
            loadNextFile();

            this.currentFileIsMultiTable = false;

            startTime = System.currentTimeMillis();
        });
        returnToCurrentButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                returnToCurrent();
                currentFileIsMultiTable = false;
                submitAllResultButton.setEnabled(true);
            }
        });
        loadAnnotationButton.addActionListener(e -> {
            this.annotateCellClassModule.loadDataset(loadAnnotationButton, annoReviewCellTable);
        });

        storeAllResultsButton.addActionListener(e -> {
            this.annotateCellClassModule.storeAnnotationResults();
        });
        aggrAnnotationLoadDatasetButton.addActionListener(e -> {
            this.annotateAggregationCellModule.loadDataset(aggrAnnotationLoadDatasetButton, fileReviewTable);
        });
        menuTab.addChangeListener(e -> {
            if (e.getSource() instanceof JTabbedPane) {
                JTabbedPane menuTabbedPane = (JTabbedPane) e.getSource();
                String title = menuTabbedPane.getTitleAt(menuTabbedPane.getSelectedIndex());
                if ("Cell Type Annotation".equals(title)) {
                    AnnotateCellPageComponents pageComponents = new AnnotateCellPageComponents(this.sheetDisplayCellTable, this.menuTab,
                            this.sheetDisplayCellPane, this.operatingCellPanel, this.loadAnnotationButton, this.preambleCellLabel,
                            this.headerCellLabel, this.dataCellLabel, this.aggregationCellLabel, this.footnoteCellLabel,
                            this.groupHeaderCellLabel, this.emptyCellLabel, this.annoReviewCellTable, this.numOfColumnsCell,
                            this.numOfLinesCell, this.topLeftCellText, this.bottomRightCellText, this.cellBlockTypeText,
                            this.annoReviewCellScrollPane, this.storeAllResultsButton, this.selectedCellValue, this.submitResultsButton);
                    this.annotateCellClassModule = (AnnotateCellClassModule) AnnotateCellClassModule.getInstance(pageComponents);
                }
                if ("Aggregation Annotation".equals(title)) {
                    AnnotateAggregationPageComponents pageComponents = new AnnotateAggregationPageComponents(this.sumRadioButton,
                            this.relativeChangeRadioButton, this.averageRadioButton, this.divisionRadioButton, this.othersRadioButton,
                            this.operatingAggrPanel, this.aggrAnnotationLoadDatasetButton, this.fileReviewTable,
                            this.fileDisplayAggrPane, this.errorTextField, this.aggrAnnotationMainPanel,
                            this.numRowsAggr, this.numColumnsAggr, this.topleftIndexAggr, this.bottomRightIndexAggr,
                            this.fileDisplayTableAggr, this.operandOneRadioButton, this.operandTwoRadioButton,
                            this.operandOneCellRange, this.operandTwoCellRange, this.errorMessageLabel, this.hopSelectionModeCheckBox,
                            this.calculateHopsButton, this.modeHintLabel, this.nextAnnotationPreviewButton, this.lastAnnotationPreviewButton,
                            this.commaPointNumberFormat, this.pointCommaNumberFormat, this.spacePointNumberFormat, this.spaceCommaNumberFormat,
                            this.nonePointNumberFormat, this.noneCommaNumberFormat);
                    this.annotateAggregationCellModule = (AnnotateAggregationCellModule) AnnotateAggregationCellModule.getInstance(pageComponents);
                }
            }

        });

        errorTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                annotateAggregationCellModule.onErrorParameterChanged();
            }
        });
        errorTextField.addActionListener(e -> annotateAggregationCellModule.onErrorParameterChanged());

        hopSelectionModeCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                annotateAggregationCellModule.enableHopSelection(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                annotateAggregationCellModule.enableHopSelection(false);
            }
        });
//        calculateHopsButton.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                annotateAggregationCellModule.detectPotentialAggregators();
//            }
//        });
        submitResultsButton.addActionListener(e -> {
            this.annotateAggregationCellModule.storeAnnotationResults();
        });
        nextAnnotationPreviewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
//                annotateAggregationCellModule.displayNextAnnotation();
            }
        });
        lastAnnotationPreviewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
//                annotateAggregationCellModule.displayLastAnnotation();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Verbose CSV File Annotation Tool");
        frame.setContentPane(new MainFrame().mainPagePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.pack();
        frame.setSize(screenSize);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPagePanel = new JPanel();
        mainPagePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        menuTab = new JTabbedPane();
        menuTab.setTabLayoutPolicy(1);
        menuTab.setTabPlacement(1);
        mainPagePanel.add(menuTab, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 337), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        menuTab.addTab("Instruction", panel1);
        lineTypeDescriptionPanel = new JPanel();
        lineTypeDescriptionPanel.setLayout(new GridLayoutManager(7, 2, new Insets(5, 10, 5, 10), -1, -1));
        panel1.add(lineTypeDescriptionPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        lineTypeDescriptionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Line Type Description", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        preambleDesc.setText("Preamble");
        lineTypeDescriptionPanel.add(preambleDesc, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, 16), null, 0, false));
        headerDesc.setText("Header");
        lineTypeDescriptionPanel.add(headerDesc, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, 16), null, 0, false));
        dataDesc.setText("Data");
        lineTypeDescriptionPanel.add(dataDesc, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, 16), null, 0, false));
        aggregationDesc.setText("Aggregation");
        lineTypeDescriptionPanel.add(aggregationDesc, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, 16), null, 0, false));
        footnoteDesc.setText("Footnote");
        lineTypeDescriptionPanel.add(footnoteDesc, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, 16), null, 0, false));
        groupheaderDesc.setText("Group title");
        lineTypeDescriptionPanel.add(groupheaderDesc, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, 16), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("A preamble line describes the characteristics of a table following it.");
        lineTypeDescriptionPanel.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("A header line describes the header of a table, including single column header line or cross column header line. A table may have multiple header lines.");
        lineTypeDescriptionPanel.add(label2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("A data line describes the value of a table.");
        lineTypeDescriptionPanel.add(label3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("An aggregation line displays the aggregation results (e.g., sum, average) of one or more other data lines in the table.");
        lineTypeDescriptionPanel.add(label4, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("A footnote line gives explanations of parts of or the whole table and appears after the table.");
        lineTypeDescriptionPanel.add(label5, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("A group header is a header of a group of lines in a table. In the example below, the line 9, 22, 30 are all group title lines for the following parts of the table..");
        lineTypeDescriptionPanel.add(label6, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        emptyDesc.setText("Empty");
        lineTypeDescriptionPanel.add(emptyDesc, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, 16), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("A empty line contains no values.");
        lineTypeDescriptionPanel.add(label7, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        howToUsePanel = new JPanel();
        howToUsePanel.setLayout(new GridLayoutManager(1, 1, new Insets(5, 10, 5, 10), -1, -1));
        panel1.add(howToUsePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        howToUsePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "How to Use", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label8 = new JLabel();
        label8.setText("Please read the instruction document.");
        howToUsePanel.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exampleFigurePanel = new JPanel();
        exampleFigurePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(exampleFigurePanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        groupheaderExample = new JLabel();
        groupheaderExample.setIcon(new ImageIcon(getClass().getResource("/Screen Shot 2019-10-18 at 9.40.51 AM.png")));
        groupheaderExample.setText("");
        exampleFigurePanel.add(groupheaderExample, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        menuTab.addTab("Line Type Annotation", panel2);
        operatingPanel = new JPanel();
        operatingPanel.setLayout(new GridBagLayout());
        panel2.add(operatingPanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        loadFilePanel = new JPanel();
        loadFilePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        operatingPanel.add(loadFilePanel, gbc);
        startEndJPanel = new JPanel();
        startEndJPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        loadFilePanel.add(startEndJPanel, gbc);
        startEndJPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        loadAllFilesButton = new JButton();
        loadAllFilesButton.setText("Start Annotation");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        startEndJPanel.add(loadAllFilesButton, gbc);
        submitAllResultButton = new JButton();
        submitAllResultButton.setEnabled(false);
        submitAllResultButton.setText("Submit all Results");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        startEndJPanel.add(submitAllResultButton, gbc);
        loadedFileNumberLabel = new JLabel();
        loadedFileNumberLabel.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        startEndJPanel.add(loadedFileNumberLabel, gbc);
        loadedFileLabel = new JLabel();
        loadedFileLabel.setText("Annotated / Loaded:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        startEndJPanel.add(loadedFileLabel, gbc);
        submitPanel = new JPanel();
        submitPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        loadFilePanel.add(submitPanel, gbc);
        submitPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        markAsMultitableButton = new JButton();
        markAsMultitableButton.setEnabled(false);
        markAsMultitableButton.setText("Mark as Multitable File");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        submitPanel.add(markAsMultitableButton, gbc);
        nextFileButton = new JButton();
        nextFileButton.setEnabled(false);
        nextFileButton.setText("Next File");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        submitPanel.add(nextFileButton, gbc);
        returnToCurrentButton = new JButton();
        returnToCurrentButton.setEnabled(false);
        returnToCurrentButton.setText("Return to Current");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        submitPanel.add(returnToCurrentButton, gbc);
        annotationPanel = new JPanel();
        annotationPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        operatingPanel.add(annotationPanel, gbc);
        labelOperatingPanel = new JPanel();
        labelOperatingPanel.setLayout(new GridLayoutManager(3, 2, new Insets(5, 5, 5, 5), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        annotationPanel.add(labelOperatingPanel, gbc);
        labelOperatingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Block selection", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, -1, labelOperatingPanel.getFont()), new Color(-16777216)));
        startLineLabel = new JLabel();
        startLineLabel.setText("Start Line");
        labelOperatingPanel.add(startLineLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        startLine = new JLabel();
        startLine.setText("n/a");
        labelOperatingPanel.add(startLine, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        lineTypeLabel = new JLabel();
        lineTypeLabel.setText("Line Function Type");
        lineTypeLabel.setVerticalAlignment(0);
        lineTypeLabel.setVerticalTextPosition(0);
        labelOperatingPanel.add(lineTypeLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(151, 16), null, 1, false));
        endLineLabel = new JLabel();
        endLineLabel.setText("End Line");
        labelOperatingPanel.add(endLineLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        endLine = new JLabel();
        endLine.setText("n/a");
        labelOperatingPanel.add(endLine, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        lineTypeDisplay = new JLabel();
        lineTypeDisplay.setText("n/a");
        labelOperatingPanel.add(lineTypeDisplay, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        colorInstructionPanel = new JPanel();
        colorInstructionPanel.setLayout(new GridLayoutManager(7, 1, new Insets(5, 5, 5, 5), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        annotationPanel.add(colorInstructionPanel, gbc);
        preambleColorLabel.setText("Preamble (P)");
        colorInstructionPanel.add(preambleColorLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        headerColorLabel.setText("Header (H)");
        colorInstructionPanel.add(headerColorLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        dataColorLabel.setText("Data (D)");
        colorInstructionPanel.add(dataColorLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        aggregationColorLabel.setText("Aggregation (A)");
        colorInstructionPanel.add(aggregationColorLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        footnoteColorLabel.setText("Footnote (F)");
        colorInstructionPanel.add(footnoteColorLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        groupHeaderColorLabel.setText("Group header (G)");
        colorInstructionPanel.add(groupHeaderColorLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        emptyColorLabel.setText("Empty (E)");
        colorInstructionPanel.add(emptyColorLabel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        sheetStatPanel = new JPanel();
        sheetStatPanel.setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        annotationPanel.add(sheetStatPanel, gbc);
        sheetStatPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Spreadsheet statistics", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        numOfLinesLabel = new JLabel();
        numOfLinesLabel.setText("Number of Lines:");
        sheetStatPanel.add(numOfLinesLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numOfColumns = new JLabel();
        numOfColumns.setText("");
        sheetStatPanel.add(numOfColumns, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Number of Columns");
        sheetStatPanel.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numOfLines = new JLabel();
        numOfLines.setText("");
        sheetStatPanel.add(numOfLines, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        annotationReviewScrollPane = new JScrollPane();
        annotationReviewScrollPane.setAutoscrolls(false);
        annotationReviewScrollPane.setMaximumSize(new Dimension(-1, 500));
        annotationReviewScrollPane.setPreferredSize(new Dimension(-1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        annotationPanel.add(annotationReviewScrollPane, gbc);
        annotationReviewScrollPane.setBorder(BorderFactory.createTitledBorder(null, "Spreadsheet Annotation Review", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        annotationReviewTable = new JTable();
        annotationReviewTable.setAutoCreateRowSorter(false);
        annotationReviewScrollPane.setViewportView(annotationReviewTable);
        patternOperationPanel = new JPanel();
        patternOperationPanel.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        annotationPanel.add(patternOperationPanel, gbc);
        patternOperationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Pattern operation", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        copyPatternButton = new JButton();
        copyPatternButton.setEnabled(false);
        copyPatternButton.setText("Copy pattern");
        patternOperationPanel.add(copyPatternButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(200, -1), 0, false));
        pastePatternButton = new JButton();
        pastePatternButton.setEnabled(false);
        pastePatternButton.setText("Paste pattern");
        patternOperationPanel.add(pastePatternButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(200, -1), 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        panel3.add(sheetDisplayPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 1, false));
        sheetDisplayPane.setBorder(BorderFactory.createTitledBorder(null, "Spreedsheet", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        menuTab.addTab("Multitable Annotation", panel4);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cellAnnotationMainPanel = new JPanel();
        cellAnnotationMainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        menuTab.addTab("Cell Type Annotation", cellAnnotationMainPanel);
        operatingCellPanel = new JPanel();
        operatingCellPanel.setLayout(new GridBagLayout());
        cellAnnotationMainPanel.add(operatingCellPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        operatingCellPanel.add(panel7, gbc);
        storeAllResultsButton = new JButton();
        storeAllResultsButton.setEnabled(true);
        storeAllResultsButton.setText("Submit all Results");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel7.add(storeAllResultsButton, gbc);
        loadAnnotationButton = new JButton();
        loadAnnotationButton.setText("Start Annotation");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel7.add(loadAnnotationButton, gbc);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        operatingCellPanel.add(panel8, gbc);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(3, 2, new Insets(5, 5, 5, 5), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel8.add(panel9, gbc);
        panel9.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Block selection", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, -1, panel9.getFont()), new Color(-16777216)));
        final JLabel label10 = new JLabel();
        label10.setText("Top left cell");
        panel9.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        topLeftCellText = new JLabel();
        topLeftCellText.setText("n/a");
        panel9.add(topLeftCellText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Cell Function Type");
        label11.setVerticalAlignment(0);
        label11.setVerticalTextPosition(0);
        panel9.add(label11, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(151, 16), null, 1, false));
        final JLabel label12 = new JLabel();
        label12.setText("Bottom right cell");
        panel9.add(label12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        bottomRightCellText = new JLabel();
        bottomRightCellText.setText("n/a");
        panel9.add(bottomRightCellText, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        cellBlockTypeText = new JLabel();
        cellBlockTypeText.setText("n/a");
        panel9.add(cellBlockTypeText, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(6, 1, new Insets(5, 5, 5, 5), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel8.add(panel10, gbc);
        preambleCellLabel.setText("Metadata (M)");
        panel10.add(preambleCellLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        headerCellLabel.setText("Header (H)");
        panel10.add(headerCellLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        dataCellLabel.setText("Data (D)");
        panel10.add(dataCellLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        aggregationCellLabel.setText("Aggregation (A)");
        panel10.add(aggregationCellLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        footnoteCellLabel.setText("Footnote (F)");
        panel10.add(footnoteCellLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        groupHeaderCellLabel.setText("Group header (G)");
        panel10.add(groupHeaderCellLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(141, -1), null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel8.add(panel11, gbc);
        panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Spreadsheet statistics", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label13 = new JLabel();
        label13.setText("Number of Lines:");
        panel11.add(label13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numOfColumnsCell = new JLabel();
        numOfColumnsCell.setText("");
        panel11.add(numOfColumnsCell, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Number of Columns");
        panel11.add(label14, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numOfLinesCell = new JLabel();
        numOfLinesCell.setText("");
        panel11.add(numOfLinesCell, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        annoReviewCellScrollPane = new JScrollPane();
        annoReviewCellScrollPane.setAutoscrolls(false);
        annoReviewCellScrollPane.setMaximumSize(new Dimension(-1, 500));
        annoReviewCellScrollPane.setPreferredSize(new Dimension(-1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel8.add(annoReviewCellScrollPane, gbc);
        annoReviewCellScrollPane.setBorder(BorderFactory.createTitledBorder(null, "Verbose CSV File Selection View", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        annoReviewCellTable = new JTable();
        annoReviewCellTable.setPreferredScrollableViewportSize(new Dimension(450, -1));
        annoReviewCellScrollPane.setViewportView(annoReviewCellTable);
        cellAnnotationMainPanel.add(sheetDisplayCellPane, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 1, new Insets(0, 30, 0, 30), -1, -1));
        cellAnnotationMainPanel.add(panel12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectedCellValue = new JLabel();
        selectedCellValue.setText("n/a");
        panel12.add(selectedCellValue, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 20), new Dimension(-1, 20), new Dimension(-1, 20), 2, false));
        aggrAnnotationMainPanel = new JPanel();
        aggrAnnotationMainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        menuTab.addTab("Aggregation Annotation", aggrAnnotationMainPanel);
        operatingAggrPanel = new JPanel();
        operatingAggrPanel.setLayout(new GridBagLayout());
        aggrAnnotationMainPanel.add(operatingAggrPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.01;
        gbc.fill = GridBagConstraints.BOTH;
        operatingAggrPanel.add(panel13, gbc);
        aggrAnnotationLoadDatasetButton = new JButton();
        aggrAnnotationLoadDatasetButton.setText("Load dataset");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel13.add(aggrAnnotationLoadDatasetButton, gbc);
        submitResultsButton = new JButton();
        submitResultsButton.setText("Submit results");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel13.add(submitResultsButton, gbc);
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.07;
        gbc.fill = GridBagConstraints.BOTH;
        operatingAggrPanel.add(panel14, gbc);
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel14.add(panel15, gbc);
        panel15.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Selected Block", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, -1, panel15.getFont()), null));
        final JLabel label15 = new JLabel();
        label15.setText("Top-left cell index");
        panel15.add(label15, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("Bottom-right cell index");
        panel15.add(label16, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        topleftIndexAggr = new JLabel();
        topleftIndexAggr.setText("N/A");
        panel15.add(topleftIndexAggr, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bottomRightIndexAggr = new JLabel();
        bottomRightIndexAggr.setText("N/A");
        panel15.add(bottomRightIndexAggr, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel14.add(panel16, gbc);
        panel16.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "File Statistics", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label17 = new JLabel();
        label17.setText("# rows");
        panel16.add(label17, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("# columns");
        panel16.add(label18, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numRowsAggr = new JLabel();
        numRowsAggr.setText("N/A");
        panel16.add(numRowsAggr, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numColumnsAggr = new JLabel();
        numColumnsAggr.setText("N/A");
        panel16.add(numColumnsAggr, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        operatingAggrPanel.add(panel17, gbc);
        panel17.setBorder(BorderFactory.createTitledBorder(null, "Aggregation Settings", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        aggrFuncParasPanel = new JPanel();
        aggrFuncParasPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.06;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        panel17.add(aggrFuncParasPanel, gbc);
        aggrFuncParasPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Function Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        operandOneCellRange = new JLabel();
        operandOneCellRange.setText("N/A");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        aggrFuncParasPanel.add(operandOneCellRange, gbc);
        operandTwoCellRange = new JLabel();
        operandTwoCellRange.setText("N/A");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        aggrFuncParasPanel.add(operandTwoCellRange, gbc);
        operandOneRadioButton = new JRadioButton();
        operandOneRadioButton.setText("Operand 1:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        aggrFuncParasPanel.add(operandOneRadioButton, gbc);
        operandTwoRadioButton = new JRadioButton();
        operandTwoRadioButton.setText("Operand 2:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        aggrFuncParasPanel.add(operandTwoRadioButton, gbc);
        errorMessageLabel = new JLabel();
        errorMessageLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        aggrFuncParasPanel.add(errorMessageLabel, gbc);
        hopSelectionModeCheckBox = new JCheckBox();
        hopSelectionModeCheckBox.setText("Free selection");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        aggrFuncParasPanel.add(hopSelectionModeCheckBox, gbc);
        modeHintLabel = new JLabel();
        modeHintLabel.setText("View Mode");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        aggrFuncParasPanel.add(modeHintLabel, gbc);
        nextAnnotationPreviewButton = new JButton();
        nextAnnotationPreviewButton.setEnabled(false);
        nextAnnotationPreviewButton.setPreferredSize(new Dimension(27, 27));
        nextAnnotationPreviewButton.setText("▶");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.insets = new Insets(5, 5, 5, 5);
        aggrFuncParasPanel.add(nextAnnotationPreviewButton, gbc);
        lastAnnotationPreviewButton = new JButton();
        lastAnnotationPreviewButton.setEnabled(false);
        lastAnnotationPreviewButton.setPreferredSize(new Dimension(27, 27));
        lastAnnotationPreviewButton.setText("◀");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        aggrFuncParasPanel.add(lastAnnotationPreviewButton, gbc);
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.01;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel17.add(panel18, gbc);
        panel18.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Function", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        sumRadioButton = new JRadioButton();
        sumRadioButton.setSelected(false);
        sumRadioButton.setText("Sum");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel18.add(sumRadioButton, gbc);
        relativeChangeRadioButton = new JRadioButton();
        relativeChangeRadioButton.setText("RelativeChange");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel18.add(relativeChangeRadioButton, gbc);
        divisionRadioButton = new JRadioButton();
        divisionRadioButton.setText("Division");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel18.add(divisionRadioButton, gbc);
        othersRadioButton = new JRadioButton();
        othersRadioButton.setText("Others");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel18.add(othersRadioButton, gbc);
        averageRadioButton = new JRadioButton();
        averageRadioButton.setText("Average");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel18.add(averageRadioButton, gbc);
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.02;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        panel17.add(panel19, gbc);
        panel19.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Global Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label19 = new JLabel();
        label19.setText("Error");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel19.add(label19, gbc);
        errorTextField = new JTextField();
        errorTextField.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel19.add(errorTextField, gbc);
        final JPanel panel20 = new JPanel();
        panel20.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.02;
        gbc.fill = GridBagConstraints.BOTH;
        panel17.add(panel20, gbc);
        panel20.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Number Format", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        commaPointNumberFormat = new JRadioButton();
        commaPointNumberFormat.setText("Comma Point");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel20.add(commaPointNumberFormat, gbc);
        pointCommaNumberFormat = new JRadioButton();
        pointCommaNumberFormat.setText("Point Comma");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel20.add(pointCommaNumberFormat, gbc);
        spacePointNumberFormat = new JRadioButton();
        spacePointNumberFormat.setText("Space Point");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel20.add(spacePointNumberFormat, gbc);
        spaceCommaNumberFormat = new JRadioButton();
        spaceCommaNumberFormat.setText("Space Comma");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel20.add(spaceCommaNumberFormat, gbc);
        nonePointNumberFormat = new JRadioButton();
        nonePointNumberFormat.setText("None Point");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel20.add(nonePointNumberFormat, gbc);
        noneCommaNumberFormat = new JRadioButton();
        noneCommaNumberFormat.setText("None Comma");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel20.add(noneCommaNumberFormat, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setMaximumSize(new Dimension(-1, -1));
        scrollPane1.setMinimumSize(new Dimension(-1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        operatingAggrPanel.add(scrollPane1, gbc);
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Verbose CSV File Selection", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        fileReviewTable = new JTable();
        fileReviewTable.setPreferredScrollableViewportSize(new Dimension(-1, -1));
        scrollPane1.setViewportView(fileReviewTable);
        aggrAnnotationMainPanel.add(fileDisplayAggrPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(sumRadioButton);
        buttonGroup.add(relativeChangeRadioButton);
        buttonGroup.add(averageRadioButton);
        buttonGroup.add(divisionRadioButton);
        buttonGroup.add(othersRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(operandOneRadioButton);
        buttonGroup.add(operandTwoRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(commaPointNumberFormat);
        buttonGroup.add(commaPointNumberFormat);
        buttonGroup.add(pointCommaNumberFormat);
        buttonGroup.add(spacePointNumberFormat);
        buttonGroup.add(spaceCommaNumberFormat);
        buttonGroup.add(nonePointNumberFormat);
        buttonGroup.add(noneCommaNumberFormat);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPagePanel;
    }

    private void createUIComponents() {
//        resetReviewTable();

        sheetDisplayTable = new JTable();
        sheetDisplayPane = new JScrollPane(sheetDisplayTable);
        JTable rowTable = new RowNumberTable(sheetDisplayTable);
        sheetDisplayPane.setRowHeaderView(rowTable);

        sheetDisplayCellTable = new JTable();
        sheetDisplayCellPane = new JScrollPane(sheetDisplayCellTable);
        JTable rowCellTable = new RowNumberTable(sheetDisplayCellTable);
        sheetDisplayCellPane.setRowHeaderView(rowCellTable);

        fileDisplayTableAggr = new JTable();
        fileDisplayAggrPane = new JScrollPane(fileDisplayTableAggr);
        JTable rowTableAggr = new RowNumberTable(fileDisplayTableAggr);
        fileDisplayAggrPane.setRowHeaderView(rowTableAggr);
//        fileDisplayAggrPane.setColumnHeaderView(rowTableAggr);

        preambleColorLabel = new JLabel("Preamble (P)");
        preambleColorLabel.setOpaque(true);
        preambleColorLabel.setBackground(ColorSolution.PREAMBLE_BACKGROUND_COLOR);
        headerColorLabel = new JLabel("Header (H)");
        headerColorLabel.setOpaque(true);
        headerColorLabel.setBackground(ColorSolution.HEADER_BACKGROUND_COLOR);
        dataColorLabel = new JLabel("Data (D)");
        dataColorLabel.setOpaque(true);
        dataColorLabel.setBackground(ColorSolution.DATA_BACKGROUND_COLOR);
        aggregationColorLabel = new JLabel("Aggregation (A)");
        aggregationColorLabel.setOpaque(true);
        aggregationColorLabel.setBackground(ColorSolution.AGGREGATION_BACKGROUND_COLOR);
        footnoteColorLabel = new JLabel("Footnote (F)");
        footnoteColorLabel.setOpaque(true);
        footnoteColorLabel.setBackground(ColorSolution.FOOTNOTE_BACKGROUND_COLOR);
        groupHeaderColorLabel = new JLabel("Group header (G)");
        groupHeaderColorLabel.setOpaque(true);
        groupHeaderColorLabel.setBackground(ColorSolution.GROUND_HEADER_BACKGROUND_COLOR);
        emptyColorLabel = new JLabel("Empty (E)");
        emptyColorLabel.setOpaque(true);
        emptyColorLabel.setBackground(ColorSolution.EMPTY_LINE_BACKGROUND_COLOR);

        preambleCellLabel = new JLabel("Preamble (P)");
        preambleCellLabel.setOpaque(true);
        preambleCellLabel.setBackground(ColorSolution.PREAMBLE_BACKGROUND_COLOR);
        headerCellLabel = new JLabel("Header (H)");
        headerCellLabel.setOpaque(true);
        headerCellLabel.setBackground(ColorSolution.HEADER_BACKGROUND_COLOR);
        dataCellLabel = new JLabel("Data (D)");
        dataCellLabel.setOpaque(true);
        dataCellLabel.setBackground(ColorSolution.DATA_BACKGROUND_COLOR);
        aggregationCellLabel = new JLabel("Aggregation (A)");
        aggregationCellLabel.setOpaque(true);
        aggregationCellLabel.setBackground(ColorSolution.AGGREGATION_BACKGROUND_COLOR);
        footnoteCellLabel = new JLabel("Footnote (F)");
        footnoteCellLabel.setOpaque(true);
        footnoteCellLabel.setBackground(ColorSolution.FOOTNOTE_BACKGROUND_COLOR);
        groupHeaderCellLabel = new JLabel("Group header (G)");
        groupHeaderCellLabel.setOpaque(true);
        groupHeaderCellLabel.setBackground(ColorSolution.GROUND_HEADER_BACKGROUND_COLOR);
        emptyCellLabel = new JLabel("Empty (E)");
        emptyCellLabel.setOpaque(true);
        emptyCellLabel.setBackground(ColorSolution.EMPTY_LINE_BACKGROUND_COLOR);

        preambleDesc = new JLabel("Preamble (P)");
        preambleDesc.setOpaque(true);
        preambleDesc.setBackground(ColorSolution.PREAMBLE_BACKGROUND_COLOR);
        headerDesc = new JLabel("Header (H)");
        headerDesc.setOpaque(true);
        headerDesc.setBackground(ColorSolution.HEADER_BACKGROUND_COLOR);
        dataDesc = new JLabel("Data (D)");
        dataDesc.setOpaque(true);
        dataDesc.setBackground(ColorSolution.DATA_BACKGROUND_COLOR);
        aggregationDesc = new JLabel("Aggregation (A)");
        aggregationDesc.setOpaque(true);
        aggregationDesc.setBackground(ColorSolution.AGGREGATION_BACKGROUND_COLOR);
        footnoteDesc = new JLabel("Footnote (F)");
        footnoteDesc.setOpaque(true);
        footnoteDesc.setBackground(ColorSolution.FOOTNOTE_BACKGROUND_COLOR);
        groupheaderDesc = new JLabel("Group header (G)");
        groupheaderDesc.setOpaque(true);
        groupheaderDesc.setBackground(ColorSolution.GROUND_HEADER_BACKGROUND_COLOR);
        emptyDesc = new JLabel("Empty (E)");
        emptyDesc.setOpaque(true);
        emptyDesc.setBackground(ColorSolution.EMPTY_LINE_BACKGROUND_COLOR);
    }

    private void loadFile(final File file) throws IOException {
        System.out.println(file.getName());

        CSVReader reader = new CSVReader(new FileReader(file));
        List<String[]> dataEntries = reader.readAll();

        this.numOfLines.setText(String.valueOf(dataEntries.size()));
        this.numOfColumns.setText(String.valueOf(dataEntries.get(0).length));

        SheetDisplayTableModel tableModel = new SheetDisplayTableModel(0, dataEntries.get(0).length);

        tableModel.insertRows(dataEntries);

        sheetDisplayTable.setModel(tableModel);

        sheetDisplayTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        sheetDisplayTable.setDefaultRenderer(Object.class, new SheetDisplayLineTypeRowRenderer(dataEntries.size(), dataEntries.get(0).length));

        sheetDisplayTable.setColumnSelectionAllowed(false);
        sheetDisplayTable.setRowSelectionAllowed(true);

        sheetDisplayPane.setBorder(BorderFactory.createTitledBorder(file.getName()));

        this.copyPatternButton.setEnabled(true);

        resizeColumnWidth(sheetDisplayTable);

        System.out.println(tableModel.getColumnCount() + "\t" + tableModel.getRowCount());
    }

    private JPopupMenu getLineTypePopupMenu() {
        AtomicInteger startIndex = new AtomicInteger();
        AtomicInteger endIndex = new AtomicInteger();
        ListSelectionModel sheetDisplayTableSelectionModel = sheetDisplayTable.getSelectionModel();
        sheetDisplayTableSelectionModel.addListSelectionListener(e -> {
            if (!sheetDisplayTableSelectionModel.isSelectionEmpty()) {
                startIndex.set(sheetDisplayTableSelectionModel.getMinSelectionIndex() + 1);
                endIndex.set(sheetDisplayTableSelectionModel.getMaxSelectionIndex() + 1);
            }
        });

        JPopupMenu lineTypePopupMenu = new JPopupMenu("Line type Popup Menu");
        JMenuItem lineTypeMenuItem = new JMenuItem("Preamble (P)");
        lineTypeMenuItem.addActionListener(e -> {
            System.out.println(e.getActionCommand());
            SheetDisplayTableModel tableModel = (SheetDisplayTableModel) this.sheetDisplayTable.getModel();
            tableModel.setRowsBackgroundColor(startIndex.get(), endIndex.get(), ColorSolution.PREAMBLE_BACKGROUND_COLOR);
        });
        lineTypePopupMenu.add(lineTypeMenuItem);

        lineTypeMenuItem = new JMenuItem("Header (H)");
        lineTypeMenuItem.addActionListener(e -> {
            System.out.println(e.getActionCommand());
            SheetDisplayTableModel tableModel = (SheetDisplayTableModel) this.sheetDisplayTable.getModel();
            tableModel.setRowsBackgroundColor(startIndex.get(), endIndex.get(), ColorSolution.HEADER_BACKGROUND_COLOR);
        });
        lineTypePopupMenu.add(lineTypeMenuItem);

        lineTypeMenuItem = new JMenuItem("Data (D)");
        lineTypeMenuItem.addActionListener(e -> {
            System.out.println(e.getActionCommand());
            SheetDisplayTableModel tableModel = (SheetDisplayTableModel) this.sheetDisplayTable.getModel();
            tableModel.setRowsBackgroundColor(startIndex.get(), endIndex.get(), ColorSolution.DATA_BACKGROUND_COLOR);
        });
        lineTypePopupMenu.add(lineTypeMenuItem);

        lineTypeMenuItem = new JMenuItem("Aggregation (A)");
        lineTypeMenuItem.addActionListener(e -> {
            System.out.println(e.getActionCommand());
            SheetDisplayTableModel tableModel = (SheetDisplayTableModel) this.sheetDisplayTable.getModel();
            tableModel.setRowsBackgroundColor(startIndex.get(), endIndex.get(), ColorSolution.AGGREGATION_BACKGROUND_COLOR);
        });
        lineTypePopupMenu.add(lineTypeMenuItem);

        lineTypeMenuItem = new JMenuItem("Footnote (F)");
        lineTypeMenuItem.addActionListener(e -> {
            System.out.println(e.getActionCommand());
            SheetDisplayTableModel tableModel = (SheetDisplayTableModel) this.sheetDisplayTable.getModel();
            tableModel.setRowsBackgroundColor(startIndex.get(), endIndex.get(), ColorSolution.FOOTNOTE_BACKGROUND_COLOR);
        });
        lineTypePopupMenu.add(lineTypeMenuItem);

        lineTypeMenuItem = new JMenuItem("Group header (G)");
        lineTypeMenuItem.addActionListener(e -> {
            System.out.println(e.getActionCommand());
            SheetDisplayTableModel tableModel = (SheetDisplayTableModel) this.sheetDisplayTable.getModel();
            tableModel.setRowsBackgroundColor(startIndex.get(), endIndex.get(), ColorSolution.GROUND_HEADER_BACKGROUND_COLOR);
        });
        lineTypePopupMenu.add(lineTypeMenuItem);
        return lineTypePopupMenu;
    }

    /**
     * Store the annotation result of this file in the memory.
     *
     * @return
     */
    private boolean storeResultInMemory() {
        if (currentFileIsMultiTable) {
//            if (!returnToCurrentButton.isEnabled()) {
//                addToAnnotationReviewTable(currentFile.getName());
//            }
            return true;
        }

        SheetDisplayTableModel sheetDisplayTableModel = (SheetDisplayTableModel) sheetDisplayTable.getModel();
        if (sheetDisplayTableModel.hasUnannotatedLines()) {
            int selectCode = JOptionPane.showConfirmDialog(null,
                    "Some lines of this file are not annotated yet. Do you still want to finish it? Click on \"Yes\" will automatically annotate this lines as empty lines");
            if (selectCode != JOptionPane.OK_OPTION) {
                return false;
            }
        }

        String[] nameSplits = GeneralUtils.splitFullName(currentFile.getName());
        AnnotationResults results = new AnnotationResults(nameSplits[0], nameSplits[1], endTime - startTime, false);

        results.annotate(sheetDisplayTableModel);

        this.store.addAnnotation(results);

        // add a piece to the annotation review table.
        addToAnnotationReviewTable(currentFile.getName());

        return true;
    }

    private void addToAnnotationReviewTable(String fileName) {
        DefaultTableModel tableModel = (DefaultTableModel) annotationReviewTable.getModel();
        tableModel.addRow(new String[]{fileName});
    }

    private void loadNextFile() {
        if (currentFile == null) {
            // load a random new table
            Random random = new Random(System.currentTimeMillis());
            int selectedIndex = random.nextInt(loadedFiles.length);

            currentFile = loadedFiles[selectedIndex];

            String[] nameSplits = GeneralUtils.splitFullName(currentFile.getName());

            currentSheet = this.store.getSpreadsheet(nameSplits[0], nameSplits[1]);
        } else {
            // get the most similar file
            Sheet mostSimilarSheet = store.findMostSimilarSheet(currentSheet);
            if (mostSimilarSheet == null) {
                int selectionCode = JOptionPane.showConfirmDialog(null, "No next file. All files have been annotated. Do you want to save all the annotation results now?");
                if (selectionCode == JOptionPane.OK_OPTION) {
                    saveResults();

                    disableAllButtons();

                    if (annotatedFileAmount != loadedFiles.length) {
                        annotatedFileAmount++;
                        this.loadedFileNumberLabel.setText(annotatedFileAmount + "/" + loadedFiles.length);
                    }
                }
                return;
            }

            currentFile = new File(this.inputFileFolder + "/" + GeneralUtils.createFullName(mostSimilarSheet.getExcelFileName(), mostSimilarSheet.getSheetName()));
            System.out.println(currentFile.getPath());

            currentSheet = mostSimilarSheet;
        }

//        this.loadedFileNumberLabel.setText(annotatedFileAmount + "/" + loadedFiles.length);

        try {
            loadFile(currentFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Todo: this function should be renamed to reflect its task better.
    public static void resizeColumnWidth(JTable table) {
        // set row height
//        table.setRowHeight(20);

        // set column width
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 30; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 250)
                width = 250;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    private void saveResults() {
        ((JsonStore) store).generateResultCache();
        JsonWriter<SpreadSheetPojo> writer = new JsonWriter<>();
        writer.write(((JsonStore) store).getResultCache());

        JOptionPane.showMessageDialog(null, "Annotation results have been saved to the file \"annotation_result.json\"");
    }

    private void returnToCurrent() {
        if (!currentFileIsMultiTable) {
            storeRevision();
        }

        annotationReviewTable.clearSelection();
        try {
            loadFile(currentFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sheetDisplayTable.setModel(currentFileTableModel);

        resizeColumnWidth(sheetDisplayTable);

        annotationReviewTableSelection = null;

        nextFileButton.setEnabled(true);
        returnToCurrentButton.setEnabled(false);
        markAsMultitableButton.setEnabled(true);
    }

    private void storeRevision() {
        SheetDisplayTableModel sheetDisplayTableModel = (SheetDisplayTableModel) sheetDisplayTable.getModel();
        if (sheetDisplayTableModel.hasUnannotatedLines()) {
            int selectCode = JOptionPane.showConfirmDialog(null,
                    "Some lines are not annotated yet. Do you still want to finish it? Click on \"Yes\" will automatically annotate this lines as empty lines");
            if (selectCode != JOptionPane.OK_OPTION) {
                return;
            }
        }
        String[] nameSplits = GeneralUtils.splitFullName(annotationReviewTableSelection);

        AnnotationResults results = new AnnotationResults(nameSplits[0], nameSplits[1], endTime - startTime);

        results.annotate(sheetDisplayTableModel);

        store.addAnnotation(results);
    }

    private void resetReviewTable() {
        DefaultTableModel tableModel = new DefaultTableModel(0, 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        annotationReviewTable = new JTable(tableModel);
        annotationReviewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        annotationReviewTable.setTableHeader(null);

        annoReviewCellTable = new JTable(tableModel);
        annoReviewCellTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        annoReviewCellTable.setTableHeader(null);

        fileReviewTable = new JTable(tableModel);
        fileReviewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileReviewTable.setTableHeader(null);
    }

    private void disableAllButtons() {
        this.submitAllResultButton.setEnabled(false);
        this.markAsMultitableButton.setEnabled(false);
        this.nextFileButton.setEnabled(false);
        this.returnToCurrentButton.setEnabled(false);
        this.copyPatternButton.setEnabled(false);
        this.pastePatternButton.setEnabled(false);
        this.loadAllFilesButton.setEnabled(false);
    }

    private void drawTableBackgroundColor(String fileName) {
        SheetDisplayTableModel sheetDisplayTableModel = (SheetDisplayTableModel) this.sheetDisplayTable.getModel();
        AnnotationResults annotationResults = this.store.getAnnotation(fileName);
        annotationResults.getAnnotationResults().forEach(result -> {
            int lineNumber = result.getLineNumber();
            String lineType = result.getType();
            sheetDisplayTableModel.setRowsBackgroundColor(lineNumber, lineNumber, ColorSolution.getColor(lineType));
        });
    }
}