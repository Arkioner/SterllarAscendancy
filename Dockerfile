# Dockerfile (project root)

# Build stage
FROM amazoncorretto:24-jdk AS build
RUN yum install -y maven
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# Runtime stage
FROM amazoncorretto:24-alpine
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
