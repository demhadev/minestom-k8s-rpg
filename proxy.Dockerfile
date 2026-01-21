FROM eclipse-temurin:21-jre-alpine

WORKDIR /proxy

# Use a specific, verified stable build of Velocity
ADD https://fill-data.papermc.io/v1/objects/fe53021f3168322cb6cb68f78699866fd098df3c306e4359847a10b0d02689ef/velocity-3.4.0-SNAPSHOT-563.jar velocity.jar

# Create the velocity.toml DIRECTLY in the build to ensure it is perfect
RUN echo 'config-version = "2.7"' > velocity.toml && \
    echo 'bind = "0.0.0.0:25577"' >> velocity.toml && \
    echo 'motd = "Minestom Cluster"' >> velocity.toml && \
    echo 'show-max-players = 500' >> velocity.toml && \
    echo 'online-mode = false' >> velocity.toml && \
    echo '[servers]' >> velocity.toml && \
    echo '    lobby = "lobby:25565"' >> velocity.toml && \
    echo '    rpg_lobby = "rpg-lobby:25565"' >> velocity.toml && \
    echo '[main]' >> velocity.toml && \
    echo '    try = ["lobby"]' >> velocity.toml && \
    echo '[player-info-forwarding]' >> velocity.toml && \
    echo '    mode = "modern"' >> velocity.toml && \
    echo '[forced-hosts]' >> velocity.toml

# Create the separate secret file Velocity wants
RUN echo "testing123" > forwarding.secret

# Start Velocity
ENTRYPOINT ["java", "-Xmx512M", "-Xms512M", "-jar", "velocity.jar"]