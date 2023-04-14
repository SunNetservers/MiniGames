package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaGroup;
import net.knarcraft.minigames.container.SerializableUUID;
import net.knarcraft.minigames.util.SerializableConverter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A sorted group of arenas that must be completed in sequence
 */
public class DropperArenaGroup extends ArenaGroup<DropperArena, DropperArenaGroup> {

    /**
     * Instantiates a new dropper arena group
     *
     * @param groupName <p>The name of this group</p>
     */
    public DropperArenaGroup(@NotNull String groupName) {
        super(groupName, MiniGames.getInstance().getDropperArenaHandler());
    }

    /**
     * Instantiates a new dropper arena group
     *
     * @param groupId   <p>The unique id of this group</p>
     * @param groupName <p>The name of this group</p>
     * @param arenas    <p>The arenas in this group</p>
     */
    private DropperArenaGroup(@NotNull UUID groupId, @NotNull String groupName, @NotNull List<UUID> arenas) {
        super(groupId, groupName, arenas, MiniGames.getInstance().getDropperArenaHandler());
    }

    /**
     * Deserializes the given data
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized arena group</p>
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static @NotNull DropperArenaGroup deserialize(@NotNull Map<String, Object> data) {
        UUID id = ((SerializableUUID) data.get("groupId")).getRawValue();
        String name = (String) data.get("groupName");
        List<SerializableUUID> serializableArenas = (List<SerializableUUID>) data.get("arenas");
        List<UUID> arenas = new ArrayList<>();

        SerializableConverter.getRawValue(new ArrayList<>(serializableArenas), arenas);
        return new DropperArenaGroup(id, name, arenas);
    }

}
