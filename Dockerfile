FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY lib /app/lib
COPY build/libs/app.jar /app/app.jar

ENTRYPOINT ["java", "-cp", "/app/app.jar:/app/lib/*", "MainKt"]