package de.hpi.isg.swing;

import de.hpi.isg.elements.AggregationRelation;
import de.hpi.isg.elements.BlockIndexTuples;
import de.hpi.isg.elements.FileBorderPainter;
import de.hpi.isg.elements.CellIndex;
import de.hpi.isg.utils.ColorSolution;
import lombok.Getter;
import lombok.Setter;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author lan
 * @since 2021/1/21
 */
public class CsvDisplayTableModel extends DefaultTableModel {


    public static class TableMode {
        public final static int SELECT_AGGREGATEES = 0;
        public final static int SELECT_AGGREGATORS = 1;
        public final static int VIEW = 2;
        public final static int DEFAULT = VIEW;
        public final static int DEFAULT_LAST = SELECT_AGGREGATEES;

        public static String getModeString(int tableMode) {
            String modeString = "";
            switch (tableMode) {
                case SELECT_AGGREGATEES:
                    modeString = "Select-aggregatees mode";
                    break;
                case SELECT_AGGREGATORS:
                    modeString = "Select-aggregators mode";
                    break;
                case VIEW:
                    modeString = "View mode";
                    break;
                default:
                    break;
            }
            return modeString;
        }
    }

    @Getter
    private List<Color[]> cellBackgroundColors = new ArrayList<>(getRowCount());
    @Getter
    private List<Color[]> lastCellBackgroundColors = new ArrayList<>(getRowCount());

    @Getter
    private FileBorderPainter cellBorderColors;

    @Getter
    private final List<CellIndex> impliedAggregatorIndices = new ArrayList<>();

    @Getter
    private final List<CellIndex> annotatedAggregateesIndices = new ArrayList<>();

    @Getter
//    private final List<BlockIndexTuples> selectedAggregateeBlocks = new ArrayList<>();
    private final Map<Integer, List<BlockIndexTuples>> selectedAggregateeBlocks = new HashMap<>();

    @Getter
    private final List<BlockIndexTuples> selectedAggregatorBlocks = new ArrayList<>();

    @Getter
    private Set<AggregationRelation> aggregationMapping = new HashSet<>();

    @Getter @Setter
    private int tableMode = TableMode.DEFAULT;
    @Getter @Setter
    private int lastTableMode = TableMode.SELECT_AGGREGATEES;

    @Getter
    private final String selectedFileName;

    @Getter
    private final String selectedSheetName;

    public CsvDisplayTableModel(int rowCount, int columnCount, String fileName, String sheetName) {
        super(rowCount, columnCount);
        this.selectedFileName = fileName;
        this.selectedSheetName = sheetName;
    }

    private void setCellBackgroundColor(int rowIndex, int columnIndex, Color color, boolean paintEmptyCells) {
        if (!(rowIndex < getRowCount() && rowIndex >= 0) || !(columnIndex < getColumnCount() && columnIndex >= 0)) {
            throw new RuntimeException("The given row index or column index is illegal.");
        }

        if (!paintEmptyCells) {
            // if the cell is not empty, allow it to be changed.
            if (getValueAt(rowIndex, columnIndex) == null) {
                cellBackgroundColors.get(rowIndex)[columnIndex] = ColorSolution.DEFAULT_BACKGROUND_COLOR;
            } else {
                if ("".equals(getValueAt(rowIndex, columnIndex).toString().trim())) {
                    cellBackgroundColors.get(rowIndex)[columnIndex] = ColorSolution.DEFAULT_BACKGROUND_COLOR;
                } else {
                    cellBackgroundColors.get(rowIndex)[columnIndex] = color;
                }
            }
        } else {
            cellBackgroundColors.get(rowIndex)[columnIndex] = color;
        }
    }

    /**
     *
     * @param topRowIndex
     * @param bottomRowIndex
     * @param leftColumnIndex
     * @param rightColumnIndex
     * @param color
     * @param paintEmptyCells whether treating the empty cells in the block as the color as well.
     */
    public void setBlockBackgroundColor(int topRowIndex, int bottomRowIndex, int leftColumnIndex, int rightColumnIndex, Color color,
                                        boolean paintEmptyCells) {
        for (int i = topRowIndex - 1; i < bottomRowIndex; i++) {
            for (int j = leftColumnIndex - 1; j < rightColumnIndex; j++) {
                setCellBackgroundColor(i, j, color, paintEmptyCells);
                fireTableCellUpdated(i, j);
            }
        }
    }

    /**
     * Highlight the cells that aggregate the selected aggregatees with red cell border.
     * @param satisfiedIndexTuples
     */
    public void highlightAggregatorCells(List<CellIndex> satisfiedIndexTuples) {
        lastCellBackgroundColors = new ArrayList<>(cellBackgroundColors);
        for (CellIndex indexTuple: satisfiedIndexTuples) {
            this.impliedAggregatorIndices.add(indexTuple);
            fireTableCellUpdated(indexTuple.getRowIndex(), indexTuple.getColumnIndex());
        }
    }

    /**
     * Remove the highlighted aggregation cells
     */
    public void dehighlightAggregatorCells() {
        for (CellIndex indexTuple : this.impliedAggregatorIndices) {
            fireTableCellUpdated(indexTuple.getRowIndex(), indexTuple.getColumnIndex());
        }
        this.impliedAggregatorIndices.clear();
        cellBackgroundColors = lastCellBackgroundColors;
    }

