package managers

import commands.GamemodeCommand
import net.minestom.server.Auth
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

class LobbyServer(private val role: ServerRole) {
    fun start() {
        val velocitySecret = System.getenv("VELOCITY_SECRET") ?: "testing123"
        System.setProperty("minestom.proxy", "velocity")
        System.setProperty("minestom.velocity.secret", velocitySecret)
        System.setProperty("minestom.unsupported.protocol-check", "true")

        // 2. This IS the correct way for your specific version
        val minecraftServer = MinecraftServer.init(Auth.Velocity(velocitySecret))

        val worldRegistry = Worlds.createRegistry(role)
        val instance = worldRegistry.getPrimaryInstance()

        // 3. Mandatory Join Listener
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            println("!!! Handshake Successful: ${event.player.username} is configuring")
         }

        // Register events
        Listeners.register(role, instance)

        // Register commands
        ServerCommand.register(role, worldRegistry)
        GamemodeCommand.register()

        val port = System.getenv("SERVER_PORT")?.toIntOrNull() ?: 25565
        minecraftServer.start("0.0.0.0", port)
        println("[$role] Server started on 0.0.0.0:$port")
    }
}