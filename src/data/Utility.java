package data;

import java.util.List;
import java.util.Map;

/**
 * Created by federico on 4/12/17.
 */
public class Utility {


    public static int relevance(int docId, List<Integer> relevantDocuments) {
        if (relevantDocuments.contains(docId)) {
            return 1;
        } else
            return 0;
    }

}
