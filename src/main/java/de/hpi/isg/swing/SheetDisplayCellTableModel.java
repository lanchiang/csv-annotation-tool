package de.hpi.isg.swing;

import de.hpi.isg.elements.BlockIndexTuples;
import de.hpi.isg.elements.CellIndex;
import de.hpi.isg.utils.ColorSolution;
import lombok.Getter;
import org.json.simple.JSONArray;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lan
 * @since 2020/3/17
 */
public class SheetDisplayCellTableModel extends DefaultTableModel {

    /**
     * The current mode of the Table, CALCULATE means the aggregations are calculated and therefore previewed.
     * SELECT means multiple lines are to be selected.
     */
    public static class TableMode {
        public final static int CALCULATE = 0;
        public final static int SELECT = 1;
    }

    @Getter
    private final List<Color[]> tableCellBackgroundColors = new ArrayList<>(getRowCount());

    /**
     * Indices of cells that can be computed as aggregators with the selected data.
     */
    @Getter
    private final List<CellIndex> previewAggregatorIndices = new ArrayList<>();

    /**
     * Store the aggregators marked by the users in GUI. Key is an aggregator index, value is the aggregatees of this aggregator.
     */
    @Getter
    private final Map<CellIndex, List<List<CellIndex>>> aggregators = new HashMap<>();

    @Getter
    private final List<CellIndex> accumulativeSelectedCellIndices = new ArrayList<>();

    @Getter
    private final List<CellIndex> selectedAggregators = new ArrayList<>();

    @Getter
    private final List<BlockIndexTuples> selectedAggregatees = new ArrayList<>();

    @Getter
    private int mode = TableMode.CALCULATE;

    @Getter
    private final String selectedFileName;

    @Getter
    private final String selectedSheetName;

