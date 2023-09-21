# Dockerfile do projeto AbsWeb SpringApi
# Linux Alpine e Java RE 17
FROM eclipse-temurin:17-jre-alpine
# Criando variável com o caminho do jar local na aplicação
ARG JAR_FILE=/configserver/target/configserver.jar
# Copiando o jar e alterando o nome
COPY ${JAR_FILE} configserver.jar
# Execução da aplicação
ENTRYPOINT ["java","-jar","configserver.jar"]