    /**
     * Highlight the cells that aggregate the selected aggregatees with red cell border.
     * @param aggregatees
     */
    public void highlightAnnotatedAggregateeCells(List<CellIndex> aggregatees) {
        lastCellBackgroundColors = new ArrayList<>(cellBackgroundColors);
        for (CellIndex indexTuple: aggregatees) {
            this.annotatedAggregateesIndices.add(indexTuple);
            fireTableCellUpdated(indexTuple.getRowIndex(), indexTuple.getColumnIndex());
        }
    }

    /**
     * Remove the highlighted aggregation cells
     */
    public void dehighlightAnnotatedAggregateeCells() {
        for (CellIndex indexTuple : this.annotatedAggregateesIndices) {
            fireTableCellUpdated(indexTuple.getRowIndex(), indexTuple.getColumnIndex());
        }
        this.annotatedAggregateesIndices.clear();
        cellBackgroundColors = lastCellBackgroundColors;
    }

    /**
     * Remove the highlighted aggregatee selection.
     */
    public void removeAggregateeSelection() {
        for (List<BlockIndexTuples> blockIndexTuples : this.selectedAggregateeBlocks.values()) {
            for (BlockIndexTuples blockIndexTuple : blockIndexTuples) {
                for (CellIndex cellIndex : blockIndexTuple.flatten()) {
                    fireTableCellUpdated(cellIndex.getRowIndex(), cellIndex.getColumnIndex());
                }
            }
        }
        this.selectedAggregateeBlocks.clear();
    }

    /**
     * Remove the highlighted aggregatee selection.
     */
    public void removeAggregatorSelection() {
        for (BlockIndexTuples blockIndexTuples : this.selectedAggregatorBlocks) {
            for (CellIndex cellIndex : blockIndexTuples.flatten()) {
                fireTableCellUpdated(cellIndex.getRowIndex(), cellIndex.getColumnIndex());
            }
        }
        this.selectedAggregatorBlocks.clear();
    }

    /**
     * Add the selected block into the aggregatee set. This means, render the selected block with blue border, and insert the block
     * into the selectedAggregateeBlocks.
     * @param block
     */
    public void selectAggregateeBlock(int operandNumber, BlockIndexTuples block) {
//         this.selectedAggregateeBlocks.add(block);
        if (!this.selectedAggregateeBlocks.containsKey(operandNumber)) {
            this.selectedAggregateeBlocks.put(operandNumber, new ArrayList<>());
        }
        this.selectedAggregateeBlocks.get(operandNumber).add(block);

        for (CellIndex tuple : block.flatten()) {
            fireTableCellUpdated(tuple.getRowIndex(), tuple.getColumnIndex());
        }
    }

    /**
     * Add the selected block into the aggregator set. This means, render the selected block with blue border, and insert the block
     * into the selectedAggregatorBlocks.
     * @param block
     */
    public void selectAggregatorBlock(BlockIndexTuples block) {
        for (CellIndex tuple : block.flatten()) {
            fireTableCellUpdated(tuple.getRowIndex(), tuple.getColumnIndex());
        }
        this.selectedAggregatorBlocks.add(block);
    }

    public void updateAggregationMapping(Set<AggregationRelation> that) {
        for (AggregationRelation relation : that) {
            cellBackgroundColors.get(relation.getAggregator().getRowIndex())[relation.getAggregator().getColumnIndex()] =
                    ColorSolution.getAggregatorColor(relation.getOperator());
            fireTableCellUpdated(relation.getAggregator().getRowIndex(), relation.getAggregator().getColumnIndex());
        }
        this.aggregationMapping.addAll(that);
    }

    public void resetCurrentAnnotationTask() {
        this.removeAggregateeSelection();
        this.removeAggregatorSelection();
        this.dehighlightAggregatorCells();
        this.dehighlightAnnotatedAggregateeCells();
    }

    public Color getCellColor(int rowIndex, int columnIndex) {
        if (!(rowIndex < getRowCount() && rowIndex >= 0) || !(columnIndex < getColumnCount() && columnIndex >= 0)) {
            throw new RuntimeException("The given row index or column index is illegal.");
        }
        return cellBackgroundColors.get(rowIndex)[columnIndex];
    }

    @Override
    public void insertRow(int row, Object[] rowData) {
        super.insertRow(row, rowData);
        cellBackgroundColors.add(row, new Color[getColumnCount()]);
    }

    public void insertRows(List<String[]> data) {
        for (int i = 0; i < data.size(); i++) {
            this.insertRow(i, data.get(i));
            for (int j = 0; j < data.get(i).length; j++) {
                cellBackgroundColors.get(i)[j] = ColorSolution.DEFAULT_BACKGROUND_COLOR;
            }
        }
        lastCellBackgroundColors = new ArrayList<>(cellBackgroundColors);
//        cellBorderColors = new FileBorderPainter(data.size(), data.get(0).length);
    }

    public void loadAnnotations(Set<AggregationRelation> relations) {
        this.aggregationMapping = relations;
        for (AggregationRelation relation : this.aggregationMapping) {
            int rowIndex = relation.getAggregator().getRowIndex();
            int columnIndex = relation.getAggregator().getColumnIndex();
            this.cellBackgroundColors.get(rowIndex)[columnIndex] = ColorSolution.getAggregatorColor(relation.getOperator());
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }


}
