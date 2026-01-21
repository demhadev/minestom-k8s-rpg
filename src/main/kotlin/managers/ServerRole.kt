package managers

enum class ServerRole {
    LOBBY,
    RPG_LOBBY,
    RPG_INSTANCE;

companion object {
    fun fromEnv(): ServerRole {
        val raw = System.getenv("ROLE")?.trim()?.uppercase()
        return runCatching { valueOf(raw ?: "LOBBY") }.getOrDefault(LOBBY)
    }
}
}