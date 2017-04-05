package performance;

import data.ReadFile;

import java.util.List;
import java.util.Map;

/**
 * Created by federico on 4/3/17.
 */
public class NMCGD extends Query {
    private ReadFile rf;
    public NMCGD (){
        rf = new ReadFile();
    }
    @Override
    public double computeSingleQuery(String pathToGroundTruth, String pathToQueryFile, int queryId) {
        int k = 3; //esempio
        Map<Integer, List<Integer>> query2relevantDocuments = rf.getQueryIdRetrievedDocuments(pathToGroundTruth);
        Map<Integer, List<Integer>> query2retrievedDocuments = rf.getQueryIdRetrievedDocuments(pathToQueryFile);
        List<Integer> relevantDocuments = query2relevantDocuments.get(queryId);
        List<Integer> retrievedDocuments = query2retrievedDocuments.get(queryId);
        int relevanceFirstDoc = relevance(retrievedDocuments.get(0),relevantDocuments);
        int MDCG = relevanceFirstDoc;;
        // aggiungere gestione caso in cui la query non ritorna risultati
        for( int i = 2 ; i<= k; i++){
            MDCG += relevance(retrievedDocuments.get(i), relevantDocuments) / (Math.log10(k)/Math.log(2));
        }
        int MaximumMDCG;// k/log2k

        return 0.0;


    }

    @Override
    public double computeAll(String pathToGroundTruth, String pathToQueryFile) {
        return 0;
    }



}

