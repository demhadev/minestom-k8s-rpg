import managers.LobbyServer
import managers.ServerRole

fun main() {
    // LobbyServer().start()
    val role = ServerRole.fromEnv()
    println("Starting ${role} server...")

    when (role) {
        ServerRole.LOBBY -> LobbyServer(role).start()
        ServerRole.RPG_LOBBY -> LobbyServer(role).start()
        ServerRole.RPG_INSTANCE -> LobbyServer(role).start()
    }
}