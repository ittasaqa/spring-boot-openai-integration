package com.showcase.aiintegration.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Conversation Entity
 * 
 * JPA entity for storing conversation history with memory support
 */
@Entity
@Table(name = "conversations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String conversationId;

    @Column(nullable = false)
    private String role; // "user" or "assistant"

    @Column(nullable = false, length = 4000)
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private Integer tokensUsed;

    private String model;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
