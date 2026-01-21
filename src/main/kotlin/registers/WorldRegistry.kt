package registers

import managers.ServerRole
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class WorldRegistry(
    private val role: ServerRole,
    private val instances: Map<String, InstanceContainer>,
    private val rpgWorldFactory: (UUID) -> InstanceContainer
) {
    private val rpgWorldsByOwner = ConcurrentHashMap<UUID, InstanceContainer>()

    fun getPrimaryInstance(): InstanceContainer {
        return when (role) {
            ServerRole.LOBBY -> instances["lobby"] ?: error("Lobby instance missing")
            ServerRole.RPG_LOBBY -> instances["rpg_lobby"] ?: error("RPG Lobby instance missing")
            ServerRole.RPG_INSTANCE -> instances["rpg_instance"] ?: error("RPG Instance missing")
        }
    }


    fun getOrCreateWorld(owner: UUID): InstanceContainer {
        return rpgWorldsByOwner.computeIfAbsent(owner) {
            rpgWorldFactory(owner)
        }
    }

}