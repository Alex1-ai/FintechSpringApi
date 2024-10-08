FROM openjdk:17-jdk


WORKDIR /app

COPY target/bank-app-0.0.1-SNAPSHOT.jar /app/bank-app.jar

EXPOSE 8080

CMD ["java", "-jar", "bank-app.jar"]