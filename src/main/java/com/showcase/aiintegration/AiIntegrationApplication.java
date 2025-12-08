package com.showcase.aiintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI Integration Demo Application
 * 
 * Professional showcase of ChatGPT/Claude AI integration with Java Spring Boot.
 * Demonstrates multiple AI capabilities from basic chat to advanced conversational AI.
 * 
 * Features:
 * - Basic ChatGPT Integration
 * - Conversational AI with Memory
 * - Content Generation
 * - Document Analysis
 * - Rate Limiting & Cost Control
 * 
 * @author AI Integration Specialist
 */
@SpringBootApplication
public class AiIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiIntegrationApplication.class, args);
        System.out.println("🚀 AI Integration Demo Application Started Successfully!");
        System.out.println("📚 API Documentation: http://localhost:8080/api/health");
        System.out.println("💡 Ensure OpenAI API key is configured in application.properties");
    }
}
