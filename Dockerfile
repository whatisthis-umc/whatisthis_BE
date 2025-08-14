FROM openjdk:17-jdk-slim
COPY build/libs/what-is-this.jar app.jar
ENTRYPOINT ["java", "-Djdk.management.cgroup.enabled=false", "-jar", "/app.jar"]
