package de.hpi.isg.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hpi.isg.pojo.ResultPojo;

import java.io.File;
import java.io.IOException;

/**
 * @author Lan Jiang
 * @since 10/11/19
 */
public class JsonReader {

    private ObjectMapper objectMapper = new ObjectMapper();

    public ResultPojo read() throws IOException {
        String annotationResultJsonPath = "./annotation_result.json";
        File file = new File(annotationResultJsonPath);
        if (!file.exists()) {
            return null;
        }
        return objectMapper.readValue(new File(annotationResultJsonPath), ResultPojo.class);
    }
}
