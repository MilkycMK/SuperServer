FROM openjdk:17-oracle

WORKDIR /app
COPY ./build/libs/AdolfServer.jar /app/AdolfServer.jar
COPY ./certificate.p12 /app/certificate.p12
EXPOSE 8443

CMD ["java", "-jar", "AdolfServer.jar", "--spring.datasource.url=jdbc:mariadb://db:3306/app"]