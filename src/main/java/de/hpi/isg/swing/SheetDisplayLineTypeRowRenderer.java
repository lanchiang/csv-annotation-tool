package de.hpi.isg.swing;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author Lan Jiang
 * @since 9/13/19
 */
public class SheetDisplayLineTypeRowRenderer implements TableCellRenderer {

    private static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    public static final int OPACITY_PARAMETER = 92;

    private final int rowCount;

    private final int columnCount;

    public SheetDisplayLineTypeRowRenderer(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            component.setBackground(Color.LIGHT_GRAY);
            return component;
        }

        SheetDisplayTableModel tableModel = (SheetDisplayTableModel) table.getModel();
        component.setBackground(tableModel.getRowColor(row));
        return component;
    }
}