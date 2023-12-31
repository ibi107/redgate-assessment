import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestDataMasker {
  DataMasker dataMasker;

  /*
  Replaces each resource file with a model file before each test so tests can be re-runnable.
   */
  @BeforeEach
  void setup() throws IOException {
    System.gc(); // Required in case resources lock

    Files.copy(Path.of("resources/dummy/people.json"), Path.of("resources/people.json"),
      StandardCopyOption.REPLACE_EXISTING);
    Files.copy(Path.of("resources/dummy/nuts.json"), Path.of("resources/nuts.json"),
      StandardCopyOption.REPLACE_EXISTING);
  }

  /*
  Tests whether getters are indeed constructed, !isEmpty() is enough rather than comparing literal
  values.
   */
  @Test
  void testGetJson()  {
    dataMasker = new DataMasker("resources/people.json", "resources/a.rules.json");

    String dataPath = dataMasker.getDataPath();
    assertFalse(dataPath.isEmpty(), "dataPath should not be null after calling getter");
    String rulePath = dataMasker.getRulePath();
    assertFalse(rulePath.isEmpty(), "rulePath should not be null after calling getter");
  }

  /*
  Tests whether masking people.json with a.rules.json masks the file correctly.

  NOTE: all tests below this point are checking human-made solutions (some are model examples
  provided in the assignment description) with a solution from the data masker.

  NOTE: all cases labelled with "Trivial case:" indicate impractical scenarios, for example
  applying a rule clearly made for `people.json` to `nuts.json` - these test cases check
  that indeed nothing gets masked.
   */
  @Test
  void testPeopleWithRuleA() throws IOException {
    dataMasker = new DataMasker("resources/people.json", "resources/a.rules.json");

    dataMasker.mask();

    String modelContent = new String(Files.readAllBytes(Paths.get(
      "resources/masked/people.a.rules.json")));
    String maskedContent = new String(Files.readAllBytes(Paths.get(
      "resources/people.json")));

    assertEquals(JsonParser.parseString(modelContent), JsonParser.parseString(maskedContent),
      "The newly masked file should match the exact structure of the model file");
  }

  /*
  Tests whether masking people.json with b.rules.json masks the file correctly.
   */
  @Test
  void testPeopleWithRuleB() throws IOException {
    dataMasker = new DataMasker("resources/people.json", "resources/b.rules.json");

    dataMasker.mask();

    String modelContent = new String(Files.readAllBytes(Paths.get(
      "resources/masked/people.b.rules.json")));
    String maskedContent = new String(Files.readAllBytes(Paths.get(
      "resources/people.json")));

    assertEquals(JsonParser.parseString(modelContent), JsonParser.parseString(maskedContent),
      "The newly masked file should match the exact structure of the model file");
  }

  /*
  Trivial case: tests whether masking people.json with c.rules.json masks the file correctly.
   */
  @Test
  void testPeopleWithRuleC() throws IOException {
    dataMasker = new DataMasker("resources/people.json", "resources/c.rules.json");

    dataMasker.mask();

    String modelContent = new String(Files.readAllBytes(Paths.get(
      "resources/masked/people.c.rules.json")));
    String maskedContent = new String(Files.readAllBytes(Paths.get(
      "resources/people.json")));

    assertEquals(JsonParser.parseString(modelContent), JsonParser.parseString(maskedContent),
      "The newly masked file should match the exact structure of the model file");
  }

  @Test
  /*
  Trivial case: tests whether masking nuts.json with a.rules.json masks the files correctly.
   */
  void testNutsWithRuleA() throws IOException {
    dataMasker = new DataMasker("resources/nuts.json", "resources/a.rules.json");

    dataMasker.mask();

    String modelContent = new String(Files.readAllBytes(Paths.get(
      "resources/masked/nuts.a.rules.json")));
    String maskedContent = new String(Files.readAllBytes(Paths.get(
      "resources/nuts.json")));

    assertEquals(JsonParser.parseString(modelContent), JsonParser.parseString(maskedContent),
      "The newly masked file should match the exact structure of the model file");
  }

  @Test
  /*
  Trivial case: tests whether masking nuts.json with b.rules.json masks the files correctly.
   */
  void testNutsWithRuleB() throws IOException {
    dataMasker = new DataMasker("resources/nuts.json", "resources/b.rules.json");

    dataMasker.mask();

    String modelContent = new String(Files.readAllBytes(Paths.get(
      "resources/masked/nuts.b.rules.json")));
    String maskedContent = new String(Files.readAllBytes(Paths.get(
      "resources/nuts.json")));

    assertEquals(JsonParser.parseString(modelContent), JsonParser.parseString(maskedContent),
      "The newly masked file should match the exact structure of the model file");
  }

  @Test
  /*
  Tests whether masking nuts.json with c.rules.json masks the files correctly.
   */
  void testNutsWithRuleC() throws IOException {
    dataMasker = new DataMasker("resources/nuts.json", "resources/c.rules.json");

    dataMasker.mask();

    String modelContent = new String(Files.readAllBytes(Paths.get(
      "resources/masked/nuts.c.rules.json")));
    String maskedContent = new String(Files.readAllBytes(Paths.get(
      "resources/nuts.json")));

    assertEquals(JsonParser.parseString(modelContent), JsonParser.parseString(maskedContent),
      "The newly masked file should match the exact structure of the model file");
  }
}
