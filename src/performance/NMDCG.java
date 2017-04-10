package performance;

import data.ReadFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by federico on 4/3/17.
 */
public class NMDCG extends Query {
    private ReadFile rf;

    private static final String PATH_DEFAULT_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/";
    private static final String PATH_ENGLISH_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stemmer/";
    private static final String PATH_STOPWORD_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/";
    private static final Logger LOGGER = Logger.getLogger(NMDCG.class.getName());

    public NMDCG() {
        this.rf = new ReadFile();
    }

    private double computeSingleQuery(Map<Integer, List<Integer>> map, int queryId, int k) {
        List<Integer> relevantDocuments = this.rf.getGroundTruth().get(queryId);
        List<Integer> retrievedDocuments = map.get(queryId);
        double mdcg = relevance(retrievedDocuments.get(0), relevantDocuments);
        double logBaseTwoK = Math.log10(k) / Math.log(2.);
        for (int i = 2; i <= k; i++) {
            mdcg += relevance(retrievedDocuments.get(i), relevantDocuments) / logBaseTwoK;
        }
        double maximumMdcg = 1.;
        for (int i = 2; i <= k; i++) {
            double log = Math.log10(i) / Math.log(2.);
            maximumMdcg += 1. / log;
        }
        return mdcg / maximumMdcg;

    }


    private Map<String, Double> computeNMCDGForAllTheQueries(int k) {
        Map<Integer, Double> queryId2nmdcg = new HashMap<>();
        for (String file : this.rf.getFiles().keySet()) {
            for (Integer queryId : this.rf.getGroundTruth().keySet()) {
                double nmdcg = this.computeSingleQuery(this.rf.getFiles().get(file), queryId, k);
                queryId2nmdcg.put(queryId, nmdcg);
            }
            double mean = this.getMean(queryId2nmdcg);

            System.out.println(file+" = "+mean);
        }
        return null;
    }

    private double getMean(Map<Integer, Double> result) {
        double mean = 0.;
        for (int key : result.keySet()) {
            mean += result.get(key);
        }
        return mean / (double) result.keySet().size();
    }

    public void computeAllTheValues(int... val) {
        System.out.println("DEFAULT STEMMER");
        this.rf.init(PATH_DEFAULT_STEMMER);
        computeAllTheStemmers(val);
        System.out.println("ENGLISH STEMMER");
        this.rf.init(PATH_ENGLISH_STEMMER);
        computeAllTheStemmers(val);
        System.out.println("STOPWORD STEMMER");
        this.rf.init(PATH_STOPWORD_STEMMER);
        computeAllTheStemmers(val);
    }

    private void computeAllTheStemmers(int[] val) {
        for (int k : val) {
            System.out.println("k = " + k);
            this.computeNMCDGForAllTheQueries(k);
        }
    }

    @Deprecated
    public void getResults(String pathToStemmer, int... val) {
        System.out.println("AVERAGE NMDCG OF " + pathToStemmer);
        for (int k : val) {
            double csMean = 0.;
            double tfidfMean = 0.;
            double bm25Mean = 0.;

            Map<String, Double> map = new HashMap<>();
            for (String key : map.keySet()) {
                if (key.contains("cs"))
                    csMean += map.get(key);
                else if (key.contains("tfidf"))
                    tfidfMean += map.get(key);
                else if (key.contains("bm25"))
                    bm25Mean += map.get(key);
            }
            System.out.println("COUNTER SCORER = " + csMean / 3.);
            System.out.println("TFIDF = " + tfidfMean / 3.);
            System.out.println("BM25 = " + bm25Mean / 3.);

        }
    }

    @Override
    public Map<String, Integer> compute(String pathToStemmer, int... k) {
        return null;
    }

    @Override
    public double computeSingle(String pathToStememr, int queryId, int... k) {
        return 0;
    }
}

