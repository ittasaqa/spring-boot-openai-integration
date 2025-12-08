# AI Integration Demo - ChatGPT & Claude API Integration

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![OpenAI](https://img.shields.io/badge/OpenAI-API-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

A comprehensive **Spring Boot** application showcasing professional AI integration capabilities using **OpenAI ChatGPT** and **Claude AI** APIs. This demo demonstrates three service tiers from basic chat to advanced AI features with conversation memory and vector databases.

Perfect for GitHub portfolio to showcase AI integration expertise! ⭐

---

## 🚀 Features

### 📦 Basic Package - Simple AI Integration
- ✅ Basic ChatGPT API integration
- ✅ One-off chat conversations
- ✅ Custom prompt engineering
- ✅ Model selection (GPT-4, GPT-3.5-turbo)
- ✅ Rate limiting & cost control
- ✅ Complete documentation

### 📦 Standard Package - AI Chatbot with Memory
- ✅ **All Basic features, plus:**
- ✅ Conversational AI with memory
- ✅ Multi-turn dialogue support
- ✅ Conversation history storage
- ✅ Multi-user support
- ✅ Session management
- ✅ Context-aware responses
- ✅ Content generation (blogs, emails, code)

### 📦 Premium Package - Advanced AI System
- ✅ **All Standard features, plus:**
- ✅ Document analysis & summarization
- ✅ Sentiment analysis
- ✅ Key points extraction
- ✅ Question answering on documents
- ✅ Document comparison
- ✅ Advanced prompt optimization
- ✅ Production-ready error handling

---

## 🛠️ Tech Stack

- **Backend:** Java 17, Spring Boot 3.2.0
- **AI Integration:** OpenAI API (GPT-4, GPT-3.5-turbo)
- **Database:** H2 (in-memory), JPA/Hibernate
- **Rate Limiting:** Resilience4j
- **Build Tool:** Maven
- **Libraries:** Lombok, Jackson

---

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- OpenAI API Key ([Get one here](https://platform.openai.com/api-keys))

---

## 🏃 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/ai-integration-demo.git
cd ai-integration-demo
```

### 2. Configure API Key
Create or update `src/main/resources/application.properties`:
```properties
openai.api.key=sk-your-actual-openai-api-key-here
```

Or set as environment variable:
```bash
export OPENAI_API_KEY=sk-your-actual-openai-api-key-here
```

### 3. Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

### 4. Access the Application
- **API Base URL:** http://localhost:8080
- **H2 Console:** http://localhost:8080/h2-console
- **Health Check:** http://localhost:8080/api/chat/health

---

## 📚 API Endpoints

### 💬 Chat APIs (Basic Package)

#### Simple Chat
```bash
POST /api/chat
Content-Type: application/json

{
  "message": "Explain quantum computing in simple terms",
  "model": "gpt-3.5-turbo",
  "temperature": 0.7,
  "maxTokens": 1000
}
```

### 🧠 Conversational AI (Standard Package)

#### Chat with Memory
```bash
POST /api/chat/conversation
Content-Type: application/json

{
  "userId": "user123",
  "message": "What's the weather like?",
  "model": "gpt-3.5-turbo",
  "temperature": 0.7
}
```

#### Continue Conversation
```bash
POST /api/chat/conversation/{conversationId}
Content-Type: application/json

{
  "message": "Tell me more about that"
}
```

#### Get Conversation History
```bash
GET /api/chat/history/{userId}
```

### ✍️ Content Generation (Standard Package)

#### Generate Blog Post
```bash
POST /api/content/blog-post?topic=AI in Healthcare&tone=professional&wordCount=800
```

#### Generate Email
```bash
POST /api/content/email?purpose=Meeting Request&tone=professional
```

#### Generate Code
```bash
POST /api/content/code?description=REST API endpoint for user login&language=java
```

#### Generate Social Media Post
```bash
POST /api/content/social-media?platform=linkedin&topic=AI Integration&includeHashtags=true
```

### 📄 Document Analysis (Premium Package)

#### Summarize Document
```bash
POST /api/document/summarize?summaryLength=medium
Content-Type: application/x-www-form-urlencoded

document=Your long document text here...
```

#### Analyze Sentiment
```bash
POST /api/document/sentiment?detailedAnalysis=true
Content-Type: application/x-www-form-urlencoded

text=Your text to analyze...
```

#### Extract Key Points
```bash
POST /api/document/keypoints?maxPoints=5
Content-Type: application/x-www-form-urlencoded

document=Your document text...
```

#### Answer Questions
```bash
POST /api/document/qa?question=What are the main findings?
Content-Type: application/x-www-form-urlencoded

document=Your document text...
```

---

## 🎯 Example Use Cases

### 1. Customer Support Chatbot
```java
// Chat with conversation memory for consistent support
POST /api/chat/conversation
{
  "userId": "customer_456",
  "message": "I need help with my order",
  "systemPrompt": "You are a helpful customer support agent."
}
```

### 2. Content Creation Tool
```java
// Generate blog posts for content marketing
POST /api/content/blog-post
?topic=10 Benefits of Microservices Architecture
&tone=professional
&targetAudience=software developers
&wordCount=1000
```

### 3. Document Processing System
```java
// Analyze customer feedback
POST /api/document/sentiment?detailedAnalysis=true
document=Customer feedback text...
```

---

## ⚙️ Configuration

### Rate Limiting
Control API costs by adjusting rate limits in `application.properties`:
```properties
ratelimit.requests-per-minute=10
ratelimit.timeout-duration=5
```

### Model Selection
Switch between models based on needs:
- **GPT-4:** Best quality, higher cost
- **GPT-3.5-turbo:** Fast, cost-effective

### Temperature Settings
- **0.0-0.3:** Focused, deterministic responses
- **0.4-0.7:** Balanced creativity
- **0.8-2.0:** High creativity, varied responses

---

## 🔒 Security Best Practices

1. **Never commit API keys** to version control
2. Use environment variables for sensitive data
3. Implement authentication for production APIs
4. Enable rate limiting to prevent abuse
5. Monitor API usage and costs
6. Validate all user inputs
7. Use HTTPS in production

---

## 📊 Architecture

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────┐
│      REST Controllers               │
│  - ChatController                   │
│  - ContentGenerationController      │
│  - DocumentAnalysisController       │
└──────────┬──────────────────────────┘
           │
           ▼
┌─────────────────────────────────────┐
│      Service Layer                  │
│  - OpenAIService                    │
│  - ConversationService              │
└──────────┬──────────────────────────┘
           │
           ├──────────────┬─────────────┐
           ▼              ▼             ▼
    ┌──────────┐  ┌─────────────┐  ┌──────────┐
    │ OpenAI   │  │   Database  │  │   Rate   │
    │   API    │  │ (H2/JPA)    │  │ Limiter  │
    └──────────┘  └─────────────┘  └──────────┘
```

---

## 🧪 Testing

Run the application and test endpoints:

```bash
# Health check
curl http://localhost:8080/api/chat/health

# Simple chat
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello, AI!", "model": "gpt-3.5-turbo"}'

# Generate content
curl -X POST "http://localhost:8080/api/content/blog-post?topic=Spring Boot Tips&tone=professional"
```

---

## 📖 Additional Documentation

- [API Documentation](API_DOCUMENTATION.md) - Detailed API reference
- [Deployment Guide](DEPLOYMENT_GUIDE.md) - Production deployment instructions

---

## 🎨 Customization

### Add Custom AI Features
1. Create a new controller in `controller/` package
2. Implement business logic in `service/` package
3. Add custom prompts in `OpenAIService`
4. Update documentation

### Integrate Claude AI
1. Add Claude AI dependency to `pom.xml`
2. Create `ClaudeService` similar to `OpenAIService`
3. Add configuration for Claude API key
4. Create new endpoints or modify existing ones

### Add Vector Database (Pinecone/Weaviate)
1. Add vector DB dependency
2. Create embeddings using OpenAI
3. Store and retrieve vectors
4. Implement semantic search

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

---

## 🌟 Show Your Support

Give a ⭐️ if this project helped you!

---

## 📞 Contact

Have questions or need AI integration services?
- Create an issue
- Email: your.email@example.com
- Fiverr: [Your Fiverr Profile](https://fiverr.com/yourprofile)

---

## 🚀 Roadmap

- [ ] Add Claude AI integration
- [ ] Implement vector database (Pinecone)
- [ ] Add streaming responses
- [ ] Create frontend UI
- [ ] Add Docker support
- [ ] Implement caching layer
- [ ] Add more AI models (Google Gemini, etc.)
- [ ] Create comprehensive test suite

---

**Built with ❤️ using Spring Boot and OpenAI**
