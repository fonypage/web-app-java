FROM eclipse-temurin:17-jre

WORKDIR /app

# Копируем любой jar из target в контейнер под именем app.jar
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
