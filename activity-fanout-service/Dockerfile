FROM openjdk:17-alpine
ADD target/activity-fanout-service.jar activity-fanout-service.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar",  "activity-fanout-service.jar"]