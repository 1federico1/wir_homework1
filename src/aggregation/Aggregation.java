package aggregation;

import data.ReadFile;

import java.util.*;

/**
 * Created by federico on 4/13/17.
 */
public abstract class Aggregation {

    ReadFile rf = ReadFile.getInstance();
    Map<Integer, List<Integer>> groundTruth = rf.getGroundTruth();

    public abstract Map<Integer, Double> aggregate(int queryId);

    public Map<Integer, Map<Integer, Double>> compute() {
        Map<Integer, Map<Integer, Double>> result = new LinkedHashMap<>();
        for(int queryId : this.groundTruth.keySet()) {
            Map<Integer, Double> tmp = this.aggregate(queryId);
            result.put(queryId, tmp);
        }
        return result;
    }

    public List<Double> getSortedListOfValues(Map<Integer, Double> result) {
        List<Double> scores = new LinkedList<>(result.values());
        Collections.sort(scores);
        Collections.reverse(scores);
        return scores;
    }

    Map<Integer, Double> orderMap(int k, Map<Integer, Double> result, List<Double> scores) {
        //rebuilds the map corresponding to the top k scores
        int i = 0;
        Map<Integer, Double> ordered = new LinkedHashMap<>();
        for(double score : scores) {
            for(int key : result.keySet()) {
                if(score == result.get(key) && i < k) {
                    ordered.put(key, score);
                    i++;
                }
            }
        }
        return ordered;
    }

}
