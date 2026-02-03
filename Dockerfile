FROM eclipse-temurin:17-jre-alpine

WORKDIR /usr/app

COPY ./target/bank-app-*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]