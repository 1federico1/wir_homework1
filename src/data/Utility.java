package data;

import java.util.List;
import java.util.Map;

/**
 * Created by federico on 4/12/17.
 */
public class Utility {

    public static final String PATH_DEFAULT_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/";
    public static final String PATH_ENGLISH_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
                    "Cranfield_DATASET/stemmer/";
    public static final String PATH_STOPWORD_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
                            "Cranfield_DATASET/stopword_stemmer/";

    public static void orderMap(int k, Map<Integer, Double> result, List<Double> scores, Map<Integer, Double> ordered) {
        //rebuilds the map corresponding to the top k scores
        int i = 0;
        for(double score : scores) {
            for(int key : result.keySet()) {
                if(score == result.get(key) && i < k) {
                    ordered.put(key, score);
                    i++;
                }
            }
        }
    }

    public static int relevance(int docId, List<Integer> relevantDocuments) {
        if (relevantDocuments.contains(docId)) {
            return 1;
        } else
            return 0;
    }
    public static void printResult(Map<Integer, Double> results, int queryId) {
        int rank=1;
        for(int docId : results.keySet()) {
            System.out.println(queryId+"\t"+docId+"\t"+rank+"\t"+results.get(docId));
            rank++;
        }
    }
}
