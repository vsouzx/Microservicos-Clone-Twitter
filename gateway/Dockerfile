FROM maven:3.8.1-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -f ./gateway/pom.xml

FROM openjdk:17
COPY --from=build ./gateway/target/gateway.jar ./gateway.jar
ENTRYPOINT ["java", "-jar", "gateway.jar"]