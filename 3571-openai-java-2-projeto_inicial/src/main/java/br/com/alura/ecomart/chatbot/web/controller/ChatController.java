package br.com.alura.ecomart.chatbot.web.controller;

import br.com.alura.ecomart.chatbot.domain.service.ChatBotService;
import br.com.alura.ecomart.chatbot.web.dto.QuestionDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({"/", "chat"})
public class ChatController {

    private static final String URL_CHAT = "chat";

    private ChatBotService service;

    public ChatController(ChatBotService service) {
        this.service = service;
    }

    @GetMapping
    public String loadPageChatbot() {
        return URL_CHAT;
    }

    @PostMapping
    @ResponseBody
    public String answerQuestion(@RequestBody QuestionDto dto) {
        return service.answerQuestion(dto.question());
    }

    @GetMapping("limpar")
    public String clearChat() {
        return URL_CHAT;
    }

}
