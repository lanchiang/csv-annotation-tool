package de.hpi.isg.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * @author Lan Jiang
 * @since 9/18/19
 */
public class SpreadSheetPojo {

    @JsonProperty("spreadsheet_name")
    @Getter
    @Setter
    private String spreadsheetName;

    @JsonProperty("excel_file_name")
    @Getter
    @Setter
    private String excelFileName;

    @JsonProperty("time_expense")
    @Getter
    @Setter
    private long timeExpense;

    @JsonProperty("is_multitable_file")
    @Getter
    @Setter
    private String isMultitableFile;

    @JsonProperty("number_of_lines")
    @Getter @Setter
    private int numLines;

    @JsonProperty("annotations")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Getter
    @Setter
    private Collection<AnnotationPojo> annotationPojos;

    public SpreadSheetPojo() {}

    public SpreadSheetPojo(String spreadsheetName, String excelFileName, long timeExpense, boolean isMultitableFile, Collection<AnnotationPojo> annotationPojos) {
        this.spreadsheetName = spreadsheetName;
        this.excelFileName = excelFileName;
        this.timeExpense = timeExpense;
        this.isMultitableFile = isMultitableFile ? "true" : "false";
        this.annotationPojos = annotationPojos;
    }
}
