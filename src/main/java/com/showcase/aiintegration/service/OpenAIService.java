package com.showcase.aiintegration.service;

import com.showcase.aiintegration.exception.AIServiceException;
import com.showcase.aiintegration.model.dto.ChatRequest;
import com.showcase.aiintegration.model.dto.ChatResponse;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenAI Service
 * 
 * Core service for OpenAI API integration.
 * Handles chat completions, model selection, and prompt engineering.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIService {

    private final OpenAiService openAiService;
    private final RateLimiter aiRateLimiter;

    /**
     * Process a simple chat request without conversation history
     * 
     * @param request Chat request with message and parameters
     * @return Chat response with AI-generated message
     */
    public ChatResponse processChat(ChatRequest request) {
        try {
            // Apply rate limiting
            RateLimiter.waitForPermission(aiRateLimiter);

            log.info("Processing chat request with model: {}", request.getModel());

            // Build message list
            List<ChatMessage> messages = new ArrayList<>();

            // Add system prompt if provided
            if (request.getSystemPrompt() != null && !request.getSystemPrompt().isEmpty()) {
                messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), request.getSystemPrompt()));
            } else {
                messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
                        "You are a helpful AI assistant integrated into a Spring Boot application."));
            }

            // Add user message
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), request.getMessage()));

            // Create completion request
            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model(request.getModel())
                    .messages(messages)
                    .temperature(request.getTemperature())
                    .maxTokens(request.getMaxTokens())
                    .build();

            // Call OpenAI API
            ChatCompletionResult result = openAiService.createChatCompletion(completionRequest);

            // Extract response
            String responseMessage = result.getChoices().get(0).getMessage().getContent();
            Integer tokensUsed = result.getUsage().getTotalTokens();

            log.info("Chat completed. Tokens used: {}", tokensUsed);

            return ChatResponse.success(responseMessage, request.getModel(), tokensUsed);

        } catch (Exception e) {
            log.error("Error processing chat: {}", e.getMessage(), e);
            throw new AIServiceException("Failed to process chat request: " + e.getMessage(), e);
        }
    }

    /**
     * Process chat with conversation history (context-aware)
     * 
     * @param messages    List of conversation messages
     * @param model       Model to use
     * @param temperature Creativity level
     * @return Chat response
     */
    public ChatResponse processChatWithHistory(List<ChatMessage> messages, String model, Double temperature) {
        try {
            // Apply rate limiting
            RateLimiter.waitForPermission(aiRateLimiter);

            log.info("Processing chat with history. Message count: {}, Model: {}", messages.size(), model);

            // Create completion request
            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .temperature(temperature)
                    .maxTokens(1500)
                    .build();

            // Call OpenAI API
            ChatCompletionResult result = openAiService.createChatCompletion(completionRequest);

            // Extract response
            String responseMessage = result.getChoices().get(0).getMessage().getContent();
            Integer tokensUsed = result.getUsage().getTotalTokens();

            log.info("Chat with history completed. Tokens used: {}", tokensUsed);

            return ChatResponse.success(responseMessage, model, tokensUsed);

        } catch (Exception e) {
            log.error("Error processing chat with history: {}", e.getMessage(), e);
            throw new AIServiceException("Failed to process chat with history: " + e.getMessage(), e);
        }
    }

    /**
     * Generate content based on type and parameters
     * 
     * @param contentType            Type of content (blog, email, code, etc.)
     * @param topic                  Topic or description
     * @param tone                   Writing tone
     * @param additionalInstructions Extra instructions
     * @param model                  Model to use
     * @return Generated content
     */
    public ChatResponse generateContent(String contentType, String topic, String tone,
            String additionalInstructions, String model) {
        try {
            // Apply rate limiting
            RateLimiter.waitForPermission(aiRateLimiter);

            log.info("Generating {} content on topic: {}", contentType, topic);

            // Build specialized prompt based on content type
            String systemPrompt = buildContentGenerationPrompt(contentType, tone);
            String userPrompt = buildUserContentPrompt(contentType, topic, additionalInstructions);

            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), userPrompt));

            // Create completion request
            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .temperature(0.8) // Higher temperature for creative content
                    .maxTokens(2000)
                    .build();

            // Call OpenAI API
            ChatCompletionResult result = openAiService.createChatCompletion(completionRequest);

            String responseMessage = result.getChoices().get(0).getMessage().getContent();
            Integer tokensUsed = result.getUsage().getTotalTokens();

            log.info("Content generation completed. Tokens used: {}", tokensUsed);

            return ChatResponse.success(responseMessage, model, tokensUsed);

        } catch (Exception e) {
            log.error("Error generating content: {}", e.getMessage(), e);
            throw new AIServiceException("Failed to generate content: " + e.getMessage(), e);
        }
    }

    /**
     * Analyze document based on analysis type
     */
    public ChatResponse analyzeDocument(String document, String analysisType, String question, String model) {
        try {
            // Apply rate limiting
            RateLimiter.waitForPermission(aiRateLimiter);

            log.info("Analyzing document. Type: {}", analysisType);

            String systemPrompt = buildDocumentAnalysisPrompt(analysisType);
            String userPrompt = buildUserAnalysisPrompt(analysisType, document, question);

            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), userPrompt));

            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .temperature(0.3) // Lower temperature for analytical tasks
                    .maxTokens(1500)
                    .build();

            ChatCompletionResult result = openAiService.createChatCompletion(completionRequest);

            String responseMessage = result.getChoices().get(0).getMessage().getContent();
            Integer tokensUsed = result.getUsage().getTotalTokens();

            log.info("Document analysis completed. Tokens used: {}", tokensUsed);

            return ChatResponse.success(responseMessage, model, tokensUsed);

        } catch (Exception e) {
            log.error("Error analyzing document: {}", e.getMessage(), e);
            throw new AIServiceException("Failed to analyze document: " + e.getMessage(), e);
        }
    }

    // Helper methods for prompt engineering

    private String buildContentGenerationPrompt(String contentType, String tone) {
        String baseTone = tone != null ? tone : "professional";

        switch (contentType.toLowerCase()) {
            case "blog":
                return "You are an expert blog writer. Create engaging, well-structured blog posts with a " + baseTone
                        + " tone.";
            case "email":
                return "You are a professional email writer. Craft clear, concise emails with a " + baseTone + " tone.";
            case "code":
                return "You are an expert programmer. Generate clean, well-documented code with explanations.";
            case "social-media":
                return "You are a social media content creator. Create engaging posts that drive engagement.";
            default:
                return "You are a professional content writer. Create high-quality content with a " + baseTone
                        + " tone.";
        }
    }

    private String buildUserContentPrompt(String contentType, String topic, String additionalInstructions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Topic: ").append(topic).append("\n\n");

        if (additionalInstructions != null && !additionalInstructions.isEmpty()) {
            prompt.append("Additional instructions: ").append(additionalInstructions).append("\n\n");
        }

        prompt.append("Please create the ").append(contentType).append(" content.");
        return prompt.toString();
    }

    private String buildDocumentAnalysisPrompt(String analysisType) {
        switch (analysisType.toLowerCase()) {
            case "summarize":
                return "You are an expert at summarizing documents. Provide concise, accurate summaries highlighting key points.";
            case "sentiment":
                return "You are a sentiment analysis expert. Analyze the emotional tone and sentiment of text.";
            case "keypoints":
                return "You are an expert at extracting key information. Identify and list the main points from documents.";
            case "qa":
                return "You are an expert at answering questions based on provided documents. Give accurate, relevant answers.";
            default:
                return "You are a document analysis expert. Analyze the provided document thoroughly.";
        }
    }

    private String buildUserAnalysisPrompt(String analysisType, String document, String question) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Document:\n").append(document).append("\n\n");

        if ("qa".equalsIgnoreCase(analysisType) && question != null) {
            prompt.append("Question: ").append(question);
        } else {
            prompt.append("Please perform the analysis.");
        }

        return prompt.toString();
    }
}
