package com.showcase.aiintegration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Chat Response DTO
 * 
 * Response object containing AI response and metadata
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String message;
    
    private String model;
    
    private Integer tokensUsed;
    
    private LocalDateTime timestamp;
    
    private String conversationId;
    
    private Boolean success;
    
    private String error;

    /**
     * Creates a successful response
     */
    public static ChatResponse success(String message, String model, Integer tokens) {
        return ChatResponse.builder()
                .message(message)
                .model(model)
                .tokensUsed(tokens)
                .timestamp(LocalDateTime.now())
                .success(true)
                .build();
    }

    /**
     * Creates an error response
     */
    public static ChatResponse error(String errorMessage) {
        return ChatResponse.builder()
                .error(errorMessage)
                .timestamp(LocalDateTime.now())
                .success(false)
                .build();
    }
}
