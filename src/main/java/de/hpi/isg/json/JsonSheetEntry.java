package de.hpi.isg.json;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author lan
 * @since 2020/3/18
 */
public class JsonSheetEntry {

    @Getter @Setter
    private String url;

    @Getter @Setter
    private String tableId; // sheet name

    @Getter @Setter
    private String fileName;

    @Getter @Setter
    private String dictionary;

    @Getter @Setter
    private JSONArray table_array;

    @Getter @Setter
    private JSONArray feature_array;

    @Getter @Setter
    private JSONArray feature_names;

    @Getter @Setter
    private JSONArray annotations;

    @Getter @Setter
    private int numOfRows;

    @Getter @Setter
    private int numOfColumns;

    @Getter @Setter
    private JSONArray token_tarr;

    @Getter @Setter
    private JSONArray token_tarr_reg;

    @Getter @Setter
    private JSONArray aggregation_annotations;

    @Getter @Setter
    private String number_format;

    public JsonSheetEntry(String url, String tableId, String fileName, String dictionary,
                          JSONArray table_array, JSONArray feature_array, JSONArray feature_names, JSONArray annotations,
                          int numOfRows, int numOfColumns,
                          JSONArray token_tarr, JSONArray token_tarr_reg, JSONArray aggregation_annotations, String number_format) {
        this.url = url;
        this.tableId = tableId;
        this.fileName = fileName;
        this.dictionary = dictionary;
        this.table_array = table_array;
        this.feature_array = feature_array;
        this.feature_names = feature_names;
        this.annotations = annotations;
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        this.token_tarr = token_tarr;
        this.token_tarr_reg = token_tarr_reg;
        this.aggregation_annotations = aggregation_annotations;
        this.number_format = number_format;
    }
}
