package de.hpi.isg.modules;

import de.hpi.isg.elements.AnnotateCellPageComponents;
import de.hpi.isg.json.JsonSheetEntry;
import de.hpi.isg.elements.PageComponents;
import de.hpi.isg.gui.MainFrame;
import de.hpi.isg.io.FileWriter;
import de.hpi.isg.swing.SheetDisplayCellTableModel;
import de.hpi.isg.swing.SheetDisplayCellTypeRenderer;
import de.hpi.isg.utils.ColorSolution;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lan
 * @since 2021/1/15
 */
public class AnnotateCellClassModule extends Module {

    private static AnnotateCellClassModule instance;

    private AnnotateCellPageComponents pageComponents;

    private AnnotateCellClassModule() {}

    public static Module getInstance(PageComponents pageComponents) {
        if (instance == null) {
            instance = new AnnotateCellClassModule();
            instance.initializePageComponents(pageComponents);
        }
        return instance;
    }

    @Override
    public void initializePageComponents(PageComponents pageComponents) {
        if (pageComponents instanceof AnnotateCellPageComponents) {
            this.pageComponents = (AnnotateCellPageComponents) pageComponents;
        } else {
            throw new RuntimeException("Internal error: page components do not fit to this page.");
        }
    }

    @Override
    public void renderFile(ListSelectionModel selectionModel) {
        this.pageComponents.getSelectedCellValue().setText("n/a");

        int selectedIndex = selectionModel.getMinSelectionIndex();

        DefaultTableModel defaultTableModel = (DefaultTableModel) this.pageComponents.getAnnoReviewCellTable().getModel();
        String fileSheetName = (String) defaultTableModel.getValueAt(selectedIndex, 0); // get the selectedCellValue from the first and the only column.

        String[] splits = fileSheetName.split("@");
        String fileName = splits[0];
        String sheetName = splits[1].split(".csv")[0];

        Optional<JsonSheetEntry> selectedJsonSheetEntryOpt = this.loadedJsonSheetEntries.stream().filter(entry ->
                fileName.equals(entry.getFileName()) && sheetName.equals(entry.getTableId())).findFirst();
        if (!selectedJsonSheetEntryOpt.isPresent()) {
            throw new RuntimeException("Json object of this index cannot be found.");
        }
        JsonSheetEntry selectedJsonSheetEntry = selectedJsonSheetEntryOpt.get();
        int numOfRows = selectedJsonSheetEntry.getNumOfRows();
        int numOfColumns = selectedJsonSheetEntry.getNumOfColumns();

        this.pageComponents.getNumOfLinesCell().setText(String.valueOf(numOfRows));
        this.pageComponents.getNumOfColumnsCell().setText(String.valueOf(numOfColumns));

        JSONArray tableValueArray = selectedJsonSheetEntry.getTable_array();
        JSONArray annotationArray = selectedJsonSheetEntry.getAnnotations();

        List<String[]> tableValues = new ArrayList<>();
        for (Object o : tableValueArray) {
            JSONArray lineJsonArray = (JSONArray) o;
            String[] lineArray = new String[lineJsonArray.size()];
            for (int j = 0; j < lineArray.length; j++) {
                lineArray[j] = lineJsonArray.get(j).toString();
            }
            tableValues.add(lineArray);
        }

        List<String[]> annotations = new ArrayList<>();
        for (Object o : annotationArray) {
            JSONArray lineJsonArray = (JSONArray) o;
            String[] lineArray = new String[lineJsonArray.size()];
            for (int j = 0; j < lineArray.length; j++) {
                if (lineJsonArray.get(j) != null) {
                    lineArray[j] = lineJsonArray.get(j).toString();
                } else {
                    lineArray[j] = null;
                }
            }
            annotations.add(lineArray);
        }

        SheetDisplayCellTableModel tableModel = new SheetDisplayCellTableModel(0, numOfColumns, fileName, sheetName);
        tableModel.insertRows(tableValues, annotations);

        this.pageComponents.getSheetDisplayCellTable().setModel(tableModel);

        this.pageComponents.getSheetDisplayCellTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        this.pageComponents.getSheetDisplayCellTable().setColumnSelectionAllowed(true);
        this.pageComponents.getSheetDisplayCellTable().setRowSelectionAllowed(true);

        this.pageComponents.getSheetDisplayCellTable().setDefaultRenderer(Object.class, new SheetDisplayCellTypeRenderer(numOfRows, numOfColumns, annotations));

        MainFrame.resizeColumnWidth(this.pageComponents.getSheetDisplayCellTable());

        this.pageComponents.getSheetDisplayCellPane().setBorder(BorderFactory.createTitledBorder(fileSheetName));
    }

    @Override
    public void mouseOperationOnFileDisplayTable(MouseEvent e) {
        ListSelectionModel sheetDisplayCellTableSelectionModel = this.pageComponents.getSheetDisplayCellTable().getSelectionModel();
        if (!sheetDisplayCellTableSelectionModel.isSelectionEmpty()) {
            int[] selectedRows = this.pageComponents.getSheetDisplayCellTable().getSelectedRows();
            int topIndex = selectedRows[0] + 1;
            int bottomIndex = selectedRows[selectedRows.length - 1] + 1;
            int[] selectedColumns = this.pageComponents.getSheetDisplayCellTable().getSelectedColumns();
            int leftIndex = selectedColumns[0] + 1;
            int rightIndex = selectedColumns[selectedColumns.length - 1] + 1;

            pageComponents.getSelectedCellValue().setText(
                    this.pageComponents.getSheetDisplayCellTable().getValueAt(selectedRows[0], selectedColumns[0]).toString());

            pageComponents.getTopLeftCellText().setText(topIndex + "," + leftIndex);
            pageComponents.getBottomRightCellText().setText(bottomIndex + "," + rightIndex);
        }
    }

