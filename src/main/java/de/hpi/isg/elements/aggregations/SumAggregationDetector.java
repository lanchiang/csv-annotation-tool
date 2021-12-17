package de.hpi.isg.elements.aggregations;

import de.hpi.isg.elements.CellIndex;
import de.hpi.isg.utils.NumberUtils;

import javax.swing.table.TableModel;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lan
 * @since 2021/1/30
 */
public class SumAggregationDetector extends AggregationDetector {

    public SumAggregationDetector(TableModel tableModel, double error, String numberFormat) {
        super(tableModel, error, numberFormat);
    }

    public SumAggregationDetector(TableModel tableModel, double error, int errorBoundMethod, String numberFormat) {
        super(tableModel, error, errorBoundMethod, numberFormat);
    }

    @Override
    public Collection<CellIndex> getSatisfiedCellIndices(Collection<CellIndex> aggregatorCandidates, Collection<CellIndex>... selectedAggregatees) {
        if (selectedAggregatees.length != 1) {
            throw new IllegalArgumentException("Too many selected aggregatee blocks for " + this.getClass().getName());
        }
        double expectedSum = selectedAggregatees[0].stream()
                .map(aggregatee -> NumberUtils.normalizeNumber(
                        Objects.toString(getTableModel().getValueAt(aggregatee.getRowIndex(), aggregatee.getColumnIndex()), ""), this.getNumberFormat()))
                .filter(Objects::nonNull).mapToDouble(Double::doubleValue).sum();

        return aggregatorCandidates.stream().filter(aggregator -> {
            String valueInRow = Objects.toString(getTableModel().getValueAt(aggregator.getRowIndex(), aggregator.getColumnIndex()), "");
            Double normalizedNumber = NumberUtils.normalizeNumber(valueInRow, this.getNumberFormat());
            boolean isValid = false;
            if (normalizedNumber != null) {
                if (NumberUtils.isMinorError(expectedSum, normalizedNumber, getError(), getErrorBoundMethod())) {
                    isValid = true;
                }
            }
            return isValid;
        }).collect(Collectors.toList());
    }
}
