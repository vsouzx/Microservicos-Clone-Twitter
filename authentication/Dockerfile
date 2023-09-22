FROM maven:3.8.1-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -f ./authentication/pom.xml

FROM openjdk:17
COPY --from=build ./authentication/target/authentication.jar ./authentication.jar
ENTRYPOINT ["java", "-jar", "authentication.jar"]