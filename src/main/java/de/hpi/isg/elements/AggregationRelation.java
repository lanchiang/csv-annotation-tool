package de.hpi.isg.elements;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * @author lan
 * @since 2021/1/22
 */
@Getter
public class AggregationRelation {

    private final CellIndex aggregator;

    private final List<CellIndex> aggregatees;

    private final String operator;

    private final double error;

    public AggregationRelation(CellIndex aggregator, List<CellIndex> aggregatees, String operator, double error) {
        this.aggregator = aggregator;
        this.aggregatees = aggregatees;
        this.operator = operator;
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregationRelation that = (AggregationRelation) o;
        return Double.compare(that.error, error) == 0 && Objects.equals(aggregator, that.aggregator)
                && Objects.equals(aggregatees, that.aggregatees) && Objects.equals(operator, that.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aggregator, aggregatees, operator, error);
    }
}
