# install ubuntu base image
FROM ubuntu:latest

# Install necessary dependencies
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean

# copy all of the files in backend to a docker directory /api
COPY . /api

# change docker working directory to /api
WORKDIR /api

RUN chmod +x gradlewri
# build gradle app with gradlew (gradle wrapper)
RUN ./gradlew

ENTRYPOINT ["java", "-jar", "build/libs/backend-0.0.1.jar"]
EXPOSE 8080
