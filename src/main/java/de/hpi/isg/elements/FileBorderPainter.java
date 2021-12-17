package de.hpi.isg.elements;

import de.hpi.isg.utils.ColorSolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * @author lan
 * @since 2021/1/24
 */
public class FileBorderPainter {

    private final int rowCount;

    private final int columnCount;

    private final List<List<Stack<Color>>> borderColors;

    public FileBorderPainter(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.borderColors = new ArrayList<>(this.rowCount);

        for (int i = 0; i < this.rowCount; i++) {
            borderColors.set(i, new ArrayList<>());
            for (int j = 0; j < this.columnCount; j++) {
                Stack<Color> colorAtIndex = new Stack<>();
                colorAtIndex.push(ColorSolution.DEFAULT_BACKGROUND_COLOR);
                borderColors.get(i).set(j, colorAtIndex);
            }
        }
    }

    public void addColorAtBlock(BlockIndexTuples block, Color color) {
        this.addColorAtIndices(block.flatten(), color);
    }

    public void removeColorAtBlock(BlockIndexTuples block, Color color) {
        this.removeColorAtIndices(block.flatten(), color);
    }

    public void addColorAtIndices(Collection<CellIndex> indices, Color color) {
        for (CellIndex index : indices) {
            this.borderColors.get(index.getRowIndex()).get(index.getColumnIndex()).push(color);
        }
    }

    public void removeColorAtIndices(Collection<CellIndex> indices, Color color) {
        for (CellIndex tuple : indices) {
            Stack<Color> colorStack = this.borderColors.get(tuple.getRowIndex()).get(tuple.getColumnIndex());
            if (!colorStack.peek().equals(ColorSolution.DEFAULT_BACKGROUND_COLOR)) {
                colorStack.pop();
            }
        }
    }
}
