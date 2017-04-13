package aggregation;

import data.ReadFile;
import data.Utility;

import java.util.*;

/**
 * Created by federico on 4/9/17.
 */
public class Fagin {
    private ReadFile rf;
    private static final String PATH_TO_GROUND_TRUTH = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/cran_Ground_Truth.tsv";
    private Map<Integer, List<Integer>> groundTruth;

    public Fagin() {
        this.rf = new ReadFile();
        this.groundTruth = this.rf.getQueryIdRetrievedDocuments(PATH_TO_GROUND_TRUTH);
    }

    public Map<Integer, Map<Integer, Double>> compute() {
        Map<Integer, Map<Integer, Double>> result = new LinkedHashMap<>();
        for(int queryId : this.groundTruth.keySet()) {
            Map<Integer, Double> tmp = this.fagin(queryId);
            result.put(queryId, tmp);
        }
        return result;
    }

    public Map<Integer, Double> fagin(int queryId) {
        //k is set as the number of relevant document for the given query
        int k = this.groundTruth.get(queryId).size();
        Map<Integer, Double> text = this.rf.getBm25StopwordTextRanking().get(queryId);
        Map<Integer, Double> title = this.rf.getBm25StopwordTitleRanking().get(queryId);
        List<Integer> textDocIds = new LinkedList<>(text.keySet());
        List<Integer> titleDocIds = new LinkedList<>(title.keySet());
        //contains values seen once
        Map<Integer, Double> seen = new LinkedHashMap<>();
        //contains values seen from text ranking
        Map<Integer, Double> textSeen = new HashMap<>();
        //contains values seen from title ranking
        Map<Integer, Double> titleSeen = new HashMap<>();
        Map<Integer, Double> result = new HashMap<>();
        int found = 0;
        int position = 0;

        while (found < k && position < textDocIds.size() && position < titleDocIds.size()) {
            //current element of the text ranking
            int textDocId = textDocIds.get(position);
            //the current value was seen previously
            double textScore = text.get(textDocId);
            if (seen.containsKey(textDocId)) {
                found++;
                double titleScore = title.get(textDocId) * 2.;
                result.put(textDocId, textScore + titleScore );
                seen.remove(textDocId);
            } else {
                seen.put(textDocId, textScore);
                textSeen.put(textDocId, textScore);
            }
            //current element of the title ranking
            int titleDocId = titleDocIds.get(position);
            double titleScore = title.get(titleDocId) * 2.;
            if (seen.containsKey(titleDocId)) {
                textScore = text.get(titleDocId);
                result.put(titleDocId, titleScore + textScore);
                seen.remove(titleDocId);
                found++;

            } else {
                seen.put(titleDocId, titleScore);
                titleSeen.put(titleDocId, titleScore);
            }
            position++;

        }
        //seen contains the values seen in only one ranking
        for (Integer docId : seen.keySet()) {
            //the current docId was seen in text
            if (textSeen.containsKey(docId)) {
                double textScore = textSeen.get(docId);
                double titleScore = 0.;
                //title ranking may not contain the current document
                if (title.containsKey(docId))
                    titleScore = title.get(docId) * 2.;
                result.put(docId, textScore + titleScore);
            }
            //the current docId was seen in title
            else if (titleSeen.containsKey(docId)) {
                double titleScore = titleSeen.get(docId) * 2.;
                double textScore = 0.;
                //text ranking may not contain the current document
                if (text.containsKey(docId))
                    textScore = text.get(docId);
                result.put(docId, textScore + titleScore);
            }
        }
        List<Double> scores = new LinkedList<>(result.values());
        Collections.sort(scores);
        Collections.reverse(scores);
        Map<Integer, Double> ordered = new LinkedHashMap<>();
        Utility.orderMap(k, result, scores, ordered);
        return ordered;
    }

}

