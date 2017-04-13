package data;

import aggregation.Fagin;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by federico on 4/12/17.
 */
public class WriteFile {

    public void writeFile(String fileName, Map<Integer, Map<Integer, Double>> result) {
        FileWriter fw = null;
        PrintWriter pw;
        File f = new File(fileName);
        if(f.exists())
            f.delete();
        try {
            fw = new FileWriter(fileName, true);
            pw = new PrintWriter(fw);
            pw.println("QUERY\tDOC_ID\tRANK\tSCORE");
            for (Integer queryId : result.keySet()) {
                pw = new PrintWriter(fw);
                Integer rank = 1;
                Map<Integer, Double> docId2Score = result.get(queryId);
                for(Integer docId : docId2Score.keySet()) {
                    String line;
                    line = queryId.toString();
                    line = line + "\t"+rank.toString();
                    line = line +"\t" + docId.toString();
                    line = line + "\t" + docId2Score.get(docId);
                    pw.println(line);
                    rank++;
                }

            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
