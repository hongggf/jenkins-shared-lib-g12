FROM gradle:8-jdk17 AS builder

WORKDIR /app

# Copy Gradle files
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle ./gradle

# Make Gradle wrapper executable
RUN chmod +x gradlew

# Copy source code
COPY src ./src

# Build application
RUN ./gradlew build -x test --no-daemon


FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]