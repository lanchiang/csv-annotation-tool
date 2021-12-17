package de.hpi.isg.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * @author Lan Jiang
 * @since 10/11/19
 */
public class ResultPojo {

    @JsonProperty("result")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Getter
    @Setter
    private Collection<SpreadSheetPojo> spreadSheetPojos;

    public ResultPojo() {}
}
