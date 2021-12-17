package de.hpi.isg.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hpi.isg.io.ResultCache;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Lan Jiang
 * @since 9/18/19
 */
public class JsonWriter<T> {

    private ObjectMapper objectMapper = new ObjectMapper();

    public void write(ResultCache<T> resultCache) {
        try {
            String outputJsonPath = "./annotation_result.json";
            objectMapper.writeValue(new FileOutputStream(outputJsonPath), resultCache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
