FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace/app
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle
COPY src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre
EXPOSE 8080
COPY --from=build /workspace/app/build/libs/*.jar /app/spring-petclinic-kotlin.jar
ENTRYPOINT ["java", "-jar", "/app/spring-petclinic-kotlin.jar"]
