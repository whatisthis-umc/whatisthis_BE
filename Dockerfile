FROM openjdk:17-jdk-slim
COPY build/libs/what-is-this.jar app.jar
# JVM에 KST 타임존 적용
ENTRYPOINT ["java", "-Djdk.management.cgroup.enabled=false", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]

