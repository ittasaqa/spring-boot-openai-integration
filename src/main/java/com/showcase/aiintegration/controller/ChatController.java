package com.showcase.aiintegration.controller;

import com.showcase.aiintegration.model.dto.ChatRequest;
import com.showcase.aiintegration.model.dto.ChatResponse;
import com.showcase.aiintegration.model.entity.Conversation;
import com.showcase.aiintegration.service.ConversationService;
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
 * Chat Controller
 * 
 * REST API endpoints for AI chat functionality.
 * Supports both simple one-off chats and conversation-based chats with memory.
 * 
 * Package Tier: Basic (simple chat), Standard (chat with memory)
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final OpenAIService openAIService;
    private final ConversationService conversationService;

    /**
     * Simple chat endpoint - Basic Package Feature
     * 
     * POST /api/chat
     * One-off chat without conversation history
     */
    @PostMapping
    public ResponseEntity<ChatResponse> simpleChat(@Valid @RequestBody ChatRequest request) {
        log.info("Received simple chat request");
        ChatResponse response = openAIService.processChat(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Chat with conversation memory - Standard Package Feature
     * 
     * POST /api/chat/conversation
     * Chat with full conversation history and context awareness
     */
    @PostMapping("/conversation")
    public ResponseEntity<ChatResponse> chatWithMemory(@Valid @RequestBody ChatRequest request) {
        log.info("Received chat request with memory for user: {}", request.getUserId());
        ChatResponse response = conversationService.processChatWithMemory(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Continue existing conversation - Standard Package Feature
     * 
     * POST /api/chat/conversation/{conversationId}
     * Continue a specific conversation thread
     */
    @PostMapping("/conversation/{conversationId}")
    public ResponseEntity<ChatResponse> continueConversation(
            @PathVariable String conversationId,
            @Valid @RequestBody ChatRequest request) {
        log.info("Continuing conversation: {}", conversationId);
        ChatResponse response = conversationService.continueConversation(conversationId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get conversation history for a user - Standard Package Feature
     * 
     * GET /api/chat/history/{userId}
     * Retrieve all conversations for a specific user
     */
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Conversation>> getConversationHistory(@PathVariable String userId) {
        log.info("Retrieving conversation history for user: {}", userId);
        List<Conversation> history = conversationService.getConversationHistory(userId);
        return ResponseEntity.ok(history);
    }

    /**
     * Get specific conversation messages - Standard Package Feature
     * 
     * GET /api/chat/conversation/{conversationId}/messages
     * Retrieve all messages in a specific conversation
     */
    @GetMapping("/conversation/{conversationId}/messages")
    public ResponseEntity<List<Conversation>> getConversationMessages(@PathVariable String conversationId) {
        log.info("Retrieving messages for conversation: {}", conversationId);
        List<Conversation> messages = conversationService.getConversationMessages(conversationId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Health check endpoint
     * 
     * GET /api/chat/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "AI Chat Service");
        health.put("features", List.of(
                "Simple Chat (Basic Package)",
                "Chat with Memory (Standard Package)",
                "Multi-user Support",
                "Conversation History"));
        return ResponseEntity.ok(health);
    }
}
