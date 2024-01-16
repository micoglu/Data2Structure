import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonExtractor {

  // Extracts unnecessary Strings from GPT Response
  public static String extractJson(String input) {
    String jsonPattern = "\\{.*\\}";
    Pattern pattern = Pattern.compile(jsonPattern, Pattern.DOTALL);
    Matcher matcher = pattern.matcher(input);

    if (matcher.find()) {
      return matcher.group(0);
    }

    return null;
  }

  public static void main(String[] args) {
    String input = "{ \"@context\": \"http://schema.org\", \"@type\": \"WrongTypeForPerson\", \"name\": \"X\", \"jobTitle\": \"Software WrongTypeForEngineer\", \"worksFor\": { \"@type\": \"WrongTypeForOrganization\", \"name\": \"adesso\" } }";
    String jsonOutput = extractJson(input);

    if (jsonOutput != null) {
      System.out.println(jsonOutput);
    } else {
      System.out.println("No JSON found in the input string.");
    }
  }
}
