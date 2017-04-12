package aggregation;

import data.ReadFile;
import data.Utility;

import java.util.*;

import static data.Utility.orderMap;

/**
 * Created by federico on 4/9/17.
 */
public class Threshold {

    private static final String PATH_TO_BM25TEXT = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/output_bm25text.tsv";
    private static final String PATH_TO_BM25TITLE = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/output_bm25title.tsv";
    private ReadFile rf;
    private Map<Integer, List<Integer>> groundTruth;

    public Threshold() {
        this.rf = new ReadFile();
        this.groundTruth = this.rf.getGroundTruth();
    }

    public void computeThresholdForAllTheQueries() {
        System.out.println("QUERY\tDOC_ID\tRANK\tSCORE");
        for (int queryId : this.groundTruth.keySet()) {
            Utility.printResult(this.threshold(queryId), queryId);
        }
    }

    public Map<Integer, Double> threshold(int queryId) {
        int k = this.groundTruth.get(queryId).size();
        Map<Integer, Double> text = this.rf.getDocIdScore(PATH_TO_BM25TEXT, queryId);
        Map<Integer, Double> title = this.rf.getDocIdScore(PATH_TO_BM25TITLE, queryId);
        List<Integer> textKeys = new LinkedList<>(text.keySet());
        List<Integer> titleKeys = new LinkedList<>(title.keySet());
        Map<Integer, Double> ordered = new LinkedHashMap<>();
        Map<Integer, Double> tempResult = new HashMap<>();

        int position = 0;
        double thresholdCounter;
        boolean repeat = true;

        while (position < textKeys.size() && position < titleKeys.size() && repeat) {

            //STEP 1 : Set the Threshold to be the aggregate of the scores seen in this access

            thresholdCounter = (text.get(textKeys.get(position)) + title.get(titleKeys.get(position)))*2.;

            double tempScore;
            //STEP 2 : Do random accesses and compute the score of the objects seen

            if (textKeys.get(position) != titleKeys.get(position)) {

                for (Integer docId : text.keySet()) {
                    if (title.containsKey(docId)) {
                        tempScore = text.get(docId) + (title.get(docId) * 2);
                        tempResult.put(docId, tempScore);
                    } else
                        tempScore = text.get(docId);
                    tempResult.put(docId, tempScore);
                }

                for (Integer docId : title.keySet()) {
                    if (text.containsKey(docId)) {
                        tempScore = (title.get(docId) * 2) + text.get(docId);
                        tempResult.put(docId, tempScore);
                    } else {
                        tempScore = title.get(docId);
                        tempResult.put(docId, tempScore);
                    }
                }

                List<Double> scores = new LinkedList<>(tempResult.values());
                Collections.sort(scores);
                Collections.reverse(scores);
                orderMap(k, tempResult, scores, ordered);
                boolean thresholdPassed = true;
                for (double score : ordered.values()) {
                    if (score < thresholdCounter)
                        thresholdPassed = false;
                }

                if (thresholdPassed)
                    repeat = false;

            } else {
                tempScore = text.get(textKeys.get(position)) + title.get(titleKeys.get(position))*2.;
                tempResult.put(textKeys.get(position), tempScore);

            }
            position++;
        }
        return ordered;
    }


}
