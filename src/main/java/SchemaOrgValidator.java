import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.RDF;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SchemaOrgValidator {

  public static List<String> validateJsonLdAgainstSchemaOrg(String jsonldInput) {
    List<String> errorMessages = new ArrayList<>();
    OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
    OntModel inputDataModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

    // Load the schema.org ontology from a local file
    try {
      RDFDataMgr.read(ontModel, new FileInputStream("schemaorg.jsonld"), Lang.JSONLD);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      errorMessages.add(e.getMessage());
      return errorMessages;
    }

    // Parse the input JSON-LD
    RDFDataMgr.read(inputDataModel, new StringReader(jsonldInput), null, Lang.JSONLD);

    // Validate the input model against the ontology
    errorMessages.addAll(validateModel(inputDataModel, ontModel));
    return errorMessages;
  }

  private static List<String> validateModel(Model inputDataModel, OntModel ontModel) {
    List<String> errors = new ArrayList<>();

    // Iterate over each statement in the input data
    StmtIterator iter = inputDataModel.listStatements();
    while (iter.hasNext()) {
      Statement stmt = iter.nextStatement();
      Property predicate = stmt.getPredicate();
      RDFNode object = stmt.getObject();

      // Validate types
      if (predicate.equals(RDF.type)) {
        if (!ontModel.containsResource(object.asResource())) {
          errors.add("Type not found in schema.org: " + object.asResource().getURI());
        }
      }
      // Validate properties
      else {
        if (!ontModel.containsResource(predicate)) {
          errors.add("Property not found in schema.org: " + predicate.getURI());
        }
      }
    }

    return errors;
  }


  // Optional main method for testing
  public static void main(String[] args) throws IOException {
    // Example JSON-LD input
    String jsonldInput = "{"
        + "\"@context\": \"https://schema.org\","
        + "\"@type\": \"Person\","
        + "\"name\": \"Test\","
        + "\"jobTitle\": \"Software Engineer\","
        + "\"worksFor\": {"
        + "\"@type\": \"Organization\","
        + "\"name\": \"X\""
        + "}}";

    List<String> validationErrors = validateJsonLdAgainstSchemaOrg(jsonldInput);

    if (validationErrors.isEmpty()) {
      System.out.println("The input JSON-LD is valid according to schema.org.");
    } else {
      System.out.println("The input JSON-LD is not valid according to schema.org. Errors:");
      validationErrors.forEach(System.out::println);
    }
  }
}
