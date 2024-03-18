FROM ubuntu:latest

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean

WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN ./gradlew build

ENTRYPOINT ["java", "-jar", "build/libs/backend-0.0.1.jar"]
EXPOSE 8080
