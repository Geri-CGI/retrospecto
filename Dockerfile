FROM eclipse-temurin:18-jre
COPY ./backend-0.0.1-SNAPSHOT.jar /retrospecto.jar
EXPOSE 8098
CMD ["java", "-jar",  "/retrospecto.jar"]