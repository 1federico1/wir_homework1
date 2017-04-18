import aggregation.Fagin;
import aggregation.Threshold;
import data.ReadFile;
import data.WriteFile;
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

    private static final String pathToResults = "/home/federico/Dropbox/intellij/wir_homework1/results";

    public static void main(String[] args) {

        /*ReadFile rf = ReadFile.getInstance();
        rf.init(rf.getPathStopwordStemmer());
        Map<Integer, Map<Integer, Double>> text = rf.getBm25StopwordTextRanking();
        Map<Integer, Map<Integer, Double>> title = rf.getBm25StopwordTitleRanking();
        Fagin f = new Fagin();
        Threshold t = new Threshold();
        Map<Integer, Double> queryFagin = f.aggregateSingleQuery(1);
        System.out.println("FAGIN = " + queryFagin);
        Map<Integer, Double> queryThreshold = t.aggregateSingleQuery(1);
        for (int queryId : queryFagin.keySet())
            System.out.println(queryId + " = " + queryFagin.get(queryId));
        System.out.println("THRESHOLD = " + queryThreshold);
        for (int queryId : queryThreshold.keySet()) {
            System.out.println(queryId + " = " + queryThreshold.get(queryId));
        }*/
        System.out.println("Statistics");
        ReadFile.getInstance().init(ReadFile.getPathDefaultStemmer());
        Map<String, Map<Integer, List<Integer>>> files = ReadFile.getInstance().getFiles();
        Set<Integer> numberOfDocuments = new HashSet<>();
        for(String fileName : files.keySet()) {
            Map<Integer, List<Integer>> map = files.get(fileName);
            for(int queryId : map.keySet()) {
                for(Integer docId : map.get(queryId))
                    numberOfDocuments.add(docId);
            }

        }

        System.out.println("Number of documents: " + numberOfDocuments.size());
        System.out.println("NMDCG");
        NMDCG NMDCG = new NMDCG();
        NMDCG.computeValuesForAllTheStemmers();
        System.out.println("RPRECISION");
        RPrecision rp = new RPrecision();
        rp.computeValuesForAllTheStemmers();
        Fagin f = new Fagin();
        Threshold t = new Threshold();
        WriteFile wf = new WriteFile();
        wf.writeFile(pathToResults+"/fagin.tsv", f.aggregate());
        wf.writeFile(pathToResults+"/threshold.tsv",t.aggregate());
        Map<Integer, List<Integer>> fagin = ReadFile.getInstance().getQueryIdRetrievedDocuments(pathToResults+"/fagin.tsv");
        System.out.println("FAGIN = " + rp.averageRPrecision(fagin));
        Map<Integer, List<Integer>> threshold = ReadFile.getInstance().getQueryIdRetrievedDocuments(pathToResults+"/threshold.tsv");
        System.out.println("THRESHOLD = " + rp.averageRPrecision(threshold));


    }
}