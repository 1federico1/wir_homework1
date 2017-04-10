package aggregation;

import data.ReadFile;

import java.util.*;

/**
 * Created by federico on 4/9/17.
 */
public class Fagin {
    private ReadFile rf;
    private static final String PATH_TO_GROUND_TRUTH = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/cran_Ground_Truth.tsv";
    private static final String PATH_TO_BM25TEXT = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/output_bm25text_stopword.tsv";
    private static final String PATH_TO_BM25TITLE = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/output_bm25title_stopword.tsv";
    private Map<Integer, List<Integer>> groundTruth;

    public Fagin() {
        this.rf = new ReadFile();
        this.groundTruth = this.rf.getQueryIdRetrievedDocuments(PATH_TO_GROUND_TRUTH);
    }

    public Map<Integer, Double> getDocId2ScoreForSingleQuery(int queryId) {
        return this.rf.getDocIdScore(PATH_TO_BM25TEXT, queryId);
    }

    public Map<Integer, Double> fagin(int queryId, int k) {
        System.out.println(this.groundTruth.get(queryId).size());
        Map<Integer, Double> text = this.rf.getDocIdScore(PATH_TO_BM25TEXT, queryId);
        Map<Integer, Double> title = this.rf.getDocIdScore(PATH_TO_BM25TITLE, queryId);
        List<Double> textScores = new LinkedList<>(text.values());
        List<Double> titleScores = new LinkedList<>(title.values());
        List<Integer> textKeys = new LinkedList<>(text.keySet());
        List<Integer> titleKeys = new LinkedList<>(title.keySet());
        Map<Integer, Double> seen = new LinkedHashMap<>();
        Map<Integer, Double> textSeen = new HashMap<>();
        Map<Integer, Double> titleSeen = new HashMap<>();
        Map<Integer, Double> result = new HashMap<>();
        int found = 0;
        int position = 0;

        while (found <= k && position <= text.keySet().size()) {
            if (seen.keySet().contains(textKeys.get(position))) {
                found++;
                seen.remove(textKeys.get(position));
            } else {
                seen.put(textKeys.get(position), textScores.get(position));
                textSeen.put(textKeys.get(position), textScores.get(position));
            }

            if (seen.keySet().contains(titleKeys.get(position))) {
                seen.remove(titleKeys.get(position));
                found++;

            } else {
                seen.put(titleKeys.get(position), titleScores.get(position));
                titleSeen.put(titleKeys.get(position), titleScores.get(position));
            }
            position++;

        }

        for (Integer docId : seen.keySet()) {
            if (textSeen.containsKey(docId)) {
                double textScore = textSeen.get(docId);
                double titleScore = 0.;
                if (title.containsKey(docId))
                    titleScore = title.get(docId) * 2.;
                result.put(docId, textScore + titleScore);
            } else if (titleSeen.containsKey(docId)) {
                double titleScore = titleSeen.get(docId) * 2.;
                double textScore = 0.;
                if (text.containsKey(docId))
                    textScore = text.get(docId);
                result.put(docId, textScore + titleScore);
            }
        }
        List<Double> scores = new LinkedList<>(result.values());
        Collections.sort(scores);
        Collections.reverse(scores);
        Map<Integer, Double> ordered = new LinkedHashMap<>();
        for(double score : scores) {
            for(int key : result.keySet()) {
                if(score == result.get(key)) {
                    ordered.put(key, score);
                }
            }
        }

        return ordered;
    }
}

