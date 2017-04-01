package data;

import java.io.BufferedReader;
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

    public static final String GROUNDTRUTH_DEFAULT = "/home/federico/Dropbox/intellij/wir_homework1/Cranfield_DATASET/" +
            "default/cran_Ground_Truth.tsv";

    public Map<Integer,List<Integer>> query2relevantDoc;

    public ReadFile() {
        this.query2relevantDoc = new HashMap<>();
    }

    public List<String[]> readFile(String path) {
        List<String[]> result = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr=new FileReader(path);
            br=new BufferedReader(fr);
            br.readLine();
            String currentLine;
            while((currentLine=br.readLine())!=null) {
                String[] split=currentLine.split("\\s+");
                result.add(split);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Map<Integer, List<Integer>> getQuery2relevantDoc(String path) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        for(String[] line : this.readFile(path)) {
            int queryId=Integer.parseInt(line[0]);
            int relevantDocId=Integer.parseInt(line[1]);
            this.checkMap(result,queryId,relevantDocId);
        }
        return result;
    }

    private void checkMap(Map<Integer, List<Integer>> map, int queryId, int relevantDocId) {
        if(map.containsKey(queryId)) {
            List<Integer> documents=map.get(queryId);
            if(!documents.contains(relevantDocId))
                documents.add(relevantDocId);
            map.put(queryId,documents);
        } else {
            map.put(queryId, new ArrayList<>(relevantDocId));
        }
    }


    public static void main(String[] args) {
        ReadFile rf= new ReadFile();
        Map<Integer, List<Integer>> groundTruth = rf.getQuery2relevantDoc(GROUNDTRUTH_DEFAULT);
        System.out.println(groundTruth);
        String pathToCsTextAndTitleDefault = "/home/federico/Dropbox/intellij/wir_homework1/Cranfield_DATASET/" +
                "default/output_cstt_default.tsv";
        Map<Integer, List<Integer>> CsTextAndTitleDefault = rf.getQuery2relevantDoc(pathToCsTextAndTitleDefault);


    }


}