    @Override
    public void keyOperationOnFileDisplayTable(KeyEvent e) {
        if (e.isControlDown() || e.isMetaDown() || e.getKeyCode() == KeyEvent.VK_SHIFT) {
            return;
        }

        // if shift is pressed down, treat all cells in the block as the indicated cell type.
        boolean isPaintEmptyCells = e.isShiftDown();

        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            int selectedRowIndex = pageComponents.getSheetDisplayCellTable().getSelectionModel().getLeadSelectionIndex();
            int selectedColumnIndex = pageComponents.getSheetDisplayCellTable().getColumnModel().getSelectionModel().getLeadSelectionIndex();
            pageComponents.getSelectedCellValue().setText(pageComponents.getSheetDisplayCellTable().getValueAt(selectedRowIndex, selectedColumnIndex).toString());
            pageComponents.getTopLeftCellText().setText((selectedRowIndex + 1) + "," + (selectedColumnIndex + 1));
            pageComponents.getBottomRightCellText().setText((selectedRowIndex + 1) + "," + (selectedColumnIndex + 1));
            return;
        }

        char pressedDownKeyChar = e.getKeyChar();
        Color selectedColor;
        switch (pressedDownKeyChar) {
            case 'M':
            case 'm': {
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
            default:
                return;
        }
        SheetDisplayCellTableModel tableModel = (SheetDisplayCellTableModel) pageComponents.getSheetDisplayCellTable().getModel();
        String[] topLeftCellIndices = pageComponents.getTopLeftCellText().getText().split(",");
        String[] bottomRightCellIndices = pageComponents.getBottomRightCellText().getText().split(",");
        int topCellIndex = Integer.parseInt(topLeftCellIndices[0]);
        int leftCellIndex = Integer.parseInt(topLeftCellIndices[1]);
        int bottomCellIndex = Integer.parseInt(bottomRightCellIndices[0]);
        int rightCellIndex = Integer.parseInt(bottomRightCellIndices[1]);
        tableModel.setBlockBackgroundColor(topCellIndex, bottomCellIndex, leftCellIndex, rightCellIndex, selectedColor, isPaintEmptyCells);

        JSONArray annotationJsonArray = tableModel.createJsonAnnotation();
        Optional<JsonSheetEntry> selectedJsonSheetEntryOpt = loadedJsonSheetEntries.stream()
                .filter(entry -> tableModel.getSelectedFileName().equals(entry.getFileName())
                        && tableModel.getSelectedSheetName().equals(entry.getTableId()))
                .findFirst();
        if (!selectedJsonSheetEntryOpt.isPresent()) {
            throw new RuntimeException("The selected index does not exist.");
        }
        JsonSheetEntry selectedJsonSheetEntry = selectedJsonSheetEntryOpt.get();
        selectedJsonSheetEntry.setAnnotations(annotationJsonArray);

        if (bottomCellIndex < tableModel.getRowCount()) {
            pageComponents.getSheetDisplayCellTable().requestFocus();
            pageComponents.getSheetDisplayCellTable().setRowSelectionInterval(bottomCellIndex, bottomCellIndex);
            pageComponents.getSheetDisplayCellTable().setColumnSelectionInterval(leftCellIndex - 1, leftCellIndex - 1);
            pageComponents.getSelectedCellValue().setText(pageComponents.getSheetDisplayCellTable().getValueAt(bottomCellIndex, leftCellIndex - 1).toString());
            bottomCellIndex += 1;
        } else {
            if (rightCellIndex < tableModel.getColumnCount()) {
                pageComponents.getSheetDisplayCellTable().requestFocus();
                pageComponents.getSheetDisplayCellTable().setRowSelectionInterval(bottomCellIndex - 1, bottomCellIndex - 1);
                pageComponents.getSheetDisplayCellTable().setColumnSelectionInterval(rightCellIndex, rightCellIndex);
                pageComponents.getSelectedCellValue().setText(pageComponents.getSheetDisplayCellTable().getValueAt(bottomCellIndex - 1, rightCellIndex).toString());
                rightCellIndex += 1;
            }
        }

        pageComponents.getTopLeftCellText().setText(bottomCellIndex + "," + rightCellIndex);
        pageComponents.getBottomRightCellText().setText(bottomCellIndex + "," + rightCellIndex);
    }

    @Override
    public void storeAnnotationResults() {
        List<JSONObject> cellAnnotationResults =
                loadedJsonSheetEntries.stream()
                        .map(entry -> {
                            JSONObject obj = new JSONObject();
                            obj.put("url", entry.getUrl());
                            obj.put("table_id", entry.getTableId());
                            obj.put("file_name", entry.getFileName());
                            obj.put("dictionary", entry.getDictionary());
                            obj.put("table_array", entry.getTable_array());
                            obj.put("feature_array", entry.getFeature_array());
                            obj.put("feature_names", entry.getFeature_names());
                            obj.put("annotations", entry.getAnnotations());
                            obj.put("num_rows", entry.getNumOfRows());
                            obj.put("num_cols", entry.getNumOfColumns());
                            obj.put("tok_tarr", entry.getToken_tarr());
                            obj.put("tok_tarr_reg", entry.getToken_tarr_reg());
                            return obj;
                        })
                        .collect(Collectors.toList());

        //Write JSON file
        FileWriter.writeJSONObjectToDisc(cellAnnotationResults, super.outputPath);
    }
}
