import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class GPTClient {

  private static final String API_KEY = "";
  private static final String API_URL = "https://api.openai.com/v1/chat/completions";

  private static final String model = "gpt-4-0613";

  public static void main(String[] args) throws IOException {
    String prompt = "My name is Mustafa and I am a software engineer at adesso. Plase answer only with the JSON-LD format, do not add any ```json or ``` at the beginning or end of the answer.";
    String response = sendGPTRequest(prompt);
    System.out.println("GPT Response: \n" + response);
  }

  public static String sendGPTRequest(String prompt) throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost request = new HttpPost(API_URL);
    request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);
    request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    request.setHeader("OpenAI-Beta", "assistants=v1");

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", "gpt-4-0613");
    requestBody.put("messages", Arrays.asList(
        new HashMap<String, String>() {{
          put("role", "system");
          put("content", "");
        }},
        new HashMap<String, String>() {{
          put("role", "user");
          put("content", "Please transform the code to the schema.org format." + prompt);
        }}
    ));

    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(requestBody);

    request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON.withCharset("UTF-8")));

    try (CloseableHttpResponse response = httpClient.execute(request)) {
      String result = EntityUtils.toString(response.getEntity());

      // Simplified response parsing
      JsonNode contentNode = objectMapper.readTree(result)
                                         .path("choices")
                                         .findPath("content");

      return contentNode.isMissingNode() ? result : contentNode.asText();
    }
  }
}
