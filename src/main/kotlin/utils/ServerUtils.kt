package utils

import net.minestom.server.entity.Player
import net.minestom.server.network.packet.server.common.PluginMessagePacket
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

object ServerUtils {
    // Send a player to another server registered in Velocity
    fun connect(player: Player, server: String) {
        val output = ByteArrayOutputStream()
        val data = DataOutputStream(output)

        try {
            data.writeUTF("Connect")
            data.writeUTF(server)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        player.sendPacket(PluginMessagePacket("bungeecord:main", output.toByteArray()))

    }
}