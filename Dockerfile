# Use uma imagem base do JDK 17
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo de build e dependências
COPY target/*.jar app.jar

# Define o comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]


