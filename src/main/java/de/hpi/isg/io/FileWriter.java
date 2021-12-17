package de.hpi.isg.io;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * @author lan
 * @since 2021/1/17
 */
public class FileWriter {

    public static void writeJSONObjectToDisc(List<JSONObject> results, String outputPath) {
        //Write JSON file
        java.io.FileWriter file = null;
        try {
            file = new java.io.FileWriter(outputPath, false);
            for (JSONObject jsonObject : results) {
                file.write(jsonObject.toJSONString());
                file.write("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                assert file != null;
                file.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
