package br.com.alura.ecomart.chatbot.infra.openai;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;

@Component
public class OpenAIClient {

    private final String apiKey;
    private final OpenAiService service;

    public OpenAIClient(@Value("${OPENAI_KEY}") String apiKey) {
        this.apiKey = apiKey;
        this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    public String requestChatCompletion(RequestDataChatCompletion data) {
        var request = ChatCompletionRequest
                .builder()
                .model("gpt-4-1106-preview")
                .messages(Arrays.asList(
                        new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                data.systemPrompt()),
                        new ChatMessage(
                                ChatMessageRole.USER.value(),
                                data.userPrompt())))
                .build();

        var secondsForNextAttempt = 5;
        var attempts = 0;
        while (attempts++ != 5) {
            try {
                return service
                        .createChatCompletion(request)
                        .getChoices().get(0)
                        .getMessage().getContent();
            } catch (OpenAiHttpException ex) {
                var errorCode = ex.statusCode;
                switch (errorCode) {
                    case 401 -> throw new RuntimeException("API key error!", ex);
                    case 429, 500, 503 -> {
                        try {
                            Thread.sleep(1000 * secondsForNextAttempt);
                            secondsForNextAttempt *= 2;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        throw new RuntimeException("API down! Couldn't connect!");
    }

}
