import aggregation.Fagin;
import data.ReadFile;
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
        NMDCG.computeAllTheValues(1, 3, 5, 10);
        RPrecision rp = new RPrecision();
        rp.computeAllTheValues();
        System.out.println("FAGIAN");
        Fagin f = new Fagin();
        Map<Integer, Double> output = f.fagin(1, 21);
        for (int key : output.keySet())
            System.out.println(key + " = " + output.get(key));

    }
}