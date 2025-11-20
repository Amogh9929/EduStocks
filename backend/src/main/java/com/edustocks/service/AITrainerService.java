package com.edustocks.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.time.Duration;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AITrainerService {

    @Value("${ai.provider:openai}")
    private String aiProvider;

    @Value("${openai.api.key:}")
    private String openAiKey;

    @Value("${gemini.api.key:}")
    private String geminiKey;

    public String getAIResponse(String prompt) {
        try {
            if ("gemini".equalsIgnoreCase(aiProvider)) {
                return getAIResponseWithGemini(prompt);
            }
            return getAIResponseWithOpenAI(prompt);
        } catch (Exception e) {
            e.printStackTrace();
            return ("gemini".equalsIgnoreCase(aiProvider) ? "Error communicating with Gemini: " : "Error communicating with OpenAI: ") + e.getMessage();
        }
    }

    private String getAIResponseWithOpenAI(String prompt) {
        OpenAiService service = new OpenAiService(openAiKey, Duration.ofSeconds(60));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        new ChatMessage("system", "You are an AI stock learning assistant."),
                        new ChatMessage("user", prompt)
                ))
                .maxTokens(300)
                .build();

        var response = service.createChatCompletion(request);
        if (response.getChoices().isEmpty()) {
            return "No response received from OpenAI.";
        }
        return response.getChoices().get(0).getMessage().getContent();
    }

    @SuppressWarnings("unchecked")
    private String getAIResponseWithGemini(String prompt) {
        if (geminiKey == null || geminiKey.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY is not set");
        }

        String model = "gemini-pro"; // text-only model suitable for chat
        String url = String.format("https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s", model, geminiKey);

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", "You are an AI stock learning assistant."), Map.of("text", prompt))
                ))
        );

        Map<String, Object> response = WebClient.create()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block(Duration.ofSeconds(60));

        if (response == null) {
            return "No response received from Gemini.";
        }

        // Extract candidates[0].content.parts[0].text
        try {
            var candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates == null || candidates.isEmpty()) return "No response received from Gemini.";
            var content = (Map<String, Object>) candidates.get(0).get("content");
            if (content == null) return "No response received from Gemini.";
            var parts = (List<Map<String, Object>>) content.get("parts");
            if (parts == null || parts.isEmpty()) return "No response received from Gemini.";
            Object text = parts.get(0).get("text");
            return text == null ? "" : text.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse Gemini response: " + ex.getMessage(), ex);
        }
    }

    /**
     * Generate a question for the given difficulty level and topic using the AI model.
     * Returns a map containing a generated question id and the question text.
     */
    public Map<String, Object> generateQuestion(String level, String topic) {
        String prompt = String.format("Create a %s-level question about %s. Return just the question text.", level, topic);
        String questionText = getAIResponse(prompt);

        Map<String, Object> result = new HashMap<>();
        result.put("questionId", UUID.randomUUID().toString());
        result.put("text", questionText);
        return result;
    }

    /**
     * Check the submitted answer for a question. This is a lightweight implementation that
     * asks the AI to evaluate the answer and returns a map with a verdict.
     */
    public Map<String, Object> checkAnswer(String questionId, Integer answer) {
        String prompt = String.format("Evaluate the answer '%d' for question id %s. Reply with 'correct' or 'incorrect' and a short explanation.", answer, questionId);
        String evaluation = getAIResponse(prompt);

        Map<String, Object> response = new HashMap<>();
        response.put("questionId", questionId);
        response.put("evaluation", evaluation);
        response.put("correct", evaluation != null && evaluation.toLowerCase().contains("correct"));
        return response;
    }

    /**
     * Answer a free-form query at a given difficulty level.
     */
    public String answerQuery(String query, String level) {
        String prompt = String.format("As a %s-level tutor, answer the following query:\n%s", level, query);
        return getAIResponse(prompt);
    }
}


