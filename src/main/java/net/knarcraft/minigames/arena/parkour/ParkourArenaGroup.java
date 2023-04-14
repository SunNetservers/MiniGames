package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaGroup;
import net.knarcraft.minigames.container.SerializableUUID;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A sorted group of arenas that must be completed in sequence
 */
public class ParkourArenaGroup extends ArenaGroup<ParkourArena, ParkourArenaGroup> {

    /**
     * Instantiates a new parkour arena group
     *
     * @param groupName <p>The name of this group</p>
     */
    public ParkourArenaGroup(@NotNull String groupName) {
        super(groupName, MiniGames.getInstance().getParkourArenaHandler());
    }

    /**
     * Instantiates a new parkour arena group
     *
     * @param groupId   <p>The unique id of this group</p>
     * @param groupName <p>The name of this group</p>
     * @param arenas    <p>The arenas in this group</p>
     */
    private ParkourArenaGroup(@NotNull UUID groupId, @NotNull String groupName, @NotNull List<UUID> arenas) {
        super(groupId, groupName, arenas, MiniGames.getInstance().getParkourArenaHandler());
    }

    /**
     * Deserializes the given data
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized arena group</p>
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static @NotNull ParkourArenaGroup deserialize(@NotNull Map<String, Object> data) {
        UUID id = ((SerializableUUID) data.get("groupId")).getRawValue();
        String name = (String) data.get("groupName");
        List<SerializableUUID> serializableArenas = (List<SerializableUUID>) data.get("arenas");
        List<UUID> arenas = new ArrayList<>();
        for (SerializableUUID arenaId : serializableArenas) {
            arenas.add(arenaId.getRawValue());
        }
        return new ParkourArenaGroup(id, name, arenas);
    }

}
