package performance;

import java.util.List;

/**
 * Created by federico on 4/3/17.
 */
public abstract class Query {

    public abstract double computeSingleQuery(String pathToGroundTruth, String pathToQueryFile, int queryId);

    public abstract double computeAll(String pathToGroundTruth, String pathToQueryFile);

    public int relevance(int docId, List<Integer> relevantDocuments) {
        if (relevantDocuments.contains(docId)) {
            return 1;
        }
        return 0;
    }



}
