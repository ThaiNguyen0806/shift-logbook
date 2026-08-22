FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN sh mvnw dependency:go-offline
COPY src/ src/
RUN sh mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY scripts/ scripts/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]