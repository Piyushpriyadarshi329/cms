package com.contraflow.cms.ai.services;


import com.contraflow.cms.ai.dto.ChatRequest;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServices {

    private final OpenAIClient openAIClient;

    // Override with OPENAI_MODEL / openai.model if your key has access to a different model.
    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Value("${OPENAI_API_KEY:}")
    private String apiKey;

    public String OpenAIChat(ChatRequest message) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "AI is not configured. Set the OPENAI_API_KEY environment variable.");
        }

        String prompt = (message != null) ? message.getMessage() : null;
        if (prompt == null || prompt.isBlank()) {
            return "Please provide a message.";
        }

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)
                .addSystemMessage(SYSTEM_PROMPT)
                .addUserMessage(prompt)
                .build();

        ChatCompletion completion = openAIClient.chat().completions().create(params);

        // First choice's message content (empty if the model returned nothing).
        String content = completion.choices().stream()
                .findFirst()
                .flatMap(choice -> choice.message().content())
                .orElse("");

        return extractHtml(content);
    }

    private static final String SYSTEM_PROMPT = """
            You generate a single, complete, self-contained HTML document for a legal contract \
            that will be converted directly into a PDF.

            Rules:
            - Output ONLY the HTML document. No markdown code fences, no explanations, no commentary.
            - Start with <!DOCTYPE html> and include <html>, <head> and <body>.
            - Put all CSS inline in a <style> tag in the head.
            - Use simple, print-friendly layout. Use tables for any side-by-side layout.
            - Do NOT use flexbox, grid, external stylesheets, scripts, or remote images.
            - Fill in reasonable contract sections (parties, purpose, terms, duration, signatures) \
            based on the user's request.
            """;

    /**
     * Pull the HTML out of the model's reply. Handles the common cases where the model
     * wraps the HTML in a ``` fence and/or adds prose before/after it.
     */
    private String extractHtml(String content) {
        if (content == null) {
            return "";
        }
        String text = content.trim();

        // 1) Fenced block: ```html ... ``` (or plain ```). Take what's inside.
        int fenceStart = text.indexOf("```");
        if (fenceStart >= 0) {
            int afterFence = text.indexOf('\n', fenceStart);
            if (afterFence >= 0) {
                int fenceEnd = text.indexOf("```", afterFence + 1);
                return (fenceEnd >= 0)
                        ? text.substring(afterFence + 1, fenceEnd).trim()
                        : text.substring(afterFence + 1).trim();
            }
        }

        // 2) No fence: slice from <!doctype>/<html> to </html> if present.
        String lower = text.toLowerCase();
        int docStart = lower.indexOf("<!doctype");
        if (docStart < 0) {
            docStart = lower.indexOf("<html");
        }
        int htmlEnd = lower.lastIndexOf("</html>");
        if (docStart >= 0 && htmlEnd > docStart) {
            return text.substring(docStart, htmlEnd + "</html>".length()).trim();
        }

        // 3) Fallback: whatever we got.
        return text;
    }
}
