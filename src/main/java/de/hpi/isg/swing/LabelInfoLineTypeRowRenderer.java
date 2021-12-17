package de.hpi.isg.swing;

import de.hpi.isg.utils.ColorSolution;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author Lan Jiang
 * @since 9/15/19
 */
public class LabelInfoLineTypeRowRenderer implements TableCellRenderer {

    private static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            component.setBackground(Color.BLUE);
            return component;
        }
        if (column == 2) {
            String lineType = table.getModel().getValueAt(row, column).toString();
            switch (lineType) {
                case "Preamble (P)": {
                    component.setBackground(ColorSolution.PREAMBLE_BACKGROUND_COLOR);
                    return component;
                }
                case "HEADER (H)": {
                    component.setBackground(ColorSolution.HEADER_BACKGROUND_COLOR);
                    return component;
                }
                default: component.setBackground(ColorSolution.DEFAULT_BACKGROUND_COLOR);
            }
        }
        return component;
    }
}
