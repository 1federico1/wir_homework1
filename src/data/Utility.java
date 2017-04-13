package data;

import java.util.List;
import java.util.Map;

/**
 * Created by federico on 4/12/17.
 */
public class Utility {

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

}
