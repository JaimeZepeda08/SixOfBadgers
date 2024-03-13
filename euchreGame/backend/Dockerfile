FROM openjdk:17-oracle

WORKDIR /backend
COPY .. .

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]

EXPOSE 8080