package br.com.alura.ecomart.chatbot.infra.openai;

public record RequestDataChatCompletion(String systemPrompt, String userPrompt) {}
