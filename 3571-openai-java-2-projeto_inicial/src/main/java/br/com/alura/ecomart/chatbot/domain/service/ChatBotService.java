package br.com.alura.ecomart.chatbot.domain.service;

import br.com.alura.ecomart.chatbot.infra.openai.OpenAIClient;
import br.com.alura.ecomart.chatbot.infra.openai.RequestDataChatCompletion;
import org.springframework.stereotype.Service;

@Service
public class ChatBotService {

    private OpenAIClient client;

    public ChatBotService(OpenAIClient client) {
        this.client = client;
    }

    public String answerQuestion(String question) {
        var systemPrompt = "Voce é um chatbot de atendimento a clientes de um ecommerce e deve responder apenas perguntas relacionadas ao ecommerce";
        var data = new RequestDataChatCompletion(systemPrompt, question);
        return client.requestChatCompletion(data);
    }
}
