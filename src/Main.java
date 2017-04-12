import aggregation.Fagin;
import aggregation.Threshold;
import performance.NMDCG;
import performance.RPrecision;

import java.util.Map;

/**
 * Created by federico on 4/1/17.
 */
public class Main {

    private static final String PATH_DEFAULT_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/";
    private static final String PATH_ENGLISH_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stemmer/";
    private static final String PATH_STOPWORD_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/";

    public static void main(String[] args) {
 NMDCG NMDCG = new NMDCG();
        NMDCG.computeValuesForAllTheStemmers(1, 3, 5, 10);
        RPrecision rp = new RPrecision();
        rp.computeValuesForAllTheStemmers();
        System.out.println("FAGIN");
        Fagin f = new Fagin();
        Map<Integer, Double> output = f.fagin(1);
        f.printResult(output, 1);
        System.out.println("THRESHOLD");
        Threshold t = new Threshold();
    }
}