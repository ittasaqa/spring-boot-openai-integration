package com.showcase.aiintegration.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Rate Limiting Configuration
 * 
 * Implements rate limiting to control API costs and prevent abuse.
 * Configurable limits per user/endpoint to manage OpenAI API usage.
 */
@Configuration
public class RateLimitConfig {

    @Value("${ratelimit.requests-per-minute:10}")
    private int requestsPerMinute;

    @Value("${ratelimit.timeout-duration:5}")
    private int timeoutDuration;

    /**
     * Configures rate limiter for AI API calls
     * 
     * @return RateLimiter instance with configured limits
     */
    @Bean
    public RateLimiter aiRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(requestsPerMinute)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofSeconds(timeoutDuration))
                .build();

        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        return registry.rateLimiter("aiApiLimiter");
    }
}
