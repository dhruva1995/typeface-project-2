FROM openjdk:17-alpine
ADD target/notification-session-service.jar notification-session-service.jar
EXPOSE 8082
ENTRYPOINT [ "java", "-jar",  "notification-session-service.jar"]