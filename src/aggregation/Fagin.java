package aggregation;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by federico on 4/9/17.
 */
public class Fagin extends Aggregation {

    @Override
    public Map<Integer, Double> aggregateSingleQuery(int queryId) {
        //k is set as the number of relevant document for the given query
        int k = super.getGroundTruth().get(queryId).size();
        Map<Integer, Double> text = super.getReadFileInstance().getBm25StopwordTextRanking().get(queryId);
        Map<Integer, Double> title = super.getReadFileInstance().getBm25StopwordTitleRanking().get(queryId);
        List<Integer> textDocIds = new LinkedList<>(text.keySet());
        List<Integer> titleDocIds = new LinkedList<>(title.keySet());
        //contains values seen once
        Map<Integer, Double> seen = new LinkedHashMap<>();
        //contains values seen from text ranking
        Map<Integer, Double> textSeen = new LinkedHashMap<>();
        //contains values seen from title ranking
        Map<Integer, Double> titleSeen = new LinkedHashMap<>();
        Map<Integer, Double> result = new LinkedHashMap<>();
        int found = 0;
        int position = 0;

        while (found <= k && position < textDocIds.size() && position < titleDocIds.size()) {
            //current element of the text ranking
            int textDocId = textDocIds.get(position);
            //the current value was seen previously
            double textScore = text.get(textDocId);
            if (seen.containsKey(textDocId)) {
                found++;
                double titleScore = title.get(textDocId) * 2.;
                result.put(textDocId, textScore + titleScore);
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
                double titleScore = titleSeen.get(docId);
                double textScore = 0.;
                //text ranking may not contain the current document
                if (text.containsKey(docId))
                    textScore = text.get(docId);
                result.put(docId, textScore + titleScore);
            }
        }
        return super.orderMap(k, result);
    }

}

