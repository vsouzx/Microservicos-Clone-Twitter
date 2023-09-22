FROM maven:3.8.1-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -f ./notifications/pom.xml

FROM openjdk:17
COPY --from=build ./notifications/target/notifications.jar ./notifications.jar
ENTRYPOINT ["java", "-jar", "notifications.jar"]