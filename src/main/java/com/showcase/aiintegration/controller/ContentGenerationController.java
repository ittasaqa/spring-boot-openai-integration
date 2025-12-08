package com.showcase.aiintegration.controller;

import com.showcase.aiintegration.model.dto.ChatResponse;
import com.showcase.aiintegration.model.dto.ContentGenerationRequest;
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
 * Content Generation Controller
 * 
 * REST API endpoints for AI-powered content generation.
 * Supports blog posts, emails, code generation, social media content, etc.
 * 
 * Package Tier: Standard & Premium
 */
@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
@Slf4j
public class ContentGenerationController {

    private final OpenAIService openAIService;

    /**
     * General content generation - Standard Package Feature
     * 
     * POST /api/content/generate
     * Generate any type of content based on parameters
     */
    @PostMapping("/generate")
    public ResponseEntity<ChatResponse> generateContent(@Valid @RequestBody ContentGenerationRequest request) {
        log.info("Generating {} content on topic: {}", request.getContentType(), request.getTopic());

        ChatResponse response = openAIService.generateContent(
                request.getContentType(),
                request.getTopic(),
                request.getTone(),
                request.getAdditionalInstructions(),
                request.getModel());

        return ResponseEntity.ok(response);
    }

    /**
     * Blog post generation - Standard Package Feature
     * 
     * POST /api/content/blog-post
     * Generate engaging blog posts
     */
    @PostMapping("/blog-post")
    public ResponseEntity<ChatResponse> generateBlogPost(
            @RequestParam String topic,
            @RequestParam(required = false, defaultValue = "professional") String tone,
            @RequestParam(required = false) String targetAudience,
            @RequestParam(required = false, defaultValue = "800") Integer wordCount) {

        log.info("Generating blog post on: {}", topic);

        String additionalInstructions = "Write a comprehensive blog post";
        if (targetAudience != null) {
            additionalInstructions += " for " + targetAudience;
        }
        if (wordCount != null) {
            additionalInstructions += " of approximately " + wordCount + " words";
        }
        additionalInstructions += ". Include an engaging introduction, well-structured body paragraphs, and a strong conclusion.";

        ChatResponse response = openAIService.generateContent(
                "blog",
                topic,
                tone,
                additionalInstructions,
                "gpt-3.5-turbo");

        return ResponseEntity.ok(response);
    }

    /**
     * Email generation - Standard Package Feature
     * 
     * POST /api/content/email
     * Generate professional emails
     */
    @PostMapping("/email")
    public ResponseEntity<ChatResponse> generateEmail(
            @RequestParam String purpose,
            @RequestParam(required = false, defaultValue = "professional") String tone,
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) String additionalContext) {

        log.info("Generating email for: {}", purpose);

        StringBuilder instructions = new StringBuilder("Write a complete email");
        if (recipient != null) {
            instructions.append(" to ").append(recipient);
        }
        instructions.append(" regarding: ").append(purpose);
        if (additionalContext != null) {
            instructions.append(". Context: ").append(additionalContext);
        }
        instructions.append(". Include appropriate greeting, body, and closing.");

        ChatResponse response = openAIService.generateContent(
                "email",
                purpose,
                tone,
                instructions.toString(),
                "gpt-3.5-turbo");

        return ResponseEntity.ok(response);
    }

    /**
     * Code generation and explanation - Premium Package Feature
     * 
     * POST /api/content/code
     * Generate code snippets with explanations
     */
    @PostMapping("/code")
    public ResponseEntity<ChatResponse> generateCode(
            @RequestParam String description,
            @RequestParam(required = false, defaultValue = "java") String language,
            @RequestParam(required = false, defaultValue = "true") Boolean includeComments,
            @RequestParam(required = false, defaultValue = "true") Boolean includeExplanation) {

        log.info("Generating {} code for: {}", language, description);

        StringBuilder instructions = new StringBuilder("Generate ");
        instructions.append(language).append(" code that: ").append(description);

        if (includeComments) {
            instructions.append(". Include helpful code comments.");
        }
        if (includeExplanation) {
            instructions.append(". Provide a brief explanation of how the code works after the code block.");
        }

        ChatResponse response = openAIService.generateContent(
                "code",
                description,
                "technical",
                instructions.toString(),
                "gpt-4" // Use GPT-4 for better code generation
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Social media content generation - Standard Package Feature
     * 
     * POST /api/content/social-media
     * Generate engaging social media posts
     */
    @PostMapping("/social-media")
    public ResponseEntity<ChatResponse> generateSocialMediaPost(
            @RequestParam String platform,
            @RequestParam String topic,
            @RequestParam(required = false, defaultValue = "engaging") String tone,
            @RequestParam(required = false, defaultValue = "false") Boolean includeHashtags) {

        log.info("Generating social media post for {} on: {}", platform, topic);

        StringBuilder instructions = new StringBuilder("Create a ");
        instructions.append(platform).append(" post about: ").append(topic);

        if (includeHashtags) {
            instructions.append(". Include relevant hashtags.");
        }

        // Platform-specific instructions
        switch (platform.toLowerCase()) {
            case "twitter":
            case "x":
                instructions.append(" Keep it under 280 characters.");
                break;
            case "linkedin":
                instructions.append(" Make it professional and insightful.");
                break;
            case "instagram":
                instructions.append(" Make it visually descriptive and engaging.");
                break;
            case "facebook":
                instructions.append(" Make it conversational and engaging.");
                break;
        }

        ChatResponse response = openAIService.generateContent(
                "social-media",
                topic,
                tone,
                instructions.toString(),
                "gpt-3.5-turbo");

        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "AI Content Generation Service");
        health.put("supportedContentTypes", List.of(
                "Blog Posts",
                "Emails",
                "Code Generation",
                "Social Media Posts",
                "Custom Content"));
        return ResponseEntity.ok(health);
    }
}
