package de.hpi.isg.swing;

import de.hpi.isg.utils.ColorSolution;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Lan Jiang
 * @since 9/15/19
 */
public class SheetDisplayTableModel extends DefaultTableModel {

    private List<Color> rowColors = new ArrayList<>(getRowCount());

    private List<Integer> emptyLineIndices = new LinkedList<>();

    public SheetDisplayTableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    private void setRowBackgroundColor(int rowIndex, Color color) {
        rowColors.set(rowIndex, color);
    }

    public void setRowsBackgroundColor(int startRowIndex, int endRowIndex, Color color) {
        for (int i = startRowIndex - 1; i < endRowIndex; i++) {
            setRowBackgroundColor(i, color);
        }
        fireTableDataChanged();
    }

    public Color getRowColor(int rowIndex) {
        return rowColors.get(rowIndex);
    }

    public void setRowColor(int rowIndex, Color color) {
        rowColors.set(rowIndex, color);
    }

    @Override
    public void insertRow(int row, Object[] rowData) {
        super.insertRow(row, rowData);
        rowColors.add(ColorSolution.DEFAULT_BACKGROUND_COLOR);
        if (Arrays.stream(rowData).map(Object::toString).allMatch(""::equals)) {
            emptyLineIndices.add(row);
        }
    }

    public void insertRows(List<String[]> dataEntries) {
        for (int i = 0; i < dataEntries.size(); i++) {
            this.insertRow(i, dataEntries.get(i));
        }
        emptyLineIndices.forEach(rowIndex -> rowColors.set(rowIndex, ColorSolution.EMPTY_LINE_BACKGROUND_COLOR));
    }

    public void setEmptyRowBackground(int startRowIndex, int endRowIndex) {
        emptyLineIndices.forEach(rowIndex -> {
            if (startRowIndex <= (rowIndex + 1) && endRowIndex >= (rowIndex + 1)) {
                rowColors.set(rowIndex, ColorSolution.EMPTY_LINE_BACKGROUND_COLOR);
            }
        });
        fireTableDataChanged();
    }

    public boolean hasUnannotatedLines() {
        return rowColors.stream().anyMatch(color -> color.equals(ColorSolution.DEFAULT_BACKGROUND_COLOR));
    }

    @Override
    public String getColumnName(int column) {
        return String.valueOf(column + 1);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
