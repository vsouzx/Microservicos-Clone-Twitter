FROM maven:3.8.1-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -f ./accounts/pom.xml

FROM openjdk:17
COPY --from=build ./accounts/target/accounts.jar ./accounts.jar
ENTRYPOINT ["java", "-jar", "accounts.jar"]