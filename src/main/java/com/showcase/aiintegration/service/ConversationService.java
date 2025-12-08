package com.showcase.aiintegration.service;

import com.showcase.aiintegration.model.dto.ChatRequest;
import com.showcase.aiintegration.model.dto.ChatResponse;
import com.showcase.aiintegration.model.entity.Conversation;
import com.showcase.aiintegration.repository.ConversationRepository;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Conversation Service
 * 
 * Manages conversation history and memory for context-aware AI interactions.
 * Implements multi-user support and session management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final OpenAIService openAIService;

    /**
     * Process a chat message with conversation memory
     * 
     * @param request Chat request
     * @return Chat response with conversation context
     */
    @Transactional
    public ChatResponse processChatWithMemory(ChatRequest request) {
        // Generate or use existing conversation ID
        String conversationId = request.getUserId() != null
                ? request.getUserId() + "-" + UUID.randomUUID().toString()
                : UUID.randomUUID().toString();

        String userId = request.getUserId() != null ? request.getUserId() : "anonymous";

        log.info("Processing chat with memory for user: {}, conversation: {}", userId, conversationId);

        // Save user message to database
        saveMessage(userId, conversationId, "user", request.getMessage(), request.getModel());

        // Get conversation history
        List<Conversation> history = conversationRepository
                .findTop10ByConversationIdOrderByCreatedAtDesc(conversationId);

        // Build messages list for OpenAI (reverse to get chronological order)
        List<ChatMessage> messages = buildChatMessages(history, request);

        // Get AI response
        ChatResponse response = openAIService.processChatWithHistory(
                messages,
                request.getModel(),
                request.getTemperature());

        // Save AI response to database
        if (response.getSuccess()) {
            saveMessage(userId, conversationId, "assistant", response.getMessage(), request.getModel());
            response.setConversationId(conversationId);
        }

        return response;
    }

    /**
     * Continue an existing conversation
     * 
     * @param conversationId Existing conversation ID
     * @param request        Chat request
     * @return Chat response
     */
    @Transactional
    public ChatResponse continueConversation(String conversationId, ChatRequest request) {
        log.info("Continuing conversation: {}", conversationId);

        // Verify conversation exists
        List<Conversation> existingConversation = conversationRepository
                .findByConversationIdOrderByCreatedAtAsc(conversationId);

        if (existingConversation.isEmpty()) {
            throw new IllegalArgumentException("Conversation not found: " + conversationId);
        }

        String userId = existingConversation.get(0).getUserId();

        // Save user message
        saveMessage(userId, conversationId, "user", request.getMessage(), request.getModel());

        // Get conversation history (last 10 messages)
        List<Conversation> history = conversationRepository
                .findTop10ByConversationIdOrderByCreatedAtDesc(conversationId);

        // Build messages list
        List<ChatMessage> messages = buildChatMessages(history, request);

        // Get AI response
        ChatResponse response = openAIService.processChatWithHistory(
                messages,
                request.getModel(),
                request.getTemperature());

        // Save AI response
        if (response.getSuccess()) {
            saveMessage(userId, conversationId, "assistant", response.getMessage(), request.getModel());
            response.setConversationId(conversationId);
        }

        return response;
    }

    /**
     * Get conversation history for a user
     * 
     * @param userId User ID
     * @return List of conversations
     */
    public List<Conversation> getConversationHistory(String userId) {
        log.info("Retrieving conversation history for user: {}", userId);
        return conversationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Get messages for a specific conversation
     * 
     * @param conversationId Conversation ID
     * @return List of messages
     */
    public List<Conversation> getConversationMessages(String conversationId) {
        log.info("Retrieving messages for conversation: {}", conversationId);
        return conversationRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    // Helper methods

    /**
     * Save a message to the database
     */
    private void saveMessage(String userId, String conversationId, String role,
            String message, String model) {
        Conversation conversation = Conversation.builder()
                .userId(userId)
                .conversationId(conversationId)
                .role(role)
                .message(message)
                .model(model)
                .build();

        conversationRepository.save(conversation);
        log.debug("Saved {} message for conversation: {}", role, conversationId);
    }

    /**
     * Build ChatMessage list from conversation history
     */
    private List<ChatMessage> buildChatMessages(List<Conversation> history, ChatRequest request) {
        List<ChatMessage> messages = new ArrayList<>();

        // Add system prompt
        String systemPrompt = request.getSystemPrompt() != null && !request.getSystemPrompt().isEmpty()
                ? request.getSystemPrompt()
                : "You are a helpful AI assistant with conversation memory. " +
                        "Continue the conversation naturally, referring to previous context when relevant.";

        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));

        // Add conversation history (reverse chronological to chronological)
        List<Conversation> chronologicalHistory = history.stream()
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .collect(Collectors.toList());

        for (Conversation conv : chronologicalHistory) {
            String role = conv.getRole().equals("user")
                    ? ChatMessageRole.USER.value()
                    : ChatMessageRole.ASSISTANT.value();

            messages.add(new ChatMessage(role, conv.getMessage()));
        }

        return messages;
    }
}
