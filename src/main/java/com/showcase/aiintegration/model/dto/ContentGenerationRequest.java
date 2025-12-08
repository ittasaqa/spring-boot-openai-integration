package com.showcase.aiintegration.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Content Generation Request DTO
 * 
 * Request for content generation features (blog posts, emails, code, etc.)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentGenerationRequest {

    @NotBlank(message = "Content type is required")
    private String contentType; // blog, email, code, social-media, etc.

    @NotBlank(message = "Topic or description is required")
    private String topic;

    private String tone; // professional, casual, friendly, formal, etc.

    private String targetAudience;

    private Integer wordCount;

    private String additionalInstructions;

    private String model = "gpt-3.5-turbo";
}
