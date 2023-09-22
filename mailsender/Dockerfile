FROM maven:3.8.1-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -f ./mailsender/pom.xml

FROM openjdk:17
COPY --from=build ./mailsender/target/mailsender.jar ./mailsender.jar
ENTRYPOINT ["java", "-jar", "mailsender.jar"]