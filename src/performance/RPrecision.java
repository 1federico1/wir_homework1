package performance;

import data.ReadFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * R-Precision: Requires a set of known relevant documents Rel, from which we calculate the precision of the top Rel
 * documents returned.
 * <p>
 */
public class RPrecision extends Query {
    private ReadFile rf;
    private static final String PATH_TO_GROUND_TRUTH = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/cran_Ground_Truth.tsv";
    Map<Integer, List<Integer>> query2relevantDocuments;


    public RPrecision() {
        this.rf = new ReadFile();
        this.query2relevantDocuments = this.rf.getQueryIdRetrievedDocuments(PATH_TO_GROUND_TRUTH);
    }

    /**
     * Computes the R-precision for a single performance, compared with the Ground Truth (relevant documents) performance results.
     *
     * @param pathToGroundTruth
     * @param pathToQueryFile
     * @param queryId
     * @return the value of the R-precision
     */
    public double computeSingleQuery(String pathToGroundTruth, String pathToQueryFile, int queryId) {
        Map<Integer, List<Integer>> query2retrievedDocuments = rf.getQueryIdRetrievedDocuments(pathToQueryFile);
        List<Integer> relevantDocuments = query2relevantDocuments.get(queryId);
        int numberOfRelevantDocuments = relevantDocuments.size();
        List<Integer> retrievedDocuments = query2retrievedDocuments.get(queryId);
        /*
        Cutoff the list of returned documents at the position indicated by the size of the relevant documents set.
         */
        retrievedDocuments = retrievedDocuments.subList(0, numberOfRelevantDocuments);

        /*
        For each retrieved document check if it is present in the relevant document set
         */
        int numberOfRetrievedDocuments = 0;
        for (int docId : retrievedDocuments) {
            numberOfRetrievedDocuments += relevance(docId, relevantDocuments);
        }
        if (numberOfRelevantDocuments == 0) {
            return 0.0;
        } else
            return (double) numberOfRetrievedDocuments / (double) numberOfRelevantDocuments;
    }


    /**
     * Computes the R-precision for all the queries in the file passed as parameter, compared with the Ground Truth
     * performance results.
     *
     * @param pathToGroundTruth
     * @param pathToQueryFile
     * @return the average of all the R-precisions computed among the retrieved documents.
     */
    public double averageRPrecision(String pathToGroundTruth, String pathToQueryFile) {
        Map<Integer, List<Integer>> groundTruth = rf.getQueryIdRetrievedDocuments(pathToGroundTruth);
        double count = 0.0;
        for (int queryId : groundTruth.keySet()) {
            count += this.computeSingleQuery(pathToGroundTruth, pathToQueryFile, queryId);
        }
        return count / (double) groundTruth.keySet().size();
    }

    /**
     * Computes the average R-precision of all the query results in the given stemmer folder.
     * Hard coded variables ...:(
     * @param pathToStemmer
     * @return
     */
    public List<Double> computeAll(String pathToStemmer) {
        List<Double> results = new ArrayList<>();
        String relevantDocumentsFileName = this.rf.getRelevantDocumentsQueryPath(pathToStemmer, "Ground_Truth");
        for(String fileName : this.rf.getQueryFiles(pathToStemmer, "output")) {
            Logger.getAnonymousLogger().info("Computing R-Precision of"+fileName);
            results.add(this.averageRPrecision(relevantDocumentsFileName,fileName));
        }
        return results;
    }

}
