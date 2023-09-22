FROM maven:3.8.1-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -f ./configserver/pom.xml

FROM openjdk:17
COPY --from=build ./configserver/target/configserver.jar ./configserver.jar
ENTRYPOINT ["java", "-jar", "configserver.jar"]