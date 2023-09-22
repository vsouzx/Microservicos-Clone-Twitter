FROM maven:3.8.1-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -f ./discovery/pom.xml

FROM openjdk:17
COPY --from=build ./discovery/target/discovery.jar ./discovery.jar
ENTRYPOINT ["java", "-jar", "discovery.jar"]