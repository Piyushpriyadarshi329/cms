# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache dependencies first (only re-downloads when pom.xml changes)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Build the app (tests skipped — they need a live DB/Redis)
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the executable Spring Boot jar (matches the repackaged jar, not *.jar.original)
COPY --from=build /app/target/*.jar app.jar

# Respect the container's memory limit (Railway/containers) instead of the host's
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"

# Informational; the app actually binds to $PORT (server.port=${PORT:8082})
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
