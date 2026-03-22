# Build
FROM maven:3.8.7-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .

RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

# Runtime
FROM eclipse-temurin:17-jre-alpine


RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
