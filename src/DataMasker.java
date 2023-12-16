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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class containing the main logic for masking sensitive information in a JSON file according to
 * some specified rules.
 *
 * @author Ibtesam Ahmad
 */
@SuppressWarnings("SuspiciousRegexArgument")
public class DataMasker {
  private final String dataPath;
  private final String rulePath;

  public DataMasker(String dataPath, String rulePath) {
    this.dataPath = dataPath;
    this.rulePath = rulePath;
  }

  /**
   * A method which parses the data and rule JSON files, so they can be processed and ultimately
   * mask the sensitive information in the original data file.
   *
   * @throws IOException if getDataPath() returns an invalid file path
   */
  public void mask() throws IOException {
    // Parses data and rule into a JsonArray
    JsonArray dataArr = JsonParser.parseReader(new FileReader(getDataPath())).getAsJsonArray();
    JsonArray ruleArr = JsonParser.parseReader(new FileReader(getRulePath())).getAsJsonArray();

    // Applies the rules to each JSON data element
    for (JsonElement ruleElement : ruleArr) {
      String rule = ruleElement.getAsString();

      for (JsonElement dataElement : dataArr) {
        JsonObject dataObject = dataElement.getAsJsonObject();
        applyMask(dataObject, rule);
      }
    }

    // Writes the masked JSON data array back into its original file
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String maskedJson = gson.toJson(dataArr);

    try (FileWriter fileWriter = new FileWriter(getDataPath())) {
      fileWriter.write(maskedJson);
    }
  }

  /**
   * A helper method which masks the values of a JsonObject depending on the type of rule.
   * For instance, a rule starting with 'k' will mask out that key's entire value and a rule
   * starting with 'v' will mask out only the part(s) of the value which matches the regex pattern.
   *
   * @param dataObject the JsonObject to be masked
   * @param rule the regex pattern the mask is applied to
   */
  private void applyMask(JsonObject dataObject, String rule) {
    String regex = rule.substring(2);
    // Masks entire value for a key
    if (rule.charAt(0) == 'k') {
      for (Map.Entry<String, JsonElement> data : dataObject.entrySet()) {
        if (data.getKey().matches(regex)) {
          String maskedData = data.getValue().getAsString().replaceAll(".", "*");
          dataObject.addProperty(data.getKey(), maskedData);
        }
      }

    // Masks only parts of the value which matches the regex
    } else if (rule.charAt(0) == 'v') {
      for (Map.Entry<String, JsonElement> data : dataObject.entrySet()) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data.getValue().getAsString());
        StringBuilder maskedData = new StringBuilder();

        while (matcher.find()) {
          String out = "*".repeat(matcher.group().length());
          matcher.appendReplacement(maskedData, out);
        }
        matcher.appendTail(maskedData);

        dataObject.addProperty(data.getKey(), maskedData.toString());
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