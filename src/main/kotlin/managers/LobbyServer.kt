package managers

import commands.GamemodeCommand
import commands.ServerCommand
import net.minestom.server.MinecraftServer

class LobbyServer(private val role: ServerRole) {
    fun start() {
        val minecraftServer = MinecraftServer.init()

        // Create worlds
        val instance = Worlds.create(role)

        // Register events
        Listeners.register(role, instance)

        // Register commands
        ServerCommand.register(instance)
        GamemodeCommand.register(role, instance)

        // Start the server
        minecraftServer.start("0.0.0.0", 25565)
        println("Server ${role} started on 25565!")

    }
}