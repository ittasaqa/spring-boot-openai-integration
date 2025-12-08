package com.showcase.aiintegration.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chat Request DTO
 * 
 * Request object for chat operations with validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 4000, message = "Message too long (max 4000 characters)")
    private String message;

    private String userId;

    private String model = "gpt-3.5-turbo"; // Default model

    private Double temperature = 0.7; // Creativity level (0.0 - 2.0)

    private Integer maxTokens = 1000; // Maximum response length

    private String systemPrompt; // Optional custom system prompt
}
