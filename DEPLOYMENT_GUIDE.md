# Deployment Guide

Step-by-step guide to deploy the AI Integration Demo application.

---

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- OpenAI API Key
- (Optional) Docker for containerization
- (Optional) Cloud platform account (AWS, Azure, GCP, Heroku)

---

## 🏠 Local Development Setup

### 1. Clone Repository
```bash
git clone https://github.com/yourusername/ai-integration-demo.git
cd ai-integration-demo
```

### 2. Configure Environment
```bash
# Copy example configuration
cp src/main/resources/application-example.properties src/main/resources/application.properties

# Edit application.properties and add your OpenAI API key
nano src/main/resources/application.properties
```

### 3. Build Application
```bash
mvn clean install
```

### 4. Run Application
```bash
mvn spring-boot:run
```

### 5. Verify
```bash
curl http://localhost:8080/api/chat/health
```

---

## 🐳 Docker Deployment

### 1. Create Dockerfile
Create `Dockerfile` in project root:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/ai-integration-demo-1.0.0.jar app.jar

EXPOSE 8080

ENV OPENAI_API_KEY=""
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### 2. Create .dockerignore
```
target/
.mvn/
*.iml
.idea/
.git/
```

### 3. Build Docker Image
```bash
docker build -t ai-integration-demo:latest .
```

### 4. Run Docker Container
```bash
docker run -d \
  -p 8080:8080 \
  -e OPENAI_API_KEY=your-api-key-here \
  --name ai-integration-demo \
  ai-integration-demo:latest
```

### 5. Check Logs
```bash
docker logs -f ai-integration-demo
```

---

## ☁️ Cloud Deployment

### AWS Elastic Beanstalk

#### 1. Install EB CLI
```bash
pip install awsebcli
```

#### 2. Initialize EB
```bash
eb init -p "Corretto 17" ai-integration-demo --region us-east-1
```

#### 3. Set Environment Variables
```bash
eb setenv OPENAI_API_KEY=your-api-key-here
```

#### 4. Deploy
```bash
mvn clean package
eb create ai-integration-demo-env
eb deploy
```

#### 5. Open Application
```bash
eb open
```

---

### Heroku

#### 1. Install Heroku CLI
```bash
# macOS
brew tap heroku/brew && brew install heroku

# Windows
# Download from heroku.com
```

#### 2. Login
```bash
heroku login
```

#### 3. Create App
```bash
heroku create ai-integration-demo
```

#### 4. Set Environment Variables
```bash
heroku config:set OPENAI_API_KEY=your-api-key-here
```

#### 5. Create Procfile
```
web: java -Dserver.port=$PORT -jar target/ai-integration-demo-1.0.0.jar
```

#### 6. Deploy
```bash
git add .
git commit -m "Deploy to Heroku"
git push heroku main
```

#### 7. Open Application
```bash
heroku open
```

---

### Azure App Service

#### 1. Install Azure CLI
```bash
# macOS
brew install azure-cli

# Windows
# Download from azure.microsoft.com
```

#### 2. Login
```bash
az login
```

#### 3. Create Resource Group
```bash
az group create --name ai-integration-rg --location eastus
```

#### 4. Create App Service Plan
```bash
az appservice plan create \
  --name ai-integration-plan \
  --resource-group ai-integration-rg \
  --sku B1 \
  --is-linux
```

#### 5. Create Web App
```bash
az webapp create \
  --resource-group ai-integration-rg \
  --plan ai-integration-plan \
  --name ai-integration-demo \
  --runtime "JAVA:17-java17"
```

#### 6. Configure Environment
```bash
az webapp config appsettings set \
  --resource-group ai-integration-rg \
  --name ai-integration-demo \
  --settings OPENAI_API_KEY=your-api-key-here
```

#### 7. Deploy
```bash
mvn clean package
az webapp deploy \
  --resource-group ai-integration-rg \
  --name ai-integration-demo \
  --src-path target/ai-integration-demo-1.0.0.jar \
  --type jar
```

---

### Google Cloud Platform (GCP)

#### 1. Install gcloud CLI
```bash
# Follow instructions at cloud.google.com/sdk
```

#### 2. Login and Set Project
```bash
gcloud auth login
gcloud config set project your-project-id
```

#### 3. Create app.yaml
```yaml
runtime: java17
env: standard

env_variables:
  OPENAI_API_KEY: "your-api-key-here"

automatic_scaling:
  min_instances: 1
  max_instances: 10
```

#### 4. Deploy
```bash
mvn clean package appengine:deploy
```

---

## 🔒 Production Configuration

### 1. Security

#### Update application.properties
```properties
# Disable H2 Console in production
spring.h2.console.enabled=false

# Use production database (PostgreSQL example)
spring.datasource.url=jdbc:postgresql://localhost:5432/aidb
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Enable HTTPS
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12
```

#### Add Authentication
Consider adding Spring Security:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 2. Database Migration

#### Switch to PostgreSQL
```xml
<!-- Add to pom.xml -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

```properties
# application-prod.properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}:5432/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
```

### 3. Monitoring

#### Add Actuator
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```properties
# Enable health and metrics endpoints
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=when-authorized
```

### 4. Logging

```properties
# Production logging
logging.level.root=WARN
logging.level.com.showcase.aiintegration=INFO
logging.file.name=/var/log/ai-integration/application.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## 📊 Performance Optimization

### 1. JVM Tuning
```bash
java -Xms512m -Xmx2g -XX:+UseG1GC -jar app.jar
```

### 2. Connection Pooling
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

### 3. Caching
Add Redis for caching:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

---

## 🔄 CI/CD Pipeline

### GitHub Actions Example

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Build with Maven
      run: mvn clean package -DskipTests
    
    - name: Build Docker image
      run: docker build -t ai-integration-demo:${{ github.sha }} .
    
    - name: Push to Docker Hub
      run: |
        echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --password-stdin
        docker tag ai-integration-demo:${{ github.sha }} yourusername/ai-integration-demo:latest
        docker push yourusername/ai-integration-demo:latest
    
    - name: Deploy to Kubernetes
      run: kubectl set image deployment/ai-integration-demo ai-integration-demo=yourusername/ai-integration-demo:${{ github.sha }}
```

---

## 🧪 Health Checks

### Production Health Endpoints
```bash
# Application health
curl https://your-domain.com/actuator/health

# Custom health check
curl https://your-domain.com/api/chat/health
```

---

## 📈 Scaling Considerations

### Horizontal Scaling
- Use load balancers (AWS ELB, Azure Load Balancer)
- Session-less architecture (current implementation)
- Database connection pooling

### Vertical Scaling
- Increase JVM heap size
- More CPU cores for parallel processing
- Faster disk I/O for logging

### Cost Management
- Monitor OpenAI API usage
- Implement caching for repeated queries
- Use GPT-3.5-turbo for most requests
- Set up billing alerts

---

## 🔐 Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `OPENAI_API_KEY` | OpenAI API key | Yes |
| `DB_HOST` | Database host | Production |
| `DB_NAME` | Database name | Production |
| `DB_USERNAME` | Database username | Production |
| `DB_PASSWORD` | Database password | Production |
| `RATE_LIMIT_RPM` | Requests per minute | No |
| `PORT` | Application port | No (default: 8080) |

---

## 📞 Support

For deployment issues:
- GitHub Issues: [Create Issue](https://github.com/yourusername/ai-integration-demo/issues)
- Email: your.email@example.com

---

**Happy Deploying! 🚀**
