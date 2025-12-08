package com.showcase.aiintegration.repository;

import com.showcase.aiintegration.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Conversation Repository
 * 
 * Data access layer for conversation history
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * Find all conversations for a specific conversation ID
     */
    List<Conversation> findByConversationIdOrderByCreatedAtAsc(String conversationId);

    /**
     * Find all conversations for a user
     */
    List<Conversation> findByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * Find recent conversations for a specific conversation
     */
    List<Conversation> findTop10ByConversationIdOrderByCreatedAtDesc(String conversationId);
}
