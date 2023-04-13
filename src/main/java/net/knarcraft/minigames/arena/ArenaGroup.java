package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.arena.dropper.DropperArenaGroup;
import net.knarcraft.minigames.container.SerializableUUID;
import net.knarcraft.minigames.util.StringSanitizer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class ArenaGroup implements ConfigurationSerializable {

    /**
     * The unique id for this group of arenas
     */
    private final UUID groupId;

    /**
     * The unique name for this group of arenas
     */
    private final String groupName;

    /**
     * The arenas in this group, ordered from stage 1 to stage n
     */
    protected final List<UUID> arenas;

    /**
     * Instantiates a new dropper arena group
     *
     * @param groupName <p>The name of this group</p>
     */
    protected ArenaGroup(@NotNull String groupName) {
        this.groupId = UUID.randomUUID();
        this.groupName = groupName;
        this.arenas = new ArrayList<>();
    }

    /**
     * Instantiates a new arena group
     *
     * @param groupId   <p>The unique id of this group</p>
     * @param groupName <p>The name of this group</p>
     * @param arenas    <p>The arenas in this group</p>
     */
    protected ArenaGroup(@NotNull UUID groupId, @NotNull String groupName, @NotNull List<UUID> arenas) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.arenas = new ArrayList<>(arenas);
    }

    /**
     * Gets the id of this arena group
     *
     * @return <p>The id of this group</p>
     */
    public @NotNull UUID getGroupId() {
        return this.groupId;
    }

    /**
     * Gets the name of this arena group
     *
     * @return <p>The name of this group</p>
     */
    public @NotNull String getGroupName() {
        return this.groupName;
    }

    /**
     * Gets the arenas contained in this group in the correct order
     *
     * @return <p>The ids of the arenas in this group</p>
     */
    public @NotNull List<UUID> getArenas() {
        return new ArrayList<>(arenas);
    }

    /**
     * Removes the given arena from this group
     *
     * @param arenaId <p>The id of the arena to remove</p>
     */
    public void removeArena(UUID arenaId) {
        this.arenas.remove(arenaId);
    }

    /**
     * Adds an arena to the end of this group
     *
     * @param arenaId <p>The arena to add to this group</p>
     */
    public void addArena(UUID arenaId) {
        addArena(arenaId, this.arenas.size());
    }

    /**
     * Adds an arena to the end of this group
     *
     * @param arenaId <p>The arena to add to this group</p>
     * @param index   <p>The index to put the arena in</p>
     */
    public void addArena(UUID arenaId, int index) {
        // Make sure we don't have duplicates
        if (!this.arenas.contains(arenaId)) {
            this.arenas.add(index, arenaId);
        }
    }

    /**
     * Gets this group's name, but sanitized
     *
     * @return <p>The sanitized group name</p>
     */
    public @NotNull String getGroupNameSanitized() {
        return StringSanitizer.sanitizeArenaName(this.getGroupName());
    }

    /**
     * Swaps the arenas at the given indices
     *
     * @param index1 <p>The index of the first arena to swap</p>
     * @param index2 <p>The index of the second arena to swap</p>
     */
    public void swapArenas(int index1, int index2) {
        // Change nothing if not a valid request
        if (index1 == index2 || index1 < 0 || index2 < 0 || index1 >= this.arenas.size() ||
                index2 >= this.arenas.size()) {
            return;
        }

        // Swap the two arena ids
        UUID temporaryValue = this.arenas.get(index2);
        this.arenas.set(index2, this.arenas.get(index1));
        this.arenas.set(index1, temporaryValue);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("groupId", new SerializableUUID(this.groupId));
        data.put("groupName", this.groupName);

        List<SerializableUUID> serializableArenas = new ArrayList<>();
        for (UUID arenaId : arenas) {
            serializableArenas.add(new SerializableUUID(arenaId));
        }
        data.put("arenas", serializableArenas);
        return data;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DropperArenaGroup otherGroup)) {
            return false;
        }
        return this.getGroupNameSanitized().equals(otherGroup.getGroupNameSanitized());
    }

}
