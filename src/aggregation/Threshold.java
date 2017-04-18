package aggregation;

import data.ReadFile;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by federico on 4/9/17.
 */
public class Threshold extends Aggregation {


    private ReadFile rf;
    private Map<Integer, List<Integer>> groundTruth;

    public Threshold() {
        this.rf = new ReadFile();
        this.groundTruth = this.rf.getGroundTruth();
    }

    @Override
    public Map<Integer, Double> aggregateSingleQuery(int queryId) {
        int k = this.groundTruth.get(queryId).size();
        Map<Integer, Double> text = this.rf.getBm25StopwordTextRanking().get(queryId);
        Map<Integer, Double> title = this.rf.getBm25StopwordTitleRanking().get(queryId);
        List<Integer> textKeys = new LinkedList<>(text.keySet());
        List<Integer> titleKeys = new LinkedList<>(title.keySet());
        Map<Integer, Double> ordered = new LinkedHashMap<>();
        Map<Integer, Double> tempResult = new LinkedHashMap<>();

        int position = 0;
        double thresholdCounter;
        boolean repeat = true;

        while (position < textKeys.size() && position < titleKeys.size() && repeat) {

            //STEP 1 : Set the Threshold to be the aggregateSingleQuery of the scores seen in this access
            Integer textDocId = textKeys.get(position);
            Integer titleDocId = titleKeys.get(position);
            double textScore = text.get(textDocId);
            double titleScore = title.get(titleDocId) * 2.;
            thresholdCounter = textScore + titleScore;
            double tempScore;

            //STEP 2 : Do random accesses and aggregate the score of the objects seen
            if (textDocId != titleDocId) {
                // check if title ranking contains the current docId of text ranking
                if (title.containsKey(textDocId)) {
                    tempScore = text.get(textDocId) + (title.get(textDocId) * 2.);
                    tempResult.put(textDocId, tempScore);
                } else {
                    tempScore = text.get(textDocId);
                    tempResult.put(textDocId, tempScore);
                }
                // check if text ranking contains the current docId of title ranking
                if (text.containsKey(titleDocId)) {
                    tempScore = (title.get(titleDocId) * 2) + text.get(titleDocId);
                    tempResult.put(titleDocId, tempScore);
                } else {
                    tempScore = title.get(titleDocId) * 2.;
                    tempResult.put(titleDocId, tempScore);
                }

            }
            //same rank for the current docID
            else {
                tempScore = textScore + titleScore;
                tempResult.put(textDocId, tempScore);
            }

            ordered = super.orderMap(k, tempResult);
            position++;


            if (isThresholdExceeded(ordered, thresholdCounter) && ordered.size() == k) {
                repeat = false;
            }


        }
        return ordered;
    }

    private boolean isThresholdExceeded(Map<Integer, Double> ordered, double thresholdCounter) {
        boolean thresholdPassed = true;
        for (double score : ordered.values()) {
            if (score < thresholdCounter)
                thresholdPassed = false;
        }
        return thresholdPassed;
    }


}
