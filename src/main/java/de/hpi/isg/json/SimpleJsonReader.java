package de.hpi.isg.json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author lan
 * @since 2020/3/17
 */
public class SimpleJsonReader {

    public JSONObject getJsonObject(String line) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(line);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject;
    }
}
