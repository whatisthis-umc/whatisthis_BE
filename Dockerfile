FROM openjdk:17-jdk-slim
COPY build/libs/what-is-this.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
