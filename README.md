# Data2Structure

## Overview
This Java application is designed to transform unstructured data into structured JSON-LD format, following the Schema.org standards. It includes a main class that iterates through the transformation and validation process, using the OpenAI GPT API for generating structured data, and a validation class to ensure the generated data adheres to Schema.org standards.

## Class Description

### `GPTClient`
Handles communication with the OpenAI GPT API. It sends requests to the API and processes the responses. The class uses an API key stored in `application.properties` for authorization and constructs requests based on provided prompts.

### `JsonExtractor`
A utility class to extract JSON data from the responses received from the GPT API. It uses regular expressions to identify and return valid JSON strings.

### `SchemaOrgValidator`
Validates the structured JSON-LD data against the Schema.org standards. It checks for compliance with the Schema.org ontology, identifying any discrepancies or unsupported properties and types.

### `Unstructured2Structured`
The main class of the application. It uses the GPT Client to convert unstructured data into structured format, then utilizes JsonExtractor to format the response, and SchemaOrgValidator to validate the structured data. The process iterates up to five times to refine the data until it passes the schema.org validation.

## Setup and Configuration

1. **Clone the Repository**
   ```bash
   git clone [repository URL]
   ```

2. **Add `application.properties` File**
   Create a file named `application.properties` in the `src/main/resources` directory with your OpenAI GPT API key:
   ```properties
   gpt.api.key=YOUR_API_KEY_HERE
   ```

3. **Build the Project**
   Use your favorite Java IDE or build tool to compile the project.

4. **Run the Application**
   Execute the `Unstructured2Structured` class to start the application.

## Usage

Modify the `prompt` string in the `Unstructured2Structured` class to define the unstructured data you wish to process. The application will automatically send this data to the GPT API, extract the relevant JSON, and validate it against Schema.org standards.

```java
String prompt = "Your unstructured data here";
```

## Dependencies

- OpenAI GPT API
- Apache HttpClient for API communication
- Jackson for JSON processing
- Jena API for RDF and ontology management

Ensure these dependencies are included in your project's build configuration (e.g., `pom.xml` for Maven).

## Contributing

Feel free to fork the repository and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.

## License

```
[MIT License]
