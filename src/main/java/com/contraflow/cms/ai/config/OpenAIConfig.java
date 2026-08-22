package com.contraflow.cms.ai.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    /**
     * Build the client from an explicit API key rather than {@code fromEnv()}, which throws
     * at startup when OPENAI_API_KEY is missing (breaking deploys on Render). A blank key
     * still lets the app boot; only actual AI calls would fail until the key is configured.
     */
    @Bean
    public OpenAIClient openAIClient(@Value("${OPENAI_API_KEY:}") String apiKey) {
        String key = (apiKey != null && !apiKey.isBlank()) ? apiKey : "not-configured";
        return OpenAIOkHttpClient.builder()
                .apiKey(key)
                .build();
    }
}
