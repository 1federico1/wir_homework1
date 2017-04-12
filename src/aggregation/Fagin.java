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
    private static final String PATH_TO_BM25TEXT = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/output_bm25text.tsv";
    private static final String PATH_TO_BM25TITLE = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/output_bm25title.tsv";
    private Map<Integer, List<Integer>> groundTruth;

    public Fagin() {
        this.rf = new ReadFile();
        this.groundTruth = this.rf.getQueryIdRetrievedDocuments(PATH_TO_GROUND_TRUTH);
    }

    public Map<Integer, Double> getDocId2ScoreForSingleQuery(int queryId) {
        return this.rf.getDocIdScore(PATH_TO_BM25TEXT, queryId);
    }

    public Map<Integer, Double> fagin(int queryId) {
        int k = this.groundTruth.get(queryId).size();
        Map<Integer, Double> text = this.rf.getDocIdScore(PATH_TO_BM25TEXT, queryId);
        Map<Integer, Double> title = this.rf.getDocIdScore(PATH_TO_BM25TITLE, queryId);
        List<Double> textScores = new LinkedList<>(text.values());
        List<Double> titleScores = new LinkedList<>(title.values());
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
        //rankings have same size
        while (found < k && position <= text.keySet().size()) {
            if (seen.keySet().contains(textDocIds.get(position))) {
                System.out.println("found in text " + textDocIds.get(position));
                found++;
                System.out.println(textDocIds.get(position)+" = " +textScores.get(position));
                result.put(textDocIds.get(position), textScores.get(position) + title.get(textDocIds.get(position)) * 2.);
                seen.remove(textDocIds.get(position));
            } else {
                System.out.println("seen in text " + textDocIds.get(position));
                System.out.println(titleDocIds.get(position)+" = " +titleScores.get(position));

                seen.put(textDocIds.get(position), textScores.get(position));
                textSeen.put(textDocIds.get(position), textScores.get(position));
            }

            if (seen.keySet().contains(titleDocIds.get(position))) {
                System.out.println("found in title " + titleDocIds.get(position));
                result.put(titleDocIds.get(position), titleScores.get(position)*2. + text.get(titleDocIds.get(position)));
                seen.remove(titleDocIds.get(position));
                found++;

            } else {
                System.out.println("seen in title " + titleDocIds.get(position));
                seen.put(titleDocIds.get(position), titleScores.get(position));
                titleSeen.put(titleDocIds.get(position), titleScores.get(position));
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

        Utility.orderMap(k, result, scores, ordered);
        return ordered;
    }


    public void printResult(Map<Integer, Double> results, int queryId) {
        System.out.println("QUERY\tDOC_ID\tRANK\tSCORE");
        int rank=1;
        for(int docId : results.keySet()) {
            System.out.println(queryId+"\t"+docId+"\t"+rank+"\t"+results.get(docId));
            rank++;
        }
    }
}

