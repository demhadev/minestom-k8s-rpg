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
    fun create(role: ServerRole): InstanceContainer {
        val instanceManager = MinecraftServer.getInstanceManager()

        val instance: InstanceContainer = instanceManager.createInstanceContainer()

        // Persist and load world data
        instance.chunkLoader = AnvilLoader(
            when (role) {
                ServerRole.LOBBY -> "worlds/lobby"
                ServerRole.RPG_LOBBY -> "worlds/rpglobby"
                ServerRole.RPG_INSTANCE -> "worlds/rpg_instance"
            }
        )

        instance.setChunkSupplier { inst, x, z -> LightingChunk(inst, x, z) }

        return instance

    }



    /*fun createRegistry(): WorldRegistry {
        val im = MinecraftServer.getInstanceManager()

        val mainLobby = im.createInstanceContainer().apply {
            setGenerator { unit ->
                unit.modifier().fillHeight(0,40, Block.GRASS_BLOCK)
            }
            setTag(InstanceMeta.TYPE, InstanceType.MAIN_LOBBY.name)
        }
        val rpgLobby = im.createInstanceContainer().apply {
            setGenerator { unit ->
                unit.modifier().fillHeight(0,40, Block.STONE)
            }
            setTag(InstanceMeta.TYPE, InstanceType.RPG_LOBBY.name)
        }
        // Registry gets a function that can create RPG worlds
        return WorldRegistry(
            mainLobby = mainLobby,
            rpgLobby = rpgLobby,
            rpgWorldFactory = ::createRPGWorld
        )
    }

    fun createRPGWorld(owner: UUID): InstanceContainer {
        val im = MinecraftServer.getInstanceManager()
        val instance = im.createInstanceContainer()

        instance.setTag(InstanceMeta.TYPE, InstanceType.RPG_WORLD.name)
        instance.setTag(InstanceMeta.OWNER, owner)

        // Placeholder generation
        instance.setGenerator { unit ->
            unit.modifier().fillHeight(0,40, Block.PODZOL)
        }
        return instance
    }

    data class WorldBundle(
        val mainLobby: InstanceContainer,
        val rpgLobby: InstanceContainer,
        val rpgWorld: InstanceContainer
    )

    fun createAll(): WorldBundle {
        val im = MinecraftServer.getInstanceManager()

        val mainLobby = im.createInstanceContainer().apply {
            setGenerator { unit ->
                // Flat lobby for now
                unit.modifier().fillHeight(0,40, Block.GRASS_BLOCK)
            }
        }
        val rpgLobby = im.createInstanceContainer().apply {
            setGenerator { unit ->
                unit.modifier().fillHeight(0,40, Block.STONE)
            }
        }

        val rpgWorld = im.createInstanceContainer().apply {
            setGenerator { unit ->
                unit.modifier().fillHeight(0,40, Block.PODZOL)
            }
        }

        return WorldBundle(
            mainLobby = mainLobby,
            rpgLobby = rpgLobby,
            rpgWorld = rpgWorld
        )
    }*/

}