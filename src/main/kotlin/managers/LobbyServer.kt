package managers

import commands.GamemodeCommand
import net.minestom.server.Auth
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import kotlin.jvm.java

class LobbyServer(private val role: ServerRole) {
    fun start() {
        System.setProperty("minestom.unsupported.protocol-check", "true")
        // 1. Get the secret from the environment
        val velocitySecret = System.getenv("VELOCITY_SECRET")
            ?: error("VELOCITY_SECRET environment variable is NOT SET! Server cannot start safely.")

        // 2. Initialize with the secret
        val minecraftServer = MinecraftServer.init(Auth.Velocity(velocitySecret))

        // 1. Initialize the Registry based on role
        val worldRegistry = Worlds.createRegistry(role)
        val instance = worldRegistry.getPrimaryInstance()

        val eventHandler = MinecraftServer.getGlobalEventHandler()
        eventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            val player = event.player
            println("Player ${player.username} is joining the $role...")
            println("!!! CONNECTION ATTEMPT: ${event.player.username} is pre-logging in")

            // YOU MUST SET THE INSTANCE HERE
            event.spawningInstance = instance
            player.respawnPoint = net.minestom.server.coordinate.Pos(0.0, 42.0, 0.0)
        }

        // Register events
        Listeners.register(role, instance)

        // Register commands
        ServerCommand.register(role, worldRegistry)
        GamemodeCommand.register()

        // Start the server
        val port = System.getenv("SERVER_PORT")?.toIntOrNull() ?: 25565
        minecraftServer.start("0.0.0.0", port)
        println("Server ${role} started on port $port!")

    }
}