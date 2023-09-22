FROM maven:3.8.1-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -f ./feed/pom.xml

FROM openjdk:17
COPY --from=build ./feed/target/feed.jar ./feed.jar
ENTRYPOINT ["java", "-jar", "feed.jar"]