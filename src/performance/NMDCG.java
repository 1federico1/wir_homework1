package performance;

import data.IO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static data.IO.getPathDefaultStemmer;
import static data.IO.getPathEnglishStemmer;
import static data.IO.getPathStopwordStemmer;
import static data.Utility.*;

/**
 * NMDCG
 */
public class NMDCG {
    private IO rf;

    public NMDCG() {
        this.rf = IO.getInstance();
    }


    /**
     * Computes the NMDCG for a single query result, compared with the Ground Truth (relevant documents) results
     * @param map
     * @param queryId
     * @param k
     * @return
     */
    public double computeSingleQuery(Map<Integer, List<Integer>> map, int queryId, int k) {
        int cut = Math.min(k, this.rf.getGroundTruth().get(queryId).size());
        List<Integer> relevantDocuments = this.rf.getGroundTruth().get(queryId);
        List<Integer> retrievedDocuments = map.get(queryId);

        double mdcg = relevance(retrievedDocuments.get(0), relevantDocuments);
        double maximumMdcg = 1.;
        if (cut > 1) {
            int i = 1;
                while (i < cut) {
                    double logBaseTwoK = Math.log10(i+1) / Math.log10(2.);
                    mdcg += (relevance(retrievedDocuments.get(i), relevantDocuments) / logBaseTwoK);
                    i++;
                }
            maximumMdcg = getMaximumMdcg(cut);
        }

        return mdcg / maximumMdcg;
    }

    private double getMaximumMdcg(int cut) {
        double maximumMdcg = 1.;
        for (int j = 2; j <= cut; j++) {
            double log = Math.log10(j) / Math.log10(2.);
            maximumMdcg += 1. / log;
        }
        return maximumMdcg;
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


    public void computeValuesForAllTheStemmers() {
        int[] val = new int[]{1, 3, 5, 10};
        System.out.println("DEFAULT STEMMER");
        this.rf.init(getPathDefaultStemmer());
        computeAllK(val);
        System.out.println("ENGLISH STEMMER");
        this.rf.init(getPathEnglishStemmer());
        computeAllK(val);
        System.out.println("STOPWORD STEMMER");
        this.rf.init(getPathStopwordStemmer());
        computeAllK(val);
    }

    private void computeAllK(int[] val) {
        for (int k : val) {
            System.out.println("k = " + k);
            this.computeSingleKForAllFiles(k);
        }
    }
}

