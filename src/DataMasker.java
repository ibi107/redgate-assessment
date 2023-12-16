import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * A class holding the main logic for the data masker.
 *
 * @author Ibtesam Ahmad
 */
@SuppressWarnings("SuspiciousRegexArgument")
public class DataMasker {
  private final String dataPath;
  private final String rulePath;


  public DataMasker(String dataPath, String rulesPath) {
    this.dataPath = dataPath;
    this.rulePath = rulesPath;
  }

  /**
   * A method which parses sensitive information from the data and rule Json files, replacing them
   * with '*'.
   *
   * @throws IOException if getDataPath() returns an invalid file path
   */
  public void mask() throws IOException {
    JsonArray dataArr = JsonParser.parseReader(new FileReader(getDataPath())).getAsJsonArray();
    JsonArray ruleArr = JsonParser.parseReader(new FileReader(getRulePath())).getAsJsonArray();

    for (JsonElement ruleElement : ruleArr) {
      String rule = ruleElement.getAsString();

      for (JsonElement dataElement : dataArr) {
        JsonObject dataObject = dataElement.getAsJsonObject();
        applyMask(dataObject, rule);
      }
    }

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String maskedJson = gson.toJson(dataArr);

    try (FileWriter fileWriter = new FileWriter(getDataPath())) {
      fileWriter.write(maskedJson);
    }
  }

  /**
   * A helper method which masks the values of a JsonObject with asterisks of same length.
   *
   * @param dataObject the JsonObject to mask the value of
   * @param rule the regex pattern the mask is applied to
   */
  private void applyMask(JsonObject dataObject, String rule) {
    String pattern = rule.substring(2);

    if (rule.charAt(0) == 'k') {
      for (Map.Entry<String, JsonElement> data : dataObject.entrySet()) {
        if (data.getKey().equals(pattern)) {
          String maskedData = data.getValue().getAsString().replaceAll(".", "*");
          dataObject.addProperty(data.getKey(), maskedData);
        }
      }
    }
  }

  public String getDataPath() {
    return dataPath;
  }

  public String getRulePath() {
    return rulePath;
  }
}
