FROM openjdk:17-alpine
COPY build/libs/what-is-this.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]