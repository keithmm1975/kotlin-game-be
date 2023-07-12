FROM openjdk:11
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY ${DEPENDENCY}/libs/game-0.0.1-SNAPSHOT.jar /app/lib/app.jar
ENTRYPOINT ["java", "-jar", "/app/lib/app.jar"]