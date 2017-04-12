package data;

import aggregation.Fagin;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by federico on 4/12/17.
 */
public class WriteFile {

    private ReadFile rf;

    public void writeFile(String fileName) {
        rf = new ReadFile();
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw;
        Map<Integer, List<Integer>> groundTruth = this.rf.getGroundTruth();
        Fagin f = new Fagin();
        try {
            fw = new FileWriter(fileName, true);
            pw = new PrintWriter(fw);
            pw.println("QUERY\tDOC_ID\tRANK\tSCORE");
            for (Integer queryId : groundTruth.keySet()) {
                pw = new PrintWriter(fw);
                Integer rank = 1;
                Map<Integer, Double> docId2Score = f.fagin(queryId);
                for(Integer docId : docId2Score.keySet()) {
                    String line;
                    line = queryId.toString();
                    line = line + "\t"+rank.toString();
                    line = line +"\t" + docId.toString();
                    line = line + "\t" + docId2Score.get(docId);
                    System.out.println(line);
                    pw.println(line);
                    rank++;
                }

            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void appendToFileAggregate(Map<Integer, Double> singleResult, String fileName) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw;
        try {
            fw = new FileWriter(fileName, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(fw);

            for (Map.Entry<Integer, Double> entry : singleResult.entrySet()) {
                String docId = entry.getKey().toString();
                String score = entry.getValue().toString();
                String line = docId + "\t" + score;
                pw.println(line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
