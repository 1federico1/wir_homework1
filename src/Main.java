import aggregation.Fagin;
import aggregation.Threshold;
import data.IO;
import performance.NMDCG;
import performance.RPrecision;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by federico on 4/1/17.
 */
public class Main {

    private static final String pathToResults = System.getProperty("user.home")+
            "/hw/results";

    public static void main(String[] args) {
        System.out.println("Statistics");
        IO.getInstance().init(IO.getPathDefaultStemmer());
        Map<String, Map<Integer, List<Integer>>> files = IO.getInstance().getFiles();
        Set<Integer> numberOfDocuments = new HashSet<>();
        for(String fileName : files.keySet()) {
            Map<Integer, List<Integer>> map = files.get(fileName);
            for(int queryId : map.keySet()) {
                for(Integer docId : map.get(queryId))
                    numberOfDocuments.add(docId);
            }

        }
        System.out.println("Number of documents: " + numberOfDocuments.size());
        System.out.println("Number of queries: " + IO.getInstance().getGroundTruth().size());
        System.out.println("NMDCG");
        NMDCG NMDCG = new NMDCG();
        NMDCG.computeValuesForAllTheStemmers();
        System.out.println("RPRECISION");
        RPrecision rp = new RPrecision();
        rp.computeValuesForAllTheStemmers();
        Fagin f = new Fagin();
        Threshold t = new Threshold();
        IO.getInstance().writeFile(pathToResults+"/fagin.tsv", f.aggregate());
        IO.getInstance().writeFile(pathToResults+"/threshold.tsv",t.aggregate());
        Map<Integer, List<Integer>> fagin = IO.getInstance().getQueryIdRetrievedDocuments(pathToResults+"/fagin.tsv");
        System.out.println("FAGIN = " + rp.averageRPrecision(fagin));
        Map<Integer, List<Integer>> threshold = IO.getInstance().getQueryIdRetrievedDocuments(pathToResults+"/threshold.tsv");
        System.out.println("THRESHOLD = " + rp.averageRPrecision(threshold));


    }
}