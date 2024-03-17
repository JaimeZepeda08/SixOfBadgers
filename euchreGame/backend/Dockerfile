# install ubuntu base image
FROM ubuntu:latest

# Install necessary dependencies
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean

# copy all of the files in backend to a docker directory /app
COPY . /api

# change docker working directory to /app
WORKDIR /api

# build gradle app with gradlew (gradle wrapper)
RUN ./gradlew build

ENTRYPOINT ["java", "-jar", "build/libs/backend-0.0.1.jar"]
EXPOSE 8080
