import query.RPrecision;

/**
 * Created by federico on 4/1/17.
 */
public class Main {

    public static void main(String[] args) {
        String pathToGroundTruth="/home/federico/Dropbox/intellij/wir_homework1/Cranfield_DATASET/" +
                "default/cran_Ground_Truth.tsv";
        String pathToFile="/home/federico/Dropbox/intellij/wir_homework1/Cranfield_DATASET/" +
                "default/output_cstt_default.tsv";
        RPrecision rp = new RPrecision();
        System.out.println(rp.computeRPrecisionForSingleQuery(pathToGroundTruth,pathToFile,1));
        System.out.println(rp.averageRPrecision(pathToGroundTruth, pathToFile));

    }
}
