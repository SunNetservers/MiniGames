package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.container.SerializableUUID;
import net.knarcraft.minigames.util.StringSanitizer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A group containing a list of arenas
 *
 * @param <K> <p>The type of arena stored</p>
 * @param <S> <p>The type of arena group stored in the given arena handler</p>
 */
public abstract class ArenaGroup<K extends Arena, S extends ArenaGroup<K, S>> implements ConfigurationSerializable {

    /**
     * The unique id for this group of arenas
     */
    private final UUID groupId;

    /**
     * The unique name for this group of arenas
     */
    private final String groupName;

    /**
     * The arena handler used to convert uuids to arenas
     */
    private final ArenaHandler<K, S> arenaHandler;

    /**
     * The arenas in this group, ordered from stage 1 to stage n
     */
    private final List<UUID> arenas;

    /**
     * Instantiates a new dropper arena group
     *
     * @param groupName    <p>The name of this group</p>
     * @param arenaHandler <p>The arena handler used to convert uuids to arenas</p>
     */
    protected ArenaGroup(@NotNull String groupName, @NotNull ArenaHandler<K, S> arenaHandler) {
        this.groupId = UUID.randomUUID();
        this.groupName = groupName;
        this.arenas = new ArrayList<>();
        this.arenaHandler = arenaHandler;
    }

    /**
     * Instantiates a new arena group
     *
     * @param groupId      <p>The unique id of this group</p>
     * @param groupName    <p>The name of this group</p>
     * @param arenas       <p>The arenas in this group</p>
     * @param arenaHandler <p>The arena handler used to convert uuids to arenas</p>
     */
    protected ArenaGroup(@NotNull UUID groupId, @NotNull String groupName, @NotNull List<UUID> arenas,
                         @NotNull ArenaHandler<K, S> arenaHandler) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.arenas = new ArrayList<>(arenas);
        this.arenaHandler = arenaHandler;
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
     * Checks whether the given player has beaten all arenas in this group on the given game-mode
     *
     * @param gameMode <p>The game-mode to check</p>
     * @param player   <p>The player to check</p>
     * @return <p>True if the player has beaten all arenas, false otherwise</p>
     */
    public boolean hasBeatenAll(ArenaGameMode gameMode, Player player) {
        for (UUID anArenaId : this.getArenas()) {
            K arena = this.arenaHandler.getArena(anArenaId);
            if (arena == null) {
                // The arena would only be null if the arena has been deleted, but not removed from this group
                MiniGames.log(Level.WARNING, "The dropper group " + this.getGroupName() +
                        " contains the arena id " + anArenaId + " which is not a valid arena id!");
                continue;
            }

            if (arena.getData().hasNotCompleted(gameMode, player)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets whether the given player can play the given arena part of this group, on the given game-mode
     *
     * @param gameMode <p>The game-mode the player is trying to play</p>
     * @param player   <p>The player to check</p>
     * @param arenaId  <p>The id of the arena in this group to check</p>
     * @return <p>True if the player is allowed to play the arena</p>
     * @throws IllegalArgumentException <p>If checking an arena not in this group</p>
     */
    public boolean cannotPlay(ArenaGameMode gameMode, Player player, UUID arenaId) throws IllegalArgumentException {
        if (!this.arenas.contains(arenaId)) {
            throw new IllegalArgumentException("Cannot check for playability for arena not in this group!");
        }

        for (UUID anArenaId : this.getArenas()) {
            // If the target arena is reached, allow, as all previous arenas must have been cleared
            if (arenaId.equals(anArenaId)) {
                return false;
            }

            K arena = this.arenaHandler.getArena(anArenaId);
            if (arena == null) {
                // The arena would only be null if the arena has been deleted, but not removed from this group
                MiniGames.log(Level.WARNING, String.format("The dropper group %s contains the" +
                        " arena id %s which is not a valid arena id!", this.getGroupName(), anArenaId));
                continue;
            }

            // This is a lower-numbered arena the player has yet to complete
            if (arena.getData().hasNotCompleted(gameMode, player)) {
                return true;
            }
        }

        return true;
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
        if (!(other instanceof ArenaGroup<?, ?> otherGroup)) {
            return false;
        }
        return this.getGroupNameSanitized().equals(otherGroup.getGroupNameSanitized());
    }

}
