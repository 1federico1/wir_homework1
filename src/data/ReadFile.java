package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by federico on 4/1/17.
 */

public class ReadFile {

    private List<String[]> readFile(String path) {
        List<String[]> result = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            br.readLine();
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                String[] split = currentLine.split("\\s+");
                result.add(split);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fr != null)
                    fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * For each query ID the list of retrieved documents ID is provided.
     * @param path of the file containing the results.
     * @return a map whit query ID as key and a list of corresponding retrieved documents as value.
     */
    public Map<Integer, List<Integer>> getQueryIdRetrievedDocuments(String path) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        for (String[] line : this.readFile(path)) {
            int queryId = Integer.parseInt(line[0]);
            int relevantDocId = Integer.parseInt(line[1]);
            this.checkMap(result, queryId, relevantDocId);
        }
        return result;
    }

    private void checkMap(Map<Integer, List<Integer>> map, int queryId, int relevantDocId) {
        if (map.containsKey(queryId)) {
            List<Integer> documents = map.get(queryId);
            if (!documents.contains(relevantDocId))
                documents.add(relevantDocId);
            map.put(queryId, documents);
        } else {
            map.put(queryId, new ArrayList<>(relevantDocId));
        }
    }

    /**
     * All files are named in the same way, this method returns all the files in the stemmer folder that start with the
     * nameToSearch parameter (usually the naming convention is 'output_scorerFunctionFieldWhereIsApplied.tsv)
     * @param pathToStemmer
     * @param nameToSearch
     * @return
     */
    public List<String> getQueryFiles(String pathToStemmer, String nameToSearch) {
        List<String> result = new ArrayList<>();
        File[] files = new File(pathToStemmer).listFiles();
        for(File file : files) {
            if(file.getName().contains(nameToSearch)) {
                result.add(file.getAbsolutePath());
            }
        }
        return result;
    }

    public String getRelevantDocumentsQueryPath(String pathToStemmer, String grounTruthFileName) {
        File[] files = new File(pathToStemmer).listFiles();
        for(File file : files) {
            if(file.getName().contains(grounTruthFileName)) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }
}
