package data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by federico on 4/12/17.
 */
public class WriteFile {

    private static final String pathToResults = "/home/federico/Dropbox/intellij/wir_homework1/results";

    public static void appendToFileAggregate(Map<Integer, Double> singleResult) {
        Properties properties = new Properties();
        for(Map.Entry<Integer, Double> entry : singleResult.entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }
        try {
            properties.store(new FileOutputStream(pathToResults+"aggregateResults.txt"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
