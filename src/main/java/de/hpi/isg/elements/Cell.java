package de.hpi.isg.elements;

import de.hpi.isg.utils.NumberUtils;
import lombok.Getter;

import java.util.Objects;

/**
 * Logical entity for a cell in a tabular structure.
 *
 * @author lan
 * @since 2021/1/31
 */
public class Cell {

    @Getter
    private final String value;

    @Getter
    private final CellIndex index;

    public Cell(String value, CellIndex index) {
        this.value = Objects.toString(value, "");
        this.index = index;
    }

    public Double getCellValueAsNumber(String numberFormat) {
        return NumberUtils.normalizeNumber(value, numberFormat);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return Objects.equals(value, cell.value) && Objects.equals(index, cell.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, index);
    }
}
