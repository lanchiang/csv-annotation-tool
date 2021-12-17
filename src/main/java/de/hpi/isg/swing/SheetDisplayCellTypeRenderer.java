package de.hpi.isg.swing;

import de.hpi.isg.utils.ColorSolution;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * @author lan
 * @since 2020/3/17
 */
public class SheetDisplayCellTypeRenderer implements TableCellRenderer {

    private static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    public static final int OPACITY_PARAMETER = 92;

    private final int rowCount;

    private final int columnCount;

    private final List<String[]> annotations;

    private final Border NULL_BORDER = new MatteBorder(2, 2, 2, 2, ColorSolution.NULL_BORDER_COLOR);
    private final Border SELECTED_BORDER = new MatteBorder(2, 2, 2, 2, ColorSolution.OPERAND_BORDER_COLOR);
    private final Border HIGHLIGHT_BORDER = new MatteBorder(2, 2, 2, 2, ColorSolution.HIGHLIGHT_BORDER_COLOR);

    public SheetDisplayCellTypeRenderer(int rowCount, int columnCount, List<String[]> annotations) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.annotations = annotations;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            component.setBackground(Color.LIGHT_GRAY);
            return component;
        }

        SheetDisplayCellTableModel tableModel = (SheetDisplayCellTableModel) table.getModel();
        Color color = tableModel.getCellColor(row, column);
        component.setBackground(color);

//        JComponent jc = (JComponent) component;
//        if (mode == SheetDisplayCellTableModel.TableMode.CALCULATE) {
//            List<FileIndexTuple> aggregatorIndices = tableModel.getImpliedAggregatorIndices();
//            if (aggregatorIndices.contains(new FileIndexTuple(row, column))) {
//                jc.setBorder(HIGHLIGHT_BORDER);
//            } else {
//                jc.setBorder(NULL_BORDER);
//            }
//        } else if (mode == SheetDisplayCellTableModel.TableMode.SELECT) {
//            List<FileIndexTuple> selectedAggregatees = tableModel.getSelectedAggregateeBlocks().stream()
//                    .flatMap(block -> block.flatten().stream()).collect(Collectors.toList());
//            if (selectedAggregatees.contains(new FileIndexTuple(row, column))) {
//                jc.setBorder(SELECTED_BORDER);
//            } else {
//                jc.setBorder(NULL_BORDER);
//            }
//        }

        return component;
    }
}
