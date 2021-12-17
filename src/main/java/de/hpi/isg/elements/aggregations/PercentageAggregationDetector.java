package de.hpi.isg.elements.aggregations;

import de.hpi.isg.elements.CellIndex;

import javax.swing.table.TableModel;
import java.util.Collection;

/**
 * @author lan
 * @since 2021/1/30
 */
public class PercentageAggregationDetector extends AggregationDetector {

    public PercentageAggregationDetector(TableModel tableModel, double error, String numberFormat) {
        super(tableModel, error, numberFormat);
    }

    public PercentageAggregationDetector(TableModel tableModel, double error, int errorBoundMethod, String numberFormat) {
        super(tableModel, error, errorBoundMethod, numberFormat);
    }

    @Override
    public Collection<CellIndex> getSatisfiedCellIndices(Collection<CellIndex> aggregatorCandidates, Collection<CellIndex>... selectedAggregatees) {
        return null;
    }
}
