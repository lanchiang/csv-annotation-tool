package de.hpi.isg.json;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;

/**
 * @author lan
 * @since 2021/1/21
 */
public class JsonAggregationAnnotationEntry {

    /**
     * two-number coordinate index of the aggregator cell.
     */
    @Getter @Setter
    private JSONArray aggregator_index;

    /**
     * One of the following values: "Sum", "Subtract", "Average", "Percentage".
     */
    @Getter @Setter
    private String operator;

    /**
     * Array of arrays where each array includes indices of all aggregatees involved in this computation, arrays are ordered as the
     * involved operands
     */
    @Getter @Setter
    private JSONArray aggregatee_indices;

    /**
     * The error bound parameter value used for this annotation.
     */
    @Getter @Setter
    private String error_parameter;

    public JsonAggregationAnnotationEntry(JSONArray aggregator_index, String operator, JSONArray aggregatee_indices, String error_parameter) {
        this.aggregator_index = aggregator_index;
        this.operator = operator;
        this.aggregatee_indices = aggregatee_indices;
        this.error_parameter = error_parameter;
    }
}
