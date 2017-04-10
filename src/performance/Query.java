package performance;

import java.util.List;

/**
 * Created by federico on 4/3/17.
 */
public abstract class Query {

    int relevance(int docId, List<Integer> relevantDocuments) {
        if (relevantDocuments.contains(docId)) {
            return 1;
        } else
            return 0;
    }
}
