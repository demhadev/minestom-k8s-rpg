import managers.LobbyServer
import managers.ServerRole

fun main() {
    // 1. Force these BEFORE any other Minestom classes load
    System.setProperty("minestom.unsupported.protocol-check", "true")
    System.setProperty("java.net.preferIPv4Stack", "true")

    val role = ServerRole.fromEnv()
    println("Starting ${role} server...")

    when (role) {
        ServerRole.LOBBY -> LobbyServer(role).start()
        ServerRole.RPG_LOBBY -> LobbyServer(role).start()
        ServerRole.RPG_INSTANCE -> LobbyServer(role).start()
    }
}