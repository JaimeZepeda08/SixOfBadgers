# Use the latest Ubuntu image as the base
FROM ubuntu:latest

# Update package lists and install OpenJDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean

# Set working directory for the application
WORKDIR /api

# Copy Gradle wrapper and necessary files for building
COPY ./gradlew /api/

COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY config /api/config
COPY src ./src

# Build the application using Gradle
RUN ./gradlew build

# Set the entry point to run the JAR file and expose port 8080
ENTRYPOINT ["java", "-jar", "build/libs/backend-0.0.1.jar"]
EXPOSE 8080
