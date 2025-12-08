package com.showcase.aiintegration.controller;

import com.showcase.aiintegration.model.dto.ChatResponse;
import com.showcase.aiintegration.model.dto.DocumentAnalysisRequest;
import com.showcase.aiintegration.service.OpenAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Document Analysis Controller
 * 
 * REST API endpoints for AI-powered document analysis.
 * Supports summarization, sentiment analysis, key points extraction, and Q&A.
 * 
 * Package Tier: Premium
 */
@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
@Slf4j
public class DocumentAnalysisController {

    private final OpenAIService openAIService;

    /**
     * General document analysis - Premium Package Feature
     * 
     * POST /api/document/analyze
     * Analyze documents based on specified analysis type
     */
    @PostMapping("/analyze")
    public ResponseEntity<ChatResponse> analyzeDocument(@Valid @RequestBody DocumentAnalysisRequest request) {
        log.info("Analyzing document with type: {}", request.getAnalysisType());

        ChatResponse response = openAIService.analyzeDocument(
                request.getDocument(),
                request.getAnalysisType(),
                request.getQuestion(),
                request.getModel());

        return ResponseEntity.ok(response);
    }

    /**
     * Document summarization - Premium Package Feature
     * 
     * POST /api/document/summarize
     * Generate concise summaries of long documents
     */
    @PostMapping("/summarize")
    public ResponseEntity<ChatResponse> summarizeDocument(
            @RequestParam String document,
            @RequestParam(required = false, defaultValue = "medium") String summaryLength,
            @RequestParam(required = false, defaultValue = "gpt-3.5-turbo") String model) {

        log.info("Summarizing document (length: {})", summaryLength);

        String question = null;
        switch (summaryLength.toLowerCase()) {
            case "short":
                question = "brief";
                break;
            case "medium":
                question = "moderate";
                break;
            case "long":
                question = "detailed";
                break;
        }

        ChatResponse response = openAIService.analyzeDocument(
                document,
                "summarize",
                question,
                model);

        return ResponseEntity.ok(response);
    }

    /**
     * Sentiment analysis - Premium Package Feature
     * 
     * POST /api/document/sentiment
     * Analyze the emotional tone and sentiment of text
     */
    @PostMapping("/sentiment")
    public ResponseEntity<ChatResponse> analyzeSentiment(
            @RequestParam String text,
            @RequestParam(required = false, defaultValue = "false") Boolean detailedAnalysis,
            @RequestParam(required = false, defaultValue = "gpt-3.5-turbo") String model) {

        log.info("Analyzing sentiment (detailed: {})", detailedAnalysis);

        String question = detailedAnalysis
                ? "Provide a detailed sentiment analysis including overall sentiment, key emotions, and tone."
                : "Provide the overall sentiment (positive, negative, or neutral) and a brief explanation.";

        ChatResponse response = openAIService.analyzeDocument(
                text,
                "sentiment",
                question,
                model);

        return ResponseEntity.ok(response);
    }

    /**
     * Key points extraction - Premium Package Feature
     * 
     * POST /api/document/keypoints
     * Extract main points and important information from documents
     */
    @PostMapping("/keypoints")
    public ResponseEntity<ChatResponse> extractKeyPoints(
            @RequestParam String document,
            @RequestParam(required = false, defaultValue = "5") Integer maxPoints,
            @RequestParam(required = false, defaultValue = "gpt-3.5-turbo") String model) {

        log.info("Extracting key points (max: {})", maxPoints);

        String question = "Extract the " + maxPoints + " most important key points from this document. " +
                "Present them as a numbered list with brief explanations.";

        ChatResponse response = openAIService.analyzeDocument(
                document,
                "keypoints",
                question,
                model);

        return ResponseEntity.ok(response);
    }

    /**
     * Question answering - Premium Package Feature
     * 
     * POST /api/document/qa
     * Answer questions based on provided documents
     */
    @PostMapping("/qa")
    public ResponseEntity<ChatResponse> answerQuestion(
            @RequestParam String document,
            @RequestParam String question,
            @RequestParam(required = false, defaultValue = "gpt-3.5-turbo") String model) {

        log.info("Answering question about document: {}", question);

        ChatResponse response = openAIService.analyzeDocument(
                document,
                "qa",
                question,
                model);

        return ResponseEntity.ok(response);
    }

    /**
     * Document comparison - Premium Package Feature
     * 
     * POST /api/document/compare
     * Compare two documents and highlight differences or similarities
     */
    @PostMapping("/compare")
    public ResponseEntity<ChatResponse> compareDocuments(
            @RequestParam String document1,
            @RequestParam String document2,
            @RequestParam(required = false, defaultValue = "differences") String focusOn,
            @RequestParam(required = false, defaultValue = "gpt-3.5-turbo") String model) {

        log.info("Comparing documents (focus: {})", focusOn);

        String combinedDocument = "Document 1:\n" + document1 + "\n\nDocument 2:\n" + document2;
        String question = "Compare these two documents and highlight their " + focusOn +
                ". Provide a detailed analysis.";

        ChatResponse response = openAIService.analyzeDocument(
                combinedDocument,
                "qa",
                question,
                model);

        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "AI Document Analysis Service");
        health.put("features", List.of(
                "Document Summarization",
                "Sentiment Analysis",
                "Key Points Extraction",
                "Question Answering",
                "Document Comparison"));
        return ResponseEntity.ok(health);
    }
}
