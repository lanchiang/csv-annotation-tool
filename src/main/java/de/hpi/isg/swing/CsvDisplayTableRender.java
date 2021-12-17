package de.hpi.isg.swing;

import de.hpi.isg.elements.BlockIndexTuples;
import de.hpi.isg.elements.CellIndex;
import de.hpi.isg.utils.ColorSolution;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lan
 * @since 2021/1/21
 */
public class CsvDisplayTableRender implements TableCellRenderer, ListCellRenderer {

    private static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    public static final int OPACITY_PARAMETER = 16;

    private static int count = 0;

    private final Border NULL_BORDER = new MatteBorder(2, 2, 2, 2, ColorSolution.NULL_BORDER_COLOR);
    private final Border ANNOTATED_AGGREGATOR_BORDER = new MatteBorder(2, 2, 2, 2, ColorSolution.ANNOTATED_AGGREGATEE_COLOR);
    private final Border SELECT_AGGREGATOR_BORDER = new MatteBorder(2, 2, 2, 2, ColorSolution.OPERAND_BORDER_COLOR);
    private final Border SELECT_AGGREGATEE_BORDER = new MatteBorder(2, 2, 2, 2, ColorSolution.SELECT_AGGREGATEE_COLOR);
    private final Border HIGHLIGHT_BORDER = new MatteBorder(2, 2, 2, 2, ColorSolution.HIGHLIGHT_BORDER_COLOR);

    public CsvDisplayTableRender() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        System.out.println(count++ + "\t" + row + "\t" + column);
        Component component = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            component.setBackground(Color.LIGHT_GRAY);
//            table.getTableHeader().setBackground(Color.CYAN);
            return component;
        }

        CsvDisplayTableModel tableModel = (CsvDisplayTableModel) table.getModel();
        Color color = tableModel.getCellColor(row, column);
        component.setBackground(color);

        JComponent jc = (JComponent) component;
        List<CellIndex> annotatedAggregatees = tableModel.getAnnotatedAggregateesIndices();
        if (annotatedAggregatees.contains(new CellIndex(row, column))) {
            jc.setBorder(ANNOTATED_AGGREGATOR_BORDER);
        }
        List<CellIndex> aggregatees = tableModel.getSelectedAggregateeBlocks().values().stream().flatMap(List::stream).map(BlockIndexTuples::flatten).flatMap(List::stream).collect(Collectors.toList());
//        List<CellIndex> aggregatees = tableModel.getSelectedAggregateeBlocks().values().stream().flatMap(b -> b.flatten().stream()).collect(Collectors.toList());
        if (aggregatees.contains(new CellIndex(row, column))) {
            jc.setBorder(SELECT_AGGREGATEE_BORDER);
        }
        List<CellIndex> impliedAggregators = tableModel.getImpliedAggregatorIndices();
        if (impliedAggregators.contains(new CellIndex(row, column))) {
            jc.setBorder(HIGHLIGHT_BORDER);
        }
        List<CellIndex> aggregators = tableModel.getSelectedAggregatorBlocks().stream()
                .flatMap(block -> block.flatten().stream()).collect(Collectors.toList());
        if (aggregators.contains(new CellIndex(row, column))) {
            jc.setBorder(SELECT_AGGREGATOR_BORDER);
        }

        return component;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return null;
    }
}
