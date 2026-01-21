package managers

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.instance.InstanceContainer
import registers.WorldRegistry
import utils.ServerUtils
import utils.Text

object ServerCommand {
    fun register(role: ServerRole, worlds: WorldRegistry) {
        val cm = MinecraftServer.getCommandManager()

        cm.register(whereCmd(role))
        cm.register(lobbyCmd())
        cm.register(rpgLobbyCmd())
        cm.register(rpgCmd(worlds))
    }

    private fun whereCmd(role: ServerRole): Command =
        object : Command("where") { init { setDefaultExecutor { sender, _ -> sender.sendMessage(Text.yellow("Server role: $role")) } }
        }

    private fun lobbyCmd(): Command =
        object : Command("lobby") {
            init {
                setDefaultExecutor { sender, _ ->
                    val player = sender as? Player ?: return@setDefaultExecutor
                    player.sendMessage(Text.yellow("Connecting to Main Lobby..."))
                    ServerUtils.connect(player, "lobby")
                }
            }
        }

    private fun rpgLobbyCmd(): Command =
        object : Command("rpglobby") {
            init {
                setDefaultExecutor { sender, _ ->
                    val player = sender as? Player ?: return@setDefaultExecutor
                    player.sendMessage(Text.yellow("Connecting to RPG Lobby..."))
                    ServerUtils.connect(player, "rpg_lobby")
                }
            }
        }

    private fun rpgCmd(worlds: WorldRegistry): Command =
        object : Command("rpg") {

    private fun playCmd(): Command =
        object : Command("play") {
            init {
                setDefaultExecutor { sender, _ ->
                    val player = sender as? net.minestom.server.entity.Player ?: return@setDefaultExecutor
                    player.sendMessage("Sending you to the RPG server...")

                    // This name "rpg_lobby" must match the name in your velocity.toml [servers] section
                    ServerUtils.connect(player, "rpg_lobby")
                }
            }
        }

    private fun teleport(player: Player, instance: net.minestom.server.instance.Instance, label: String) {
        // Minestom instance switch is async
        player.setInstance(instance).thenRun {
            player.sendMessage(
                Component.text("Teleported to $label", NamedTextColor.GREEN)
            )
        }.exceptionally { ex ->
            player.sendMessage(
                Component.text("Teleport failed: ${ex.message}", NamedTextColor.RED)
            )
            null
        }
    }
}
    }
