package managers

import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.instance.block.Block
import registers.InstanceMeta
import registers.InstanceType
import registers.WorldRegistry
import java.util.UUID

object Worlds {
    fun createRegistry(role: ServerRole): WorldRegistry {
        val im = MinecraftServer.getInstanceManager()
        val instances = mutableMapOf<String, InstanceContainer>()

        // This is where we actually trigger the creation/generation
        when (role) {
            ServerRole.LOBBY -> {
                instances["lobby"] = createInstance(im, "worlds/lobby")
            }
            ServerRole.RPG_LOBBY -> {
                instances["rpg_lobby"] = createInstance(im, "worlds/rpglobby")
            }
            ServerRole.RPG_INSTANCE -> {
                instances["rpg_instance"] = createInstance(im, "worlds/rpg_instance")
            }
        }

        return WorldRegistry(role, instances, ::createRPGWorld)
    }

    private fun createInstance(im: InstanceManager, path: String): InstanceContainer {
        val instance = im.createInstanceContainer()

        // Get the absolute path to see where Docker thinks it is
        val absolutePath = java.io.File(path).absolutePath
        println("Initializing instance at: $absolutePath")

        instance.chunkLoader = AnvilLoader(path)

        instance.setChunkSupplier { inst, x, z -> LightingChunk(inst, x, z) }

        // FORCE GENERATION: Load the spawn chunks immediately
        // This will trigger the generator and save the files to your folder
        for (x in -2..2) {
            for (z in -2..2) {
                instance.loadChunk(x, z)
            }
        }

        return instance
    }

    private fun createRPGWorld(owner: UUID): InstanceContainer {
        // ... implementation for dynamic instance creation ...
        return MinecraftServer.getInstanceManager().createInstanceContainer()
    }
}

