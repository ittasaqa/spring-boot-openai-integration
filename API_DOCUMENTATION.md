# API Documentation

Comprehensive API reference for the AI Integration Demo application.

---

## Base URL
```
http://localhost:8080
```

---

## 🔐 Authentication

Currently, this demo does not require authentication. For production use, implement:
- JWT tokens
- API keys
- OAuth 2.0

---

## 📚 API Reference

### 1. Chat APIs

#### 1.1 Simple Chat (Basic Package)

**Endpoint:** `POST /api/chat`

**Description:** One-off chat without conversation history.

**Request Body:**
```json
{
  "message": "string (required, max 4000 chars)",
  "userId": "string (optional)",
  "model": "string (optional, default: gpt-3.5-turbo)",
  "temperature": "number (optional, 0.0-2.0, default: 0.7)",
  "maxTokens": "integer (optional, default: 1000)",
  "systemPrompt": "string (optional)"
}
```

**Response:**
```json
{
  "message": "AI generated response",
  "model": "gpt-3.5-turbo",
  "tokensUsed": 150,
  "timestamp": "2024-01-08T12:00:00",
  "success": true
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Explain microservices architecture",
    "model": "gpt-3.5-turbo",
    "temperature": 0.7
  }'
```

---

#### 1.2 Chat with Memory (Standard Package)

**Endpoint:** `POST /api/chat/conversation`

**Description:** Chat with conversation history and context awareness.

**Request Body:**
```json
{
  "userId": "string (required for memory)",
  "message": "string (required)",
  "model": "string (optional)",
  "temperature": "number (optional)",
  "systemPrompt": "string (optional)"
}
```

**Response:**
```json
{
  "message": "AI response with context",
  "model": "gpt-3.5-turbo",
  "tokensUsed": 200,
  "timestamp": "2024-01-08T12:00:00",
  "conversationId": "user123-uuid",
  "success": true
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/chat/conversation \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "message": "What is API rate limiting?"
  }'
```

---

#### 1.3 Continue Conversation

**Endpoint:** `POST /api/chat/conversation/{conversationId}`

**Description:** Continue an existing conversation thread.

**Path Parameters:**
- `conversationId` - ID of the conversation to continue

**Request Body:**
```json
{
  "message": "string (required)"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/chat/conversation/user123-abc-123 \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Can you explain more about that?"
  }'
```

---

#### 1.4 Get Conversation History

**Endpoint:** `GET /api/chat/history/{userId}`

**Description:** Retrieve all conversations for a user.

**Path Parameters:**
- `userId` - User identifier

**Response:**
```json
[
  {
    "id": 1,
    "userId": "user123",
    "conversationId": "user123-abc-123",
    "role": "user",
    "message": "What is API rate limiting?",
    "createdAt": "2024-01-08T12:00:00",
    "model": "gpt-3.5-turbo"
  },
  {
    "id": 2,
    "userId": "user123",
    "conversationId": "user123-abc-123",
    "role": "assistant",
    "message": "API rate limiting is...",
    "createdAt": "2024-01-08T12:00:01",
    "tokensUsed": 200
  }
]
```

---

### 2. Content Generation APIs

#### 2.1 Generate Blog Post

**Endpoint:** `POST /api/content/blog-post`

**Query Parameters:**
- `topic` (required) - Blog post topic
- `tone` (optional, default: professional) - Writing tone
- `targetAudience` (optional) - Target audience
- `wordCount` (optional, default: 800) - Approximate word count

**Example:**
```bash
curl -X POST "http://localhost:8080/api/content/blog-post?topic=AI in Healthcare&tone=professional&wordCount=1000"
```

---

#### 2.2 Generate Email

**Endpoint:** `POST /api/content/email`

**Query Parameters:**
- `purpose` (required) - Email purpose/subject
- `tone` (optional, default: professional)
- `recipient` (optional) - Recipient name/role
- `additionalContext` (optional)

**Example:**
```bash
curl -X POST "http://localhost:8080/api/content/email?purpose=Project Status Update&tone=professional&recipient=Team"
```

---

#### 2.3 Generate Code

**Endpoint:** `POST /api/content/code`

**Query Parameters:**
- `description` (required) - What the code should do
- `language` (optional, default: java) - Programming language
- `includeComments` (optional, default: true)
- `includeExplanation` (optional, default: true)

**Example:**
```bash
curl -X POST "http://localhost:8080/api/content/code?description=REST endpoint for user registration&language=java"
```

---

#### 2.4 Generate Social Media Post

**Endpoint:** `POST /api/content/social-media`

**Query Parameters:**
- `platform` (required) - twitter, linkedin, instagram, facebook
- `topic` (required) - Post topic
- `tone` (optional, default: engaging)
- `includeHashtags` (optional, default: false)

**Example:**
```bash
curl -X POST "http://localhost:8080/api/content/social-media?platform=linkedin&topic=Spring Boot Best Practices&includeHashtags=true"
```

---

#### 2.5 General Content Generation

