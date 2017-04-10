package performance;

import data.ReadFile;

import java.util.List;
import java.util.Map;

/**
 * Created by federico on 4/3/17.
 */
public abstract class Query {

    private static final String PATH_DEFAULT_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/";
    private static final String PATH_ENGLISH_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stemmer/";
    private static final String PATH_STOPWORD_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/";

    int relevance(int docId, List<Integer> relevantDocuments) {
        if (relevantDocuments.contains(docId)) {
            return 1;
        } else
            return 0;
    }
}
