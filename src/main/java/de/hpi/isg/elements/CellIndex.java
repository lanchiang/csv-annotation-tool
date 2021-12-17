package de.hpi.isg.elements;

import lombok.Getter;

import java.util.Objects;

/**
 * This class represents the (row, column) index of a tabular structure.
 *
 * @author lan
 * @since 2021/1/18
 */
public class CellIndex {

    @Getter
    private final int rowIndex;

    @Getter
    private final int columnIndex;

    public CellIndex(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellIndex that = (CellIndex) o;
        return Objects.equals(rowIndex, that.rowIndex) && Objects.equals(columnIndex, that.columnIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIndex, columnIndex);
    }
}
