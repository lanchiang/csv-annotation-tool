package de.hpi.isg.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Lan Jiang
 * @since 9/18/19
 */
public class ResultCache<T> {

    @JsonProperty("result")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Getter
    private Collection<T> resultCache = new LinkedList<>();

    public void addResultToCache(T result) {
        resultCache.add(result);
    }
}
