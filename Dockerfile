# Use an OpenJDK image as the base
FROM --platform=${TARGETPLATFORM} eclipse-temurin:17-jre

WORKDIR /app

COPY build/libs/spring-petclinic-kotlin-*.*.*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
