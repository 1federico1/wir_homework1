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
    private Map<Integer, List<Integer>> query2relevantDocuments;
    private static final String PATH_TO_GROUND_TRUTH = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/cran_Ground_Truth.tsv";
    private static final Logger LOGGER = Logger.getLogger(NMDCG.class.getName());

    public NMDCG() {
        rf = new ReadFile();
        this.query2relevantDocuments = this.rf.getQueryIdRetrievedDocuments(PATH_TO_GROUND_TRUTH);
    }

    private double computeSingleQuery(String pathToQueryFile, int queryId, int k) {
        Map<Integer, List<Integer>> query2retrievedDocuments = rf.getQueryIdRetrievedDocuments(pathToQueryFile);
        List<Integer> relevantDocuments = query2relevantDocuments.get(queryId);
        List<Integer> retrievedDocuments = query2retrievedDocuments.get(queryId);
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

    private Map<String, Double> computeNMCDGForAllTheQueries(String pathToStemmer, int k) {
        Map<Integer, Double> queryId2nmdcg = null;
        Map<String, Double> result = new HashMap<>();
        for (String pathToQuery : this.rf.getQueryFiles(pathToStemmer)) {
            queryId2nmdcg = new HashMap<>();
            String fileName = pathToQuery.replaceAll(pathToStemmer, "");
            System.out.println("Computing NMDCG of " + fileName + ", k = " + k);
            for (Integer queryId : this.query2relevantDocuments.keySet()) {
                double nmdcg = this.computeSingleQuery(pathToQuery, queryId, k);
                queryId2nmdcg.put(queryId, nmdcg);
            }
            // compute the mean for all the queries in the file
            double mean = getMean(queryId2nmdcg);
            result.put(fileName, mean);
            //System.out.println("Mean NMDCG = " + String.valueOf(mean));
        }
        return result;
    }

    private double getMean(Map<Integer, Double> result) {
        double mean = 0.;
        for (int key : result.keySet()) {
            // LOGGER.info(key+":"+result.get(key)); per vedere tutti i valori per ciascuna query
            mean += result.get(key);
        }
        return mean / (double) result.keySet().size();
    }

    public void getResults(String pathToStemmer, int... val) {
        System.out.println("AVERAGE NMDCG OF " + pathToStemmer);
        for (int k : val) {
            double csMean = 0.;
            double tfidfMean = 0.;
            double bm25Mean = 0.;
            Map<String, Double> map = this.computeNMCDGForAllTheQueries(pathToStemmer, k);
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
}

