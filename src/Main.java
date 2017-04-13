import aggregation.Fagin;
import aggregation.Threshold;
import data.WriteFile;
import performance.NMDCG;
import performance.RPrecision;

/**
 * Created by federico on 4/1/17.
 */
public class Main {

    private static final String pathToResults = "/home/federico/Dropbox/intellij/wir_homework1/results";

    public static void main(String[] args) {
        System.out.println("NMDCG");
        NMDCG NMDCG = new NMDCG();
        NMDCG.computeValuesForAllTheStemmers();
        System.out.println("RPRECISION")
        RPrecision rp = new RPrecision();
        rp.computeValuesForAllTheStemmers();
        System.out.println("FAGIN");
        Fagin f = new Fagin();
        System.out.println("THRESHOLD");
        Threshold t = new Threshold();
        WriteFile wf = new WriteFile();
        wf.writeFile(pathToResults+"/fagin.tsv", f.compute());
        wf.writeFile(pathToResults+"/threshold.tsv",t.compute());

    }
}