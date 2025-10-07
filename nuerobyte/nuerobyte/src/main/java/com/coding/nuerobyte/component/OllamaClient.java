package com.coding.nuerobyte.component;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class OllamaClient {

    private final WebClient webClient;

    public OllamaClient() {
        // Default Ollama REST endpoint
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:11434/api")
                .build();
    }

    /**
     * Generate a response from a given model using a prompt (non-chat).
     * Returns the raw text from the model as String.
     */
    public String generate(String model, String prompt, int maxTokens, Duration timeout) {
        Map<String, Object> payload = Map.of(
                "model", model,
                "prompt", prompt,
                "max_tokens", maxTokens
        );

        Mono<String> mono = webClient.post()
                .uri("/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(timeout);

        return mono.block(); // blocking for simplicity; use async in production
    }

    /**
     * Chat-style call (recommended if you want role messages)
     */
    public String chat(String model, Object messagesPayload, Duration timeout) {
        Map<String, Object> payload = Map.of(
                "model", model,
                "messages", messagesPayload
        );

        Mono<String> mono = webClient.post()
                .uri("/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(timeout);

        return mono.block();
    }
}
