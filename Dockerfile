# Dockerfile (project root)

# build stage
FROM maven:3.9.10-eclipse-temurin-24 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# runtime stage
FROM eclipse-temurin:24-jdk
ARG JAR_FILE=/workspace/target/*.jar
COPY --from=build /workspace/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
