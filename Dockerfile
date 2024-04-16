#if you are deploying it to vercel then you dont need this first stage but just make sure that it points to the railway domain
# you can just continue with the second and third stage 

FROM node:21 AS ng-builder

RUN npm i -g @angular/cli

WORKDIR /ngapp

COPY frontend/package*.json . 
#OR COPY *.json .
COPY frontend/angular.json .
COPY frontend/tsconfig.* .
COPY frontend/src src

RUN npm ci
RUN ng build



# Starting with this Linux server
FROM maven:3-eclipse-temurin-21 AS sb-builder

## Build the application
# Create a directory call /sbapp
# go into the directory cd /app
WORKDIR /sbapp

# everything after this is in /sbapp
COPY server/mvnw .
COPY server/mvnw.cmd .
COPY server/pom.xml .
COPY server/.mvn .mvn
COPY server/src src

COPY --from=ng-builder /ngapp/dist/frontend/browser/ src/main/resources/static 
#OR . is ok

# Build the application
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:21-jdk-bullseye

WORKDIR /app 

COPY --from=sb-builder /sbapp/target/server-0.0.1-SNAPSHOT.jar app.jar

## Run the application
# Define environment variable 
ENV PORT=8080 


# Expose the port
EXPOSE ${PORT}

# Run the program
ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar