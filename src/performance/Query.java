package performance;

/**
 * Created by federico on 4/3/17.
 */
public interface Query {

    double computeSingleQuery(String pathToGroundTruth, String pathToQueryFile, int queryId);

    double computeAll(String pathToGroundTruth, String pathToQueryFile);
}
