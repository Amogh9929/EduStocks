package com.edustocks.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    @Value("${openai.api.key}")
    private String apiKey;

    public String getAIResponse(String prompt) {
        try {
            // Initialize OpenAI service with timeout
            OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(60));

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

        } catch (Exception e) {
            e.printStackTrace();
            return "Error communicating with OpenAI: " + e.getMessage();
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


