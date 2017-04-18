package aggregation;

import data.ReadFile;

import java.util.*;

/**
 * Created by federico on 4/13/17.
 */
public abstract class Aggregation {

    /**
     * Computes the aggregation algorithm (Fagin or Threshold) for a single query
     *
     * @param queryId
     * @return
     */
    public abstract Map<Integer, Double> aggregateSingleQuery(int queryId);

    public Map<Integer, Map<Integer, Double>> aggregate() {
        Map<Integer, Map<Integer, Double>> result = new LinkedHashMap<>();
        for (int queryId : this.getGroundTruth().keySet()) {
            Map<Integer, Double> tmp = this.aggregateSingleQuery(queryId);
            result.put(queryId, tmp);
        }
        return result;
    }

    /**
     * Non-decreasingly order a list
     *
     * @param result
     * @return
     */
    private List<Double> orderList(Map<Integer, Double> result) {
        List<Double> scores = new LinkedList<>(result.values());
        Collections.sort(scores);
        Collections.reverse(scores);
        return scores;
    }

    /**
     * Given the map of aggregated scores (docId - score) rebuilds the map ordering them.
     *
     * @param k
     * @param result
     * @return
     */
    Map<Integer, Double> orderMap(int k, Map<Integer, Double> result) {
        int i = 0;
        Map<Integer, Double> ordered = new LinkedHashMap<>();
        for (double score : this.orderList(result)) {
            for (int key : result.keySet()) {
                if (score == result.get(key) && i < k) {
                    ordered.put(key, score);
                    i++;
                }
            }
        }
        return ordered;
    }


    ReadFile getReadFileInstance() {
        return ReadFile.getInstance();
    }

    Map<Integer, List<Integer>> getGroundTruth() {
        return getReadFileInstance().getGroundTruth();
    }

}
