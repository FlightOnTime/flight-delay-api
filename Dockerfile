FROM eclipse-temurin:17-jdk-alpine

# Estágio 1: Build
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /app

# Copia o pom.xml e baixa as dependências para aproveitar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e realiza o build
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar /app/app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando de entrada
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
