package de.hpi.isg.elements.aggregations;

import com.google.common.collect.Streams;
import de.hpi.isg.elements.CellIndex;

import javax.swing.table.TableModel;
import java.util.Collection;

/**
 * @author lan
 * @since 2021/1/30
 */
public class SubtractionAggregationDetector extends AggregationDetector {

    public SubtractionAggregationDetector(TableModel tableModel, double error, String numberFormat) {
        super(tableModel, error, numberFormat);
    }

    public SubtractionAggregationDetector(TableModel tableModel, double error, int errorBoundMethod, String numberFormat) {
        super(tableModel, error, errorBoundMethod, numberFormat);
    }

    @Override
    public Collection<CellIndex> getSatisfiedCellIndices(Collection<CellIndex> aggregatorCandidates, Collection<CellIndex>... selectedAggregatees) {
        int validAggregateeBlockNumber = 2;
        if (selectedAggregatees.length != validAggregateeBlockNumber) {
            throw new IllegalArgumentException(String.format("More or less than %d for %s", validAggregateeBlockNumber, this.getClass().getName()));
        }
        Collection<CellIndex> opOneAggregatees = selectedAggregatees[0];
        Collection<CellIndex> opTwoAggregatees = selectedAggregatees[1];
        if (opOneAggregatees.size() != opTwoAggregatees.size() || opOneAggregatees.size() != aggregatorCandidates.size()) {
            throw new IllegalArgumentException("Operands are not aligned.");
        }
//        Streams.zip(opOneAggregatees.stream(), opTwoAggregatees.stream(), (opOne, opTwo) -> )
        return null;
    }
}
