FROM openjdk:17-alpine
ADD target/activity-api-service.jar activity-api-service.jar
EXPOSE 8081
ENTRYPOINT [ "java", "-jar",  "activity-api-service.jar"]