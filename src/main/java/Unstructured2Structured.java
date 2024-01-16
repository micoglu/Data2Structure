import java.io.IOException;
import java.util.List;

public class Unstructured2Structured {

  static JsonExtractor jsonExtractor = new JsonExtractor();

  public static void main(String[] args) throws IOException {
    String prompt = "Max is an Software Developer specialized in developing AI Applications, he is working for the Company X, he is specialized in Java and Python Development";
    String structuredData = GPTClient.sendGPTRequest(prompt);

    boolean isValidated;
    int attempt = 0;
    do {
      attempt++;
      System.out.println("Attempt: " + attempt + ": Structured Data:\n" + structuredData);
      String formattedStructuredData = JsonExtractor.extractJson(structuredData);

      // Validate the structured text against schema.org
      List<String> validationErrors = SchemaOrgValidator.validateJsonLdAgainstSchemaOrg(
          formattedStructuredData);

      isValidated = validationErrors.isEmpty();
      if (!isValidated) {
        System.out.println("Validation errors found:\n" + validationErrors);
        // Request GPTClient to refine data based on validation errors
        structuredData = GPTClient.sendGPTRequest(
            "Refine this: " + structuredData + " with errors: " + validationErrors);
        structuredData = JsonExtractor.extractJson(structuredData);
      }
    } while (!isValidated && attempt < 5); // Limit to 5 attempts to prevent infinite loops

    if (isValidated) {
      System.out.println("schema.org Validation successful, no errors found.");
    } else {
      System.out.println("Failed to validate after 5 attempts.");
    }
    System.out.println("\n Final Structured Data:\n" + structuredData);
  }
}
