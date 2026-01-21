package managers

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import utils.Text
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import registers.WorldRegistry

object Listeners {
    object SpawnPoints {
        val LOBBY = Pos(0.0, 42.0, 0.0)
        val RPG_LOBBY = Pos(10.0, 42.0, 10.0)
    }


    fun register(role: ServerRole, spawningInstance: Instance) {
        val handler: GlobalEventHandler = MinecraftServer.getGlobalEventHandler()

        // New Minestom flow: set spawning instance + respawn point before player is fully in the game
        handler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            event.spawningInstance = spawningInstance
            event.player.respawnPoint = SpawnPoints.LOBBY
        }



        handler.addListener(PlayerSpawnEvent::class.java) { event ->
            if (event.isFirstSpawn) {
                val msg = Component.text("Welcome! Role = $role", NamedTextColor.GREEN)
                event.player.sendMessage(msg)
            }
        }
    }
}

