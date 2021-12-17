package de.hpi.isg.elements.aggregations;

import de.hpi.isg.elements.CellIndex;
import de.hpi.isg.utils.NumberUtils;

import javax.swing.table.TableModel;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * @author lan
 * @since 2021/1/30
 */
public class AverageAggregationDetector extends AggregationDetector {

    public AverageAggregationDetector(TableModel tableModel, double error, String numberFormat) {
        super(tableModel, error, numberFormat);
    }

    public AverageAggregationDetector(TableModel tableModel, double error, int errorBoundMethod, String numberForat) {
        super(tableModel, error, errorBoundMethod, numberForat);
    }

    @Override
    public Collection<CellIndex> getSatisfiedCellIndices(Collection<CellIndex> aggregatorCandidates, Collection<CellIndex>... selectedAggregatees) {
        if (selectedAggregatees.length != 1) {
            throw new IllegalArgumentException("Too many selected aggregatee blocks for " + this.getClass().getName());
        }
        OptionalDouble optExpectedAverage = selectedAggregatees[0].stream()
                .map(aggregatee -> NumberUtils.normalizeNumber(
                        Objects.toString(getTableModel().getValueAt(aggregatee.getRowIndex(), aggregatee.getColumnIndex()), null), this.getNumberFormat()))
                .filter(Objects::nonNull).mapToDouble(Double::doubleValue).average();
        if (!optExpectedAverage.isPresent()) {
            throw new RuntimeException("Average of the given numbers cannot be computed.");
        }
        double expectedAverage = optExpectedAverage.getAsDouble();

        return aggregatorCandidates.stream().filter(aggregator -> {
            String valueInRow = Objects.toString(getTableModel().getValueAt(aggregator.getRowIndex(), aggregator.getColumnIndex()), "");
            Double normalizedNumber = NumberUtils.normalizeNumber(valueInRow, this.getNumberFormat());
            boolean isValid = false;
            if (normalizedNumber != null) {
                if (NumberUtils.isMinorError(expectedAverage, normalizedNumber, getError(), getErrorBoundMethod())) {
                    isValid = true;
                }
            }
            return isValid;
        }).collect(Collectors.toList());
    }
}
