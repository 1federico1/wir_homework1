package performance;

import data.ReadFile;

import java.util.List;
import java.util.Map;

/**
 * R-Precision: Requires a set of known relevant documents Rel, from which we calculate the precision of the top Rel
 * documents returned.
 * <p>
 */
public class RPrecision extends Query {
    private ReadFile rf;

    public RPrecision() {
        this.rf = new ReadFile();
    }

    /**
     * Computes the R-precision for a single performance, compared with the Ground Truth (relevant documents) performance results.
     *
     * @param pathToGroundTruth
     * @param pathToQueryFile
     * @param queryId
     * @return the value of the R-precision
     */
    @Override
    public double computeSingleQuery(String pathToGroundTruth, String pathToQueryFile, int queryId) {
        Map<Integer, List<Integer>> groundTruth = rf.getQueryIdRetrievedDocuments(pathToGroundTruth);
        Map<Integer, List<Integer>> query = rf.getQueryIdRetrievedDocuments(pathToQueryFile);
        System.out.println(groundTruth);
        System.out.println(query);
        List<Integer> relevantDocuments = groundTruth.get(queryId);
        int numberOfRelevantDocuments = relevantDocuments.size();
        List<Integer> retrievedDocuments = query.get(queryId);
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
    @Override
    public double computeAll(String pathToGroundTruth, String pathToQueryFile) {
        ReadFile rf = new ReadFile();
        Map<Integer, List<Integer>> groundTruth = rf.getQueryIdRetrievedDocuments(pathToGroundTruth);
        double count = 0.0;
        for (int queryId : groundTruth.keySet()) {
            count += this.computeSingleQuery(pathToGroundTruth, pathToQueryFile, queryId);
        }
        return count / (double) groundTruth.keySet().size();
    }

}
