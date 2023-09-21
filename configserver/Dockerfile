# Dockerfile do projeto AbsWeb SpringApi
# Linux Alpine e Java RE 17
FROM eclipse-temurin:17-jre-alpine
# Copiando o jar e alterando o nome
COPY /configserver/target/configserver.jar configserver.jar
# Execução da aplicação
ENTRYPOINT ["java","-jar","configserver.jar"]