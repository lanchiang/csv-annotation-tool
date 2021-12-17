package de.hpi.isg.elements;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lan
 * @since 2021/1/19
 */
@Getter
public class BlockIndexTuples {

    private final CellIndex topLeftIndexTuple;

    private final CellIndex bottomRightIndexTuple;

    private final int size;

    public BlockIndexTuples(CellIndex topLeftIndexTuple, CellIndex bottomRightIndexTuple) {
        this.topLeftIndexTuple = topLeftIndexTuple;
        this.bottomRightIndexTuple = bottomRightIndexTuple;

        this.size = (bottomRightIndexTuple.getRowIndex() - topLeftIndexTuple.getRowIndex() + 1)
                * (bottomRightIndexTuple.getColumnIndex() - topLeftIndexTuple.getColumnIndex() + 1);
    }

    public static BlockIndexTuples createBlockIndexTuples(String topLeftIndexStr, String bottomRightIndexStr) {
        Pattern pattern = Pattern.compile("<(\\d+),(\\d+)>");
        Matcher matcher = pattern.matcher(topLeftIndexStr);
        int topIndex = 0, leftIndex = 0;
        if (matcher.find()) {
            topIndex = Integer.parseInt(matcher.group(1)) - 1;
            leftIndex = Integer.parseInt(matcher.group(2)) - 1;
        }
        matcher = pattern.matcher(bottomRightIndexStr);
        int bottomIndex = 0, rightIndex = 0;
        if (matcher.find()) {
            bottomIndex = Integer.parseInt(matcher.group(1)) - 1;
            rightIndex = Integer.parseInt(matcher.group(2)) - 1;
        }
        return new BlockIndexTuples(new CellIndex(topIndex, leftIndex), new CellIndex(bottomIndex, rightIndex));
    }

    public List<CellIndex> flatten() {
        List<CellIndex> cellIndices = new ArrayList<>();
        for (int i = this.topLeftIndexTuple.getRowIndex(); i <= this.bottomRightIndexTuple.getRowIndex(); i++) {
            for (int j = this.topLeftIndexTuple.getColumnIndex(); j <= this.bottomRightIndexTuple.getColumnIndex(); j++) {
                cellIndices.add(new CellIndex(i, j));
            }
        }
        return cellIndices;
    }

    public int size() {
        return this.size;
    }

    public boolean containsIndex(CellIndex tuple) {
        return this.flatten().contains(tuple);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockIndexTuples that = (BlockIndexTuples) o;
        return Objects.equals(topLeftIndexTuple, that.topLeftIndexTuple) && Objects.equals(bottomRightIndexTuple, that.bottomRightIndexTuple);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topLeftIndexTuple, bottomRightIndexTuple);
    }
}
