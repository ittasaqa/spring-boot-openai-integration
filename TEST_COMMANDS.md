# Quick Test Commands

# Save this file and use these commands to quickly test your AI Integration Demo

# ==========================================
# IMPORTANT: Start the application first!
# ==========================================
# mvn spring-boot:run

# ==========================================
# 1. HEALTH CHECKS
# ==========================================

# Chat service health
curl http://localhost:8080/api/chat/health

# Content generation health
curl http://localhost:8080/api/content/health

# Document analysis health
curl http://localhost:8080/api/document/health


# ==========================================
# 2. BASIC CHAT (Basic Package)
# ==========================================

# Simple chat request
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d "{\"message\":\"Explain what Spring Boot is in 3 sentences\",\"model\":\"gpt-3.5-turbo\"}"

# Chat with custom temperature
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d "{\"message\":\"Write a creative story about a robot\",\"model\":\"gpt-3.5-turbo\",\"temperature\":1.2}"

# Chat with system prompt
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d "{\"message\":\"How do I make pasta?\",\"model\":\"gpt-3.5-turbo\",\"systemPrompt\":\"You are a professional Italian chef.\"}"


# ==========================================
# 3. CONVERSATIONAL CHAT (Standard Package)
# ==========================================

# Start a conversation
curl -X POST http://localhost:8080/api/chat/conversation \
  -H "Content-Type: application/json" \
  -d "{\"userId\":\"testuser123\",\"message\":\"My name is Alice and I love programming\"}"

# Continue the conversation (AI will remember)
curl -X POST http://localhost:8080/api/chat/conversation \
  -H "Content-Type: application/json" \
  -d "{\"userId\":\"testuser123\",\"message\":\"What is my name and what do I love?\"}"

# Get conversation history
curl http://localhost:8080/api/chat/history/testuser123


# ==========================================
# 4. CONTENT GENERATION (Standard Package)
# ==========================================

# Generate a blog post
curl -X POST "http://localhost:8080/api/content/blog-post?topic=Benefits of Microservices Architecture&tone=professional&wordCount=500"

# Generate a professional email
curl -X POST "http://localhost:8080/api/content/email?purpose=Request a meeting to discuss project roadmap&tone=professional&recipient=Project Manager"

# Generate Java code
curl -X POST "http://localhost:8080/api/content/code?description=Create a REST controller for user CRUD operations&language=java&includeComments=true"

# Generate a LinkedIn post
curl -X POST "http://localhost:8080/api/content/social-media?platform=linkedin&topic=AI Integration in Modern Applications&includeHashtags=true"

# Generate a tweet
curl -X POST "http://localhost:8080/api/content/social-media?platform=twitter&topic=Just learned Spring Boot&includeHashtags=true"

# General content generation
curl -X POST http://localhost:8080/api/content/generate \
  -H "Content-Type: application/json" \
  -d "{\"contentType\":\"blog\",\"topic\":\"Top 10 Java Best Practices\",\"tone\":\"professional\",\"wordCount\":800}"


# ==========================================
# 5. DOCUMENT ANALYSIS (Premium Package)
# ==========================================

# Summarize a document
curl -X POST "http://localhost:8080/api/document/summarize?summaryLength=medium" \
  --data-urlencode "document=Spring Boot is an open-source Java framework used to create standalone, production-grade Spring-based applications. It simplifies the development process by providing auto-configuration, embedded servers, and production-ready features. Developers can quickly build microservices and web applications without extensive XML configuration. The framework includes starter dependencies that bundle commonly used libraries together."

# Analyze sentiment
curl -X POST "http://localhost:8080/api/document/sentiment?detailedAnalysis=true" \
  --data-urlencode "text=I absolutely love this product! It exceeded all my expectations and the customer service was amazing. Highly recommended!"

# Extract key points
curl -X POST "http://localhost:8080/api/document/keypoints?maxPoints=5" \
  --data-urlencode "document=The quarterly meeting covered several important topics. First, revenue increased by 25% compared to last quarter. Second, the new product launch is scheduled for next month. Third, we are hiring 10 new engineers. Fourth, customer satisfaction scores improved significantly. Finally, the new office location will open in December."

# Question answering
curl -X POST "http://localhost:8080/api/document/qa?question=What is the revenue increase?" \
  --data-urlencode "document=The quarterly meeting covered several important topics. Revenue increased by 25% compared to last quarter. The new product launch is scheduled for next month."

# Compare documents
curl -X POST "http://localhost:8080/api/document/compare?focusOn=differences" \
  --data-urlencode "document1=Spring Boot is a framework for building Java applications with minimal configuration." \
  --data-urlencode "document2=Spring Boot is a tool that makes Java development faster by providing automatic configuration."


# ==========================================
# 6. ADVANCED EXAMPLES
# ==========================================

# Code generation with GPT-4 (better quality)
curl -X POST "http://localhost:8080/api/content/code?description=Implement a binary search algorithm with error handling&language=python&includeComments=true"

# Long-form content
curl -X POST "http://localhost:8080/api/content/blog-post?topic=Complete Guide to RESTful API Design&tone=professional&wordCount=1500"

# Multiple language code example
curl -X POST "http://localhost:8080/api/content/code?description=Create a function to validate email addresses&language=javascript&includeComments=true"


# ==========================================
# NOTES:
# ==========================================
# 1. Make sure to replace 'your-api-key-here' in application.properties with your actual OpenAI API key
# 2. The application must be running (mvn spring-boot:run) before testing
# 3. For Windows PowerShell, you may need to escape quotes differently or save JSON to a file
# 4. Rate limiting is set to 10 requests per minute by default
# 5. All responses include token usage for cost tracking
# 6. Conversation history is stored in H2 database (viewable at http://localhost:8080/h2-console)
