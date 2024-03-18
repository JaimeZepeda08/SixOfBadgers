# 6 of Badgers - Docker Documentation

This project contains the following files related to Docker:

- `euchreGame/docker-compose.yml`
- `euchreGame/backend/Dockerfile`
- `euchreGame/database/Dockerfile`
- `euchreGame/frontend/Dockerfile`

## Docker Compose

Docker Compose is used to define and run multi-container applications.

```
version: "3.8"
services:
  frontend:
    build: ./frontend
    restart: unless-stopped
    container_name: frontend
    ports:
      - "3000:3000"
    links:
      - backend
    volumes:
      - ./frontend:/app
  backend:
    build: ./backend
    container_name: backend
    restart: unless-stopped
    ports:
      - "8080:8080"
    environment:
      - SPRING.PROFILES.ACTIVE=default
    depends_on:
      - euchreDatabase
    volumes:
      - ./backend:/api
  euchreDatabase:
    build: ./database
    container_name: database
    image: mysql/mysql-server:latest
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: supersecure
      MYSQL_DATABASE: euchre
      MYSQL_ROOT_HOST: '%'
    volumes:
      - euchredb:/var/lib/mysql
    ports:
      - 53306:3306

volumes:
  euchredb:
```

### Microservices

This app contains 3 microservices: frontend, backend, and euchreDatabase.

Each of these is in charge of building its corresponding image, setting up any environment variables, volumes, and managing ports.

### Self Healing Containers

Containers can have restart policies defined. These tell Docker when to run the containers in the app. All three services in this app have `restart: unless-stopped` as their restart policy. This means that the container will start on its own, unless explicitly stopped.

### Volumes

Volumes provide a way to share persistent data between Docker containers and the Docker host (your computer).

There are 3 main volumes in this app:

#### Backend Volume

```
{...}
    volumes:
      - ./backend:/api
{...}
```

This volume links the local directory `backend` to a directory named `api` in the Docker container. This means that any changes made locally inside `/backend` will be reflected in the container.

#### Frontend Volume

```
{...}
    volumes:
      - ./frontend:/app
{...}
```

This volume links the local directory `frontend` to a directory named `app` in the Docker container. This means that any changes made locally inside `/frontend` will be reflected in the container.

#### Database Volume

```
{...}
    volumes:
      - euchredb:/var/lib/mysql
{...}
volumes:
  euchredb:
```

The database volume is defined by the `docker-compose.yml` with a specified name `euchredb`, this allows the volume to be referenced by other containers and microservices.

Then, inside the database microservice, the volume `euchredb` is mounted into the container at the path `/var/lib/mysql`, allowing it to connect to the MySQL database.

## Dockerfile

There are 3 Dockerfiles in this project. Each of them specifies how the image for a specific microservice should be built.

Dockerfiles are templates used by Docker to create images. They work by adding layers on top of other layers.

These layers allow Docker to only add the layers that changed when building the image, and use cached data for the rest.

### Backend Dockerfile

```
FROM ubuntu:latest
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean
COPY . /api
WORKDIR /api
RUN ./gradlew build
ENTRYPOINT ["java", "-jar", "build/libs/backend-0.0.1.jar"]
EXPOSE 8080
```

The first thing that Docker will do is download `ubuntu:latest` as the base image. This will create a new layer containing just the necessary parts of the OS.

The next layer will install Java-17, and update the dependencies.

All the source code will then get copied into another layer and into a directory named `/api`. This will also be set as the working directory for this Docker image.

Next, Docker will use `gradlew` (a lightweight executable gradle wrapper) to build the java app and create the `.jar` file.

The `ENTRYPOINT` variable tells Docker what commands to run first when the image is built into a container. In this case, it will run the `.jar` file created in the previous step.

> Port 8080

### Frontend Dockerfile

```
FROM node:18-alpine
COPY . /app
COPY package.json ./
COPY package-lock.json ./
WORKDIR /app
RUN npm i
ENTRYPOINT [ "npm", "run", "dev" ]
EXPOSE 3000
```

The base image and first layer for the frontend will be `node:18-alpine`.

All the source code and dependency information is copied into a new directory called `/app`.

The packages and dependencies listed in `package.json` are then installed using `npm i`.

The `ENTRYPOINT` variable tells Docker what commands to run first when the image is built into a container. In this case, it will run `npm run dev`.

> Port: 3000

### Database Dockerfile

```
FROM mysql
RUN chown -R mysql:root /var/lib/mysql/
ENV MYSQL_DATABASE=euchre
ENV MYSQL_ROOT_PASSWORD=supersecure
COPY ./euchre_schema.sql /docker-entrypoint-initdb.d
EXPOSE 3306
```

First the base image is specified. We are using the official MySQL image from Docker Hub.

Then, `chown` will change the ownership of the MySQL data directory to ensure proper permissions for the MySQL server process.

Some important environment variables are set.

- `MYSQL_DATABASE`: Specifies the initial database name.
- `MYSQL_ROOT_PASSWORD`: Sets the root password for the MySQL server.

Finally, the `euchre_schema.sql` file is copied into a directory that MySQL automatically executes during initialization. This file contains SQL commands to initialize the euchre database.

> Port: 3306

## How to run and troubleshoot

> **Make sure the Docker Deamon is running or open Docker Desktop.**

### Start the app

Use the following command to run the entire app:

`docker compose up`

- use `--build` flag to rebuild images if needed
- use `-d` to run the app in detached mode and be able to keep using the terminal window

### Stop the app

Use the following command to stop the containers. This will also override the self-healing policy specified in `docker-compose.yml`.

`docker compose down`

- use `-v` to remove volumes.

### Common Problems

```
------
[backend 5/5] RUN ./gradlew build:
0.309 /bin/sh 1: ./gradlew not found
------
```

> Fix: Unknown
