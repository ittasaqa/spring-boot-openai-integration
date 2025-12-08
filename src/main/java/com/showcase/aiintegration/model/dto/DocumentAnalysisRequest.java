package com.showcase.aiintegration.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Document Analysis Request DTO
 * 
 * Request for document analysis features (summarization, sentiment, Q&A)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentAnalysisRequest {

    @NotBlank(message = "Document text is required")
    @Size(max = 10000, message = "Document too long (max 10000 characters)")
    private String document;

    @NotBlank(message = "Analysis type is required")
    private String analysisType; // summarize, sentiment, keypoints, qa

    private String question; // For Q&A analysis

    private Integer maxSummaryLength; // For summarization

    private String model = "gpt-3.5-turbo";
}
