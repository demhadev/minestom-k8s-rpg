package commands

import managers.ServerRole
import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance
import registers.InstanceMeta
import registers.InstanceType
import registers.WorldRegistry
import utils.Text

object ServerCommand {
    
    fun register(role: ServerRole, instance: Instance) {
        val commandManager = MinecraftServer.getCommandManager()

        val serverCmd = Command("server")

        // /server main
        serverCmd.addSyntax({sender, _ ->
            run {
                val player = sender as? Player ?: run {
                    sender.sendMessage(Text.gray("This command can only be run by the player!"))
                    return@addSyntax
                }

                if (player.instance == ) {
                    player.sendMessage(Text.red("You are already on this server!"))
                    return@addSyntax
                }

                player.setInstance(worlds.mainLobby, player.respawnPoint)
                player.sendMessage(Text.green("Teleported to main lobby!"))
            }
        }, ArgumentType.Literal("main"))

        // /server rpg
        serverCmd.addSyntax({ sender, _ ->
            run {
                val player = sender as? Player ?: run {
                    sender.sendMessage(Text.gray("This command can only be run by the player!"))
                    return@addSyntax
                }
                if (player.instance == worlds.rpgLobby) {
                    player.sendMessage(Text.red("You are already on this server!"))
                    return@addSyntax
                }
                player.setInstance(worlds.rpgLobby, player.respawnPoint)
                player.sendMessage(Text.green("Teleported to rpg lobby!"))
            }
        }, ArgumentType.Literal("rpg"))

        // /server play
        serverCmd.addSyntax({ sender, _ ->
            run {
                val player = sender as? Player ?: run {
                    sender.sendMessage(Text.gray("This command can only be run by the player!"))
                    return@addSyntax
                }
                val personalWorld = worlds.getOrCreateWorld(player.uuid)

                // Check if they are already in their own server
                val currentType = player.instance.getTag(InstanceMeta.TYPE)
                val currentOwner = player.instance.getTag(InstanceMeta.OWNER)

                if (currentType == InstanceType.RPG_WORLD.name && currentOwner == player.uuid) {
                    player.sendMessage(Text.red("You are already on this server!"))
                    return@addSyntax
                }
                player.setInstance(personalWorld, player.respawnPoint)
                player.sendMessage(Text.green("Teleported to your rpg world!"))

            }
        }, ArgumentType.Literal("play"))

        // If they type just /server
        serverCmd.setDefaultExecutor { sender, _ ->
            sender.sendMessage(Text.red("Usage: /server main | rpg | play"))
        }

        commandManager.register(serverCmd)



 /*       // /main
        commandManager.register(object : Command("main") {
            init {
                setDefaultExecutor { sender, _ ->
                    val player = sender as? Player
                    if (player == null) {
                        sender.sendMessage(Text.red("Players only."))
                        return@setDefaultExecutor
                    }
                    player.setInstance(worlds.mainLobby, player.respawnPoint)
                    player.sendMessage(Text.green("Teleported to Main Lobby."))
                }
            }
        })

        // /rpg
        commandManager.register(object : Command("rpg") {
            init {
                setDefaultExecutor { sender, _ ->
                    val player = sender as? Player
                    if (player == null) {
                        sender.sendMessage(Text.red("Players only."))
                        return@setDefaultExecutor
                    }
                    player.setInstance(worlds.rpgLobby, player.respawnPoint)
                    player.sendMessage(Text.green("Teleported to RPG Lobby."))
                }
            }
        })

        // /play -> RPG world
        commandManager.register(object : Command("play") {
            init {
                setDefaultExecutor { sender, _ ->
                    val player = sender as? Player
                    if (player == null) {
                        sender.sendMessage(Text.red("Players only."))
                        return@setDefaultExecutor
                    }
                    player.setInstance(worlds.rpgWorld, player.respawnPoint)
                    player.sendMessage(Text.green("Teleported to RPG World."))
                }
            }
        })*/
    }

}
