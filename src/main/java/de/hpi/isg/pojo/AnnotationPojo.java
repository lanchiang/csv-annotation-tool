package de.hpi.isg.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Lan Jiang
 * @since 9/18/19
 */
public class AnnotationPojo {

    @JsonProperty("start_line_number")
    @Getter @Setter
    private int startLineNumber;

    @JsonProperty("end_line_number")
    @Getter @Setter
    private int endLineNumber;

    @JsonProperty("line_type")
    @Getter @Setter
    private String lineType;

    public AnnotationPojo() {}

    public AnnotationPojo(int startLineNumber, int endLineNumber, String lineType) {
        this.startLineNumber = startLineNumber;
        this.endLineNumber = endLineNumber;
        this.lineType = lineType;
    }
}
