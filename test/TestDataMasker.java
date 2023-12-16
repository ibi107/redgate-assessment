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
    Files.copy(Path.of("resources/dummy/people.json"), Path.of("resources/people.json"),
      StandardCopyOption.REPLACE_EXISTING);
    Files.copy(Path.of("resources/dummy/nuts.json"), Path.of("resources/nuts.json"),
      StandardCopyOption.REPLACE_EXISTING);
    Files.copy(Path.of("resources/dummy/a.rules.json"), Path.of("resources/a.rules.json"),
      StandardCopyOption.REPLACE_EXISTING);
    Files.copy(Path.of("resources/dummy/b.rules.json"), Path.of("resources/b.rules.json"),
      StandardCopyOption.REPLACE_EXISTING);
    Files.copy(Path.of("resources/dummy/c.rules.json"), Path.of("resources/c.rules.json"),
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
  Tests whether masking people.json with a.rules.json masks the file correctly by comparing it with
  a model solution.
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
  Tests whether masking people.json with b.rules.json masks the file correctly by comparing it with
  a model solution.
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
}