**Endpoint:** `POST /api/content/generate`

**Request Body:**
```json
{
  "contentType": "string (required: blog, email, code, social-media, custom)",
  "topic": "string (required)",
  "tone": "string (optional)",
  "targetAudience": "string (optional)",
  "wordCount": "integer (optional)",
  "additionalInstructions": "string (optional)",
  "model": "string (optional, default: gpt-3.5-turbo)"
}
```

---

### 3. Document Analysis APIs

#### 3.1 Summarize Document

**Endpoint:** `POST /api/document/summarize`

**Query Parameters:**
- `document` (required, form data) - Document text
- `summaryLength` (optional: short, medium, long)
- `model` (optional)

**Example:**
```bash
curl -X POST "http://localhost:8080/api/document/summarize?summaryLength=medium" \
  --data-urlencode "document=Your long document text here..."
```

---

#### 3.2 Analyze Sentiment

**Endpoint:** `POST /api/document/sentiment`

**Query Parameters:**
- `text` (required, form data)
- `detailedAnalysis` (optional, default: false)
- `model` (optional)

**Example:**
```bash
curl -X POST "http://localhost:8080/api/document/sentiment?detailedAnalysis=true" \
  --data-urlencode "text=Customer feedback text here..."
```

---

#### 3.3 Extract Key Points

**Endpoint:** `POST /api/document/keypoints`

**Query Parameters:**
- `document` (required, form data)
- `maxPoints` (optional, default: 5)
- `model` (optional)

**Example:**
```bash
curl -X POST "http://localhost:8080/api/document/keypoints?maxPoints=7" \
  --data-urlencode "document=Meeting notes or document..."
```

---

#### 3.4 Question Answering

**Endpoint:** `POST /api/document/qa`

**Query Parameters:**
- `document` (required, form data)
- `question` (required)
- `model` (optional)

**Example:**
```bash
curl -X POST "http://localhost:8080/api/document/qa?question=What are the main conclusions?" \
  --data-urlencode "document=Research paper text..."
```

---

#### 3.5 Compare Documents

**Endpoint:** `POST /api/document/compare`

**Query Parameters:**
- `document1` (required, form data)
- `document2` (required, form data)
- `focusOn` (optional: differences, similarities)
- `model` (optional)

---

#### 3.6 General Document Analysis

**Endpoint:** `POST /api/document/analyze`

**Request Body:**
```json
{
  "document": "string (required, max 10000 chars)",
  "analysisType": "string (required: summarize, sentiment, keypoints, qa)",
  "question": "string (optional, for Q&A)",
  "maxSummaryLength": "integer (optional)",
  "model": "string (optional)"
}
```

---

## 🔄 Response Format

### Success Response
```json
{
  "message": "AI generated content",
  "model": "gpt-3.5-turbo",
  "tokensUsed": 150,
  "timestamp": "2024-01-08T12:00:00",
  "conversationId": "optional-conversation-id",
  "success": true
}
```

### Error Response
```json
{
  "success": false,
  "error": "Error message description",
  "timestamp": "2024-01-08T12:00:00",
  "status": 500
}
```

### Validation Error Response
```json
{
  "success": false,
  "error": "Validation failed",
  "validationErrors": {
    "message": "Message cannot be empty",
    "field2": "Error message"
  },
  "timestamp": "2024-01-08T12:00:00",
  "status": 400
}
```

---

## ⚠️ Error Codes

| Status Code | Description |
|------------|-------------|
| 200 | Success |
| 400 | Bad Request - Validation error |
| 429 | Too Many Requests - Rate limit exceeded |
| 500 | Internal Server Error - AI service error |

---

## 💡 Best Practices

### 1. Rate Limiting
- Default: 10 requests per minute
- Plan your API calls accordingly
- Implement exponential backoff for retries

### 2. Model Selection
- **GPT-3.5-turbo**: Fast, cost-effective for most tasks
- **GPT-4**: Use for complex reasoning, better code generation

### 3. Temperature Settings
- **Low (0.0-0.3)**: Factual, deterministic responses
- **Medium (0.4-0.7)**: Balanced
- **High (0.8-2.0)**: Creative content

### 4. Token Management
- Monitor `tokensUsed` in responses
- Adjust `maxTokens` to control costs
- Shorter prompts = lower costs

### 5. Conversation Memory
- Use `userId` consistently for better memory
- Conversations auto-store last 10 messages
- Clear old conversations periodically

---

## 🔧 Postman Collection

Import this JSON into Postman for easy testing:

```json
{
  "info": {
    "name": "AI Integration Demo",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Simple Chat",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\"message\": \"Hello AI!\", \"model\": \"gpt-3.5-turbo\"}"
        },
        "url": "http://localhost:8080/api/chat"
      }
    }
  ]
}
```

---

## 📞 Support

For issues or questions:
- GitHub Issues: [Create an issue](https://github.com/yourusername/ai-integration-demo/issues)
- Email: your.email@example.com
