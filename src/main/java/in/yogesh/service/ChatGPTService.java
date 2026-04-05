package in.yogesh.service;

import in.yogesh.dto.ChatGPTRequest;
import in.yogesh.dto.ChatGPTResponse;
import in.yogesh.dto.PromptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ChatGPTService {
    private final RestClient restClient;

    @Value("${openapi.api.key}")
    private String apiKey;

    @Value("${openapi.api.model}")
    private String model;

    @Autowired
    public ChatGPTService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getChatGPTResponse(PromptRequest promptRequest){
        try {
            ChatGPTRequest request = new ChatGPTRequest(
                    model, List.of(new ChatGPTRequest.Message("user", promptRequest.prompt()))
            );

            ChatGPTResponse response = this.restClient.post()
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(request)
                    .retrieve()
                    .body(ChatGPTResponse.class);

            return response.choices().get(0).message().content();
        }  catch (Exception e) {
            return "API Error : " + e.getMessage();
        }
    }
}
