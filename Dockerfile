FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /workspace

# Cache dependencies separately from source code for faster rebuilds.
COPY pom.xml ./
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline

COPY src ./src
RUN mvn --batch-mode --no-transfer-progress clean package -DskipTests \
    && cp target/*.jar target/application.jar

FROM eclipse-temurin:17-jre-jammy AS runtime

RUN groupadd --system spring \
    && useradd --system --gid spring --home-dir /app spring

WORKDIR /app

COPY --from=build --chown=spring:spring /workspace/target/application.jar ./application.jar

USER spring:spring

EXPOSE 10000

# Render supplies PORT at runtime and expects the service to listen on 0.0.0.0.
ENTRYPOINT ["sh", "-c", "exec java -Dserver.address=0.0.0.0 -Dserver.port=${PORT:-10000} -jar /app/application.jar"]
