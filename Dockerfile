FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# This forces Docker to re-copy the jar every single time
# by using a unique build argument
ARG CACHE_BUST=1

COPY build/libs/*-SNAPSHOT.jar app.jar

RUN mkdir -p worlds/lobby worlds/rpglobby worlds/rpg_instance

# Add a check to see the JAR's size in the logs
RUN ls -lh app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]