package com.showcase.aiintegration.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * OpenAI Configuration
 * 
 * Configures the OpenAI API client with API key and timeout settings.
 * Supports multiple models: GPT-4, GPT-3.5-turbo
 */
@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.api.timeout:60}")
    private int timeoutSeconds;

    /**
     * Creates and configures OpenAI service client
     * 
     * @return Configured OpenAiService instance
     */
    @Bean
    public OpenAiService openAiService() {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here")) {
            System.err.println("⚠️  WARNING: OpenAI API key not configured!");
            System.err.println("Please set 'openai.api.key' in application.properties");
            // Return a service anyway to allow application to start
            // Actual API calls will fail with appropriate error messages
        }
        
        return new OpenAiService(apiKey, Duration.ofSeconds(timeoutSeconds));
    }
}
