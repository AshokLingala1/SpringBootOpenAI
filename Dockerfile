# Use Maven to build the project
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use a smaller JDK image to run the app
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Set environment variable for the OpenAI key
ENV SPRING_AI_OPENAI_API_KEY=${SPRING_AI_OPENAI_API_KEY}

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
