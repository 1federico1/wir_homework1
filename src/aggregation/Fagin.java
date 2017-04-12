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
        //k is set as the number of relevant document for the given query
        int k = this.groundTruth.get(queryId).size();
        Map<Integer, Double> text = this.rf.getDocIdScore(PATH_TO_BM25TEXT, queryId);
        Map<Integer, Double> title = this.rf.getDocIdScore(PATH_TO_BM25TITLE, queryId);
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
            //current element of the text ranking
            int textDocId = textDocIds.get(position);
            //the current value was seen previously
            double textScore = text.get(textDocId);
            if (seen.containsKey(textDocId)) {
                System.out.println("found in text " + textDocId);
                found++;
                double titleScore = title.get(textDocId) * 2.;
                result.put(textDocId, textScore + titleScore );
                seen.remove(textDocId);
            } else {
                System.out.println("seen in text " + textDocId);
                seen.put(textDocId, textScore);
                textSeen.put(textDocId, textScore);
            }
            //current element of the title ranking
            int titleDocId = titleDocIds.get(position);
            double titleScore = title.get(titleDocId) * 2.;
            if (seen.containsKey(titleDocId)) {
                System.out.println("found in title " + titleDocId);
                textScore = text.get(titleDocId);
                result.put(titleDocId, titleScore + textScore);
                seen.remove(titleDocId);
                found++;

            } else {
                System.out.println("seen in title " + titleDocId);
                seen.put(titleDocId, titleScore);
                titleSeen.put(titleDocId, titleScore);
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

