FROM eclipse-temurin:17-jre
WORKDIR /app
# Use wildcard to match any JAR file
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]