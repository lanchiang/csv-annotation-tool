package de.hpi.isg.elements.aggregations;

import de.hpi.isg.elements.CellIndex;
import de.hpi.isg.utils.NumberUtils;
import lombok.Getter;

import javax.swing.table.TableModel;
import java.util.Collection;

/**
 * The abstract class for aggregation functions.
 *
 * @author lan
 * @since 2021/1/30
 */
abstract public class AggregationDetector {

    /**
     * Allowed error level.
     */
    @Getter
    private final double error;

    @Getter
    private final int errorBoundMethod;

    @Getter
    private final String numberFormat;

    /**
     * The table model that stores the values of all cells.
     */
    @Getter
    private final TableModel tableModel;

    protected AggregationDetector(TableModel tableModel, double error, String numberFormat) {
        this(tableModel, error, NumberUtils.ErrorBoundMethod.ABSOLUTE_BOUND, numberFormat);
    }

    protected AggregationDetector(TableModel tableModel, double error, int errorBoundMethod, String numberFormat) {
        this.error = error;
        this.tableModel = tableModel;
        this.errorBoundMethod = errorBoundMethod;
        this.numberFormat = numberFormat;
    }

    abstract public Collection<CellIndex> getSatisfiedCellIndices(Collection<CellIndex> aggregatorCandidates, Collection<CellIndex>... selectedAggregatees);
}
