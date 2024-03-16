FROM openjdk:17
# copy xargs - file used by gradle
COPY xargs /usr/bin/

# copy all of the files in backend to a docker directory /app
COPY . /app

# change docker working directory to /app
WORKDIR /app

# build gradle app with gradlew (gradle wrapper)
RUN ./gradlew build

ENTRYPOINT ["java", "-jar", "build/libs/backend-0.0.1.jar"]
EXPOSE 8080
