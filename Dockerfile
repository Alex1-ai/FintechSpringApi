FROM eclipse-temurin:17-jre-alpine

WORKDIR /usr/app

COPY ./target/bank-app-*.jar /usr/app/

EXPOSE 8080

CMD java -jar bank-app-*.jar
