import aggregation.Fagin;
import aggregation.Threshold;
import data.ReadFile;
import data.WriteFile;
import performance.NMDCG;
import performance.RPrecision;

import java.util.List;
import java.util.Map;

/**
 * Created by federico on 4/1/17.
 */
public class Main {

    private static final String pathToResults = "/home/federico/Dropbox/intellij/wir_homework1/results";

    public static void main(String[] args) {
        System.out.println("NMDCG");
        NMDCG NMDCG = new NMDCG();
        NMDCG.computeValuesForAllTheStemmers();
        System.out.println("RPRECISION");
        RPrecision rp = new RPrecision();
        rp.computeValuesForAllTheStemmers();
        Fagin f = new Fagin();
        Threshold t = new Threshold();
        WriteFile wf = new WriteFile();
        wf.writeFile(pathToResults+"/fagin.tsv", f.compute());
        wf.writeFile(pathToResults+"/threshold.tsv",t.compute());
        Map<Integer, List<Integer>> fagin = ReadFile.getInstance().getQueryIdRetrievedDocuments(pathToResults+"/fagin.tsv");
        System.out.println("FAGIN = " + rp.averageRPrecision(fagin));
        Map<Integer, List<Integer>> threshold = ReadFile.getInstance().getQueryIdRetrievedDocuments(pathToResults+"/threshold.tsv");
        System.out.println("THRESHOLD = " + rp.averageRPrecision(threshold));


    }
}