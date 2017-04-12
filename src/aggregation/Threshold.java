package aggregation;

import data.ReadFile;

import java.util.*;

/**
 * Created by federico on 4/9/17.
 */
public class Threshold {

    private static final String PATH_TO_BM25TEXT = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/output_bm25text.tsv";
    private static final String PATH_TO_BM25TITLE = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/output_bm25title.tsv";
    private ReadFile rf;

    public Threshold() {
        this.rf = new ReadFile();
    }


    public Map<Integer, Double> threshold(int queryId, int k) {

        Map<Integer, Double> text = this.rf.getDocIdScore(PATH_TO_BM25TEXT, queryId);
        Map<Integer, Double> title = this.rf.getDocIdScore(PATH_TO_BM25TITLE, queryId);
        List<Integer> textKeys = new LinkedList<>(text.keySet());
        List<Integer> titleKeys = new LinkedList<>(title.keySet());
        Map<Integer, Double> ordered = new LinkedHashMap<>();
        Map<Integer, Double> tempResult = new HashMap<>();
        int position = 0;
        double thresholdCounter;
        boolean repeat = true;




        while(position <= text.keySet().size() && repeat){

            //STEP 1 : Set the Threshold to be the aggregate of the scores seen in this access

            thresholdCounter = (text.get(textKeys.get(position)) + title.get(titleKeys.get(position))*2);

            double tempScore;
            //STEP 2 : Do random accesses and compute the score of the objects seen

            if(textKeys.get(position) != titleKeys.get(position)){

                for(Integer docId : text.keySet()){
                    if(title.containsKey(docId)){
                        tempScore = text.get(docId) + (title.get(docId) * 2);
                        tempResult.put(docId, tempScore);
                    }
                    else
                        tempScore = text.get(docId);
                    tempResult.put(docId, tempScore);
                }

                for(Integer docId : title.keySet()){
                    if(text.containsKey(docId)){
                        tempScore = (title.get(docId) * 2) + text.get(docId);
                        tempResult.put(docId, tempScore);
                    }
                    else {
                        tempScore = title.get(docId);
                        tempResult.put(docId, tempScore);
                    }
                }

                List<Double> scores = new LinkedList<>(tempResult.values());
                Collections.sort(scores);
                Collections.reverse(scores);
                Utility.orderMap(k, tempResult, scores, ordered);
                boolean thresholdPassed = true;
                for(double score : ordered.values()){
                    if(score < thresholdCounter)
                        thresholdPassed = false;
                }

                if(thresholdPassed)
                    repeat = false;

            } else {

                tempScore = text.get(textKeys.get(position)) + title.get(titleKeys.get(position)*2);
                tempResult.put(textKeys.get(position), tempScore);

            }
            position++;
        }
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
