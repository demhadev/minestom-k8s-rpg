package registers

import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class WorldRegistry(
    val mainLobby: InstanceContainer,
    val rpgLobby: InstanceContainer,
    private val rpgWorldFactory: (UUID) -> InstanceContainer
) {
    private val rpgWorldsByOwner = ConcurrentHashMap<UUID, InstanceContainer>()

    fun getOrCreateWorld(owner: UUID): InstanceContainer {
        return rpgWorldsByOwner.computeIfAbsent(owner) {
            rpgWorldFactory(owner)
        }
    }

}