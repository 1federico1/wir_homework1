package performance;

import data.ReadFile;
import data.Utility;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by federico on 4/3/17.
 */
public class NMDCG {
    private ReadFile rf;

    private static final String PATH_DEFAULT_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/";
    private static final String PATH_ENGLISH_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stemmer/";
    private static final String PATH_STOPWORD_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/";

    public NMDCG() {
        this.rf = new ReadFile();
    }

    public double computeSingleQuery(Map<Integer, List<Integer>> map, int queryId, int k) {
        List<Integer> relevantDocuments = this.rf.getGroundTruth().get(queryId);
        List<Integer> retrievedDocuments = map.get(queryId);
        /*get(0), poi get(i) partendo da 2, saltiamo il get(1)!*/
        double mdcg = Utility.relevance(retrievedDocuments.get(0), relevantDocuments);
        double logBaseTwoK = Math.log10(k) / Math.log(2.);
        double maximumMdcg = 1.;
        if (k > 1) {
            int i = 1;
            /* < oppure <= ? */
            while (i < k) {
                mdcg += Utility.relevance(retrievedDocuments.get(i), relevantDocuments) / logBaseTwoK;
                i++;
            }

            for (int j = 2; j <= k; j++) {
                double log = Math.log10(j) / Math.log(2.);
                maximumMdcg += 1. / log;
            }
        }
        return mdcg / maximumMdcg;

    }

    private Map<String, Double> computeSingleKForAllFiles(int k) {
        Map<Integer, Double> queryId2nmdcg = new HashMap<>();
        Map<String, Double> results = new LinkedHashMap<>();
        for (String file : this.rf.getFiles().keySet()) {
            for (Integer queryId : this.rf.getGroundTruth().keySet()) {
                double nmdcg = this.computeSingleQuery(this.rf.getFiles().get(file), queryId, k);
                queryId2nmdcg.put(queryId, nmdcg);
            }
            double mean = this.getMean(queryId2nmdcg);
            results.put(file, mean);
            System.out.println(file + " = " + mean);
        }
        return results;
    }

    private double getMean(Map<Integer, Double> result) {
        double mean = 0.;
        for (int key : result.keySet()) {
            mean += result.get(key);
        }
        return mean / (double) result.keySet().size();
    }

    public void computeValuesForAllTheStemmers(int... val) {
        System.out.println("DEFAULT STEMMER");
        this.rf.init(PATH_DEFAULT_STEMMER);
        computeAllK(val);
        System.out.println("ENGLISH STEMMER");
        this.rf.init(PATH_ENGLISH_STEMMER);
        computeAllK(val);
        System.out.println("STOPWORD STEMMER");
        this.rf.init(PATH_STOPWORD_STEMMER);
        computeAllK(val);
    }

    private void computeAllK(int[] val) {
        for (int k : val) {
            System.out.println("k = " + k);
            this.computeSingleKForAllFiles(k);
        }
    }
}

