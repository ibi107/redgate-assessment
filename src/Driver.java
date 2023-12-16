import java.io.IOException;

/**
 * The driver class which runs the main method to initialise a DataMasker.
 *
 * @author Ibtesam Ahmad
 */
public class Driver {
  public static void main(String[] args) throws IOException {
    DataMasker dataMasker = new DataMasker(args[0], args[1]);
    dataMasker.mask();
  }
}