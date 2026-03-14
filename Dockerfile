#builder
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

#cache del docker y depenencias
COPY pom.xml .
RUN mvn dependency:go-offline

# copiar codigo fuente y compilr
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]