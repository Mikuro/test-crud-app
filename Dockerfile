FROM bellsoft/liberica-openjdk-alpine:17.0.9 AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew gradle.properties /app/
COPY gradle /app/gradle
COPY src /app/src

RUN ./gradlew installDist

FROM bellsoft/liberica-openjdk-alpine:17.0.9 AS application

WORKDIR /app

COPY --from=build /app/build/install/com.pushpyshev.test-crud-app /app
COPY keystore.jks /app

EXPOSE 8443:8443

CMD ["./bin/com.pushpyshev.test-crud-app"]