    public SheetDisplayCellTableModel(int rowCount, int columnCount, String selectedFileName, String selectedSheetName) {
        super(rowCount, columnCount);
        this.selectedFileName = selectedFileName;
        this.selectedSheetName = selectedSheetName;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    private void setCellBackgroundColor(int rowIndex, int columnIndex, Color color, boolean paintEmptyCells) {
        setCellColor(rowIndex, columnIndex, color, paintEmptyCells);
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
            }
        }
        fireTableDataChanged();
    }

    public void highlightAggregatorCells(List<CellIndex> satisfiedIndexTuples) {
        for (CellIndex indexTuple: satisfiedIndexTuples) {
            this.previewAggregatorIndices.add(indexTuple);
            fireTableCellUpdated(indexTuple.getRowIndex(), indexTuple.getColumnIndex());
        }
    }

    public void removeAggregatorEmphasis() {
        for (CellIndex indexTuple : this.previewAggregatorIndices) {
            fireTableCellUpdated(indexTuple.getRowIndex(), indexTuple.getColumnIndex());
        }
        this.previewAggregatorIndices.clear();
    }

    public void clearAllSelection() {
        for (CellIndex tuple : this.accumulativeSelectedCellIndices) {
            fireTableCellUpdated(tuple.getRowIndex(), tuple.getColumnIndex());
        }
        this.accumulativeSelectedCellIndices.clear();
    }

    public void updateAccSelectedCells(BlockIndexTuples blockIndex) {
        for (int i = blockIndex.getTopLeftIndexTuple().getRowIndex(); i <= blockIndex.getBottomRightIndexTuple().getRowIndex(); i++) {
            for (int j = blockIndex.getTopLeftIndexTuple().getColumnIndex(); j <= blockIndex.getBottomRightIndexTuple().getColumnIndex(); j++) {
                this.accumulativeSelectedCellIndices.add(new CellIndex(i, j));
                fireTableCellUpdated(i, j);
            }
        }
    }

    public Color getCellColor(int rowIndex, int columnIndex) {
        if (!(rowIndex < getRowCount() && rowIndex >= 0) || !(columnIndex < getColumnCount() && columnIndex >= 0)) {
            throw new RuntimeException("The given row index or column index is illegal.");
        }
        return tableCellBackgroundColors.get(rowIndex)[columnIndex];
    }

    private void setCellColor(int rowIndex, int columnIndex, Color color, boolean paintEmptyCells) {
        if (!(rowIndex < getRowCount() && rowIndex >= 0) || !(columnIndex < getColumnCount() && columnIndex >= 0)) {
            throw new RuntimeException("The given row index or column index is illegal.");
        }

        if (!paintEmptyCells) {
            // if the cell is not empty, allow it to be changed.
            if (getValueAt(rowIndex, columnIndex) == null) {
                tableCellBackgroundColors.get(rowIndex)[columnIndex] = ColorSolution.DEFAULT_BACKGROUND_COLOR;
            } else {
                if (getValueAt(rowIndex, columnIndex).toString().trim().equals("")) {
                    tableCellBackgroundColors.get(rowIndex)[columnIndex] = ColorSolution.DEFAULT_BACKGROUND_COLOR;
                } else {
                    tableCellBackgroundColors.get(rowIndex)[columnIndex] = color;
                }
            }
        } else {
            tableCellBackgroundColors.get(rowIndex)[columnIndex] = color;
        }

    }

    @Override
    public void insertRow(int row, Object[] rowData) {
        super.insertRow(row, rowData);
        tableCellBackgroundColors.add(row, new Color[getColumnCount()]);
    }

    public void insertRows(List<String[]> data, List<String[]> annotations) {
        assert data.size() == annotations.size();
        for (int i = 0; i < data.size(); i++) {
            this.insertRow(i, data.get(i));
            for (int j = 0; j < data.get(i).length; j++) {
                tableCellBackgroundColors.get(i)[j] = ColorSolution.getColorRNN(annotations.get(i)[j]);
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return String.valueOf(column + 1);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void addToAnnotations(List<CellIndex> aggregators, List<BlockIndexTuples> aggregatees) {

        long numLeftIndices = aggregatees.stream().map(BlockIndexTuples::getTopLeftIndexTuple).map(CellIndex::getColumnIndex).distinct().count();
        long numRightIndices = aggregatees.stream().map(BlockIndexTuples::getBottomRightIndexTuple).map(CellIndex::getColumnIndex).distinct().count();
        long aggregateeSliceLength = aggregatees.stream()
                .map(bit -> bit.getBottomRightIndexTuple().getColumnIndex() - bit.getTopLeftIndexTuple().getColumnIndex())
                .distinct().count();
        long aggregatorRowSpan = aggregators.stream().map(CellIndex::getRowIndex).distinct().count();

        if (numLeftIndices == 1 && numRightIndices == 1 && aggregatorRowSpan == 1) {
            // blocks are vertically aligned.
            List<Integer> selectedRowsList = new ArrayList<>();
            for (BlockIndexTuples block : aggregatees) {
                for (int i = block.getTopLeftIndexTuple().getRowIndex(); i <= block.getBottomRightIndexTuple().getRowIndex(); i++) {
                    selectedRowsList.add(i);
                }
            }

            for (CellIndex aggregator : aggregators) {
                List<CellIndex> aggregateeIndices = selectedRowsList.stream()
                        .map(rowIndex -> new CellIndex(rowIndex, aggregator.getColumnIndex())).collect(Collectors.toList());
                this.aggregators.putIfAbsent(aggregator, new ArrayList<>());
                this.aggregators.get(aggregator).add(aggregateeIndices);
            }
        }

        long numTopIndices = aggregatees.stream().map(BlockIndexTuples::getTopLeftIndexTuple).map(CellIndex::getRowIndex).distinct().count();
        long numBottomIndices = aggregatees.stream().map(BlockIndexTuples::getBottomRightIndexTuple).map(CellIndex::getRowIndex).distinct().count();
        long aggregatorColumnSpan = aggregators.stream().map(CellIndex::getColumnIndex).distinct().count();

        if (numTopIndices == 1 && numBottomIndices == 1 && aggregatorColumnSpan == 1) {
            // blocks are horizontally aligned.
            List<Integer> selectedColumnsList = new ArrayList<>();
            for (BlockIndexTuples block : aggregatees) {
                for (int i = block.getTopLeftIndexTuple().getColumnIndex(); i <= block.getBottomRightIndexTuple().getColumnIndex(); i++) {
                    selectedColumnsList.add(i);
                }
            }

            for (CellIndex aggregator : aggregators) {
                List<CellIndex> aggregateeIndices = selectedColumnsList.stream()
                        .map(columnIndex -> new CellIndex(aggregator.getRowIndex(), columnIndex)).collect(Collectors.toList());
                this.aggregators.putIfAbsent(aggregator, new ArrayList<>());
                this.aggregators.get(aggregator).add(aggregateeIndices);
            }
        }
    }

    public JSONArray createJsonAnnotation() {
        JSONArray jsonArray = new JSONArray();
        for (Color[] lineColors : tableCellBackgroundColors) {
            JSONArray lineJsonArray = new JSONArray();
            List<String> cellTypes = Arrays.stream(lineColors).map(ColorSolution::getLineTypeRNN).collect(Collectors.toList());
            lineJsonArray.addAll(cellTypes);
            jsonArray.add(lineJsonArray);
        }
        return jsonArray;
    }
}