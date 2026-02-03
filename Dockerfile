FROM eclipse-temurin:17-jre-alpine


COPY ./target/bank-app-*.jar /usr/app/
WORKDIR /usr/app
#COPY ./target/bank-app-*.jar /app/bank-app.jar

EXPOSE 8080

CMD ["java", "-jar", "bank-app-*.jar"]