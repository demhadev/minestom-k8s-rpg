package registers

import net.minestom.server.tag.Tag
import java.util.UUID

enum class InstanceType {
    MAIN_LOBBY,
    RPG_LOBBY,
    RPG_WORLD
}

object InstanceMeta {
    val TYPE: Tag<String> = Tag.String("instance_type")
    val OWNER: Tag<UUID> = Tag.UUID("owner_uuid")
}