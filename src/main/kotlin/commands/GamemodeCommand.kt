package commands

import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import utils.Text

object GamemodeCommand {

    fun register() {
        val commandManager = MinecraftServer.getCommandManager()
        val gameModeCmd = Command("gamemode")

        // /gamemode <mode> [target]
        registerMode(gameModeCmd, "creative", GameMode.CREATIVE)
        registerMode(gameModeCmd, "survival", GameMode.SURVIVAL)
        registerMode(gameModeCmd, "adventure", GameMode.ADVENTURE)
        registerMode(gameModeCmd, "spectator", GameMode.SPECTATOR)

        gameModeCmd.setDefaultExecutor { sender, _ ->
            sender.sendMessage(Text.red("Usage: /gamemode creative|survival|adventure|spectator [player]"))
        }

        commandManager.register(gameModeCmd)
    }

    private fun registerMode(cmd: Command, literal: String, mode: GameMode) {
        val targetArg = ArgumentType.Entity("target").onlyPlayers(true).singleEntity(true)

        // /gamemode <mode>
        cmd.addSyntax({ sender, _ ->
            val player = sender as? Player ?: run {
                sender.sendMessage(Text.gray("This command can only be run by a player!"))
                return@addSyntax
            }

            player.gameMode = mode
            player.sendMessage(
                Text.green("Set your game mode to ").append(Text.white(mode.name.lowercase()))
            )
        }, ArgumentType.Literal(literal))

        // /gamemode <mode> <target>
        cmd.addSyntax({ sender, ctx ->
            val senderPlayer = sender as? Player // optional, only for nicer messages

            val target = ctx.get(targetArg).findFirstPlayer(sender)
                ?: run {
                    sender.sendMessage(Text.red("Player not found."))
                    return@addSyntax
                }

            target.gameMode = mode

            // Message to target
            target.sendMessage(
                Text.green("Your game mode was set to ").append(Text.white(mode.name.lowercase()))
            )

            // Message to sender
            val targetName = target.username
            sender.sendMessage(
                Text.green("Set ").append(Text.white(targetName))
                    .append(Text.green("'s game mode to "))
                    .append(Text.white(mode.name.lowercase()))
            )
        }, ArgumentType.Literal(literal), targetArg)
    }
}
