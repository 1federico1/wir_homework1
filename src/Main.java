import aggregation.Fagin;
import aggregation.Threshold;
import data.ReadFile;
import data.Utility;
import data.WriteFile;
import performance.NMDCG;
import performance.RPrecision;

import java.util.Map;

/**
 * Created by federico on 4/1/17.
 */
public class Main {

    public static void main(String[] args) {
        NMDCG NMDCG = new NMDCG();
        NMDCG.computeValuesForAllTheStemmers(1, 3, 5, 10);
        RPrecision rp = new RPrecision();
        rp.computeValuesForAllTheStemmers();
        System.out.println("FAGIN");
        Fagin f = new Fagin();
        f.computeFaginForAllTheQueries();
        System.out.println("THRESHOLD");
        Threshold t = new Threshold();
        t.computeThresholdForAllTheQueries();
        WriteFile wf = new WriteFile();
        Map<Integer, Double> first = t.threshold(1);
    }
}