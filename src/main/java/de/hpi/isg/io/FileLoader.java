package de.hpi.isg.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

/**
 * @author lan
 * @since 2020/3/17
 */
public class FileLoader {

    public List<String> loadGzipToLines(String path) {
        List<String> lines = new ArrayList<>();
        GZIPInputStream gzip;
        BufferedReader bufferedReader = null;
        try {
            gzip = new GZIPInputStream(new FileInputStream(path));
            bufferedReader = new BufferedReader(new InputStreamReader(gzip));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(bufferedReader).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    public List<String> loadGzipToLines(File file) {
        String filePath = file.getPath();
        return loadGzipToLines(filePath);
    }
}
