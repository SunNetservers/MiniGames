package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.util.StringSanitizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/**
 * An interface describing a generic arena handler
 *
 * @param <K> <p>The type of arena stored</p>
 * @param <S> <p>The type of arena group stored</p>
 */
public abstract class ArenaHandler<K extends Arena, S extends ArenaGroup<K, S>> {

    protected Map<UUID, K> arenas = new HashMap<>();
    protected Map<UUID, S> arenaGroups = new HashMap<>();
    protected Map<String, UUID> arenaNameLookup = new HashMap<>();
    private final ArenaPlayerRegistry<K> playerRegistry;

    /**
     * Instantiates a new arena handler
     *
     * @param playerRegistry <p>The registry keeping track of player sessions</p>
     */
    public ArenaHandler(ArenaPlayerRegistry<K> playerRegistry) {
        this.playerRegistry = playerRegistry;
    }

    /**
     * Gets all arenas that are within a group
     *
     * @return <p>All arenas in a group</p>
     */
    public @NotNull Set<K> getArenasInAGroup() {
        Set<K> arenas = new HashSet<>();
        for (UUID arenaId : arenaGroups.keySet()) {
            arenas.add(this.arenas.get(arenaId));
        }
        return arenas;
    }

    /**
     * Gets a copy of all arena groups
     *
     * @return <p>All arena groups</p>
     */
    public Set<S> getAllGroups() {
        return new HashSet<>(arenaGroups.values());
    }

    /**
     * Gets the group the given arena belongs to
     *
     * @param arenaId <p>The id of the arena to get the group of</p>
     * @return <p>The group the arena belongs to, or null if not in a group</p>
     */
    public @Nullable S getGroup(@NotNull UUID arenaId) {
        return this.arenaGroups.get(arenaId);
    }

    /**
     * Sets the group for the given arena
     *
     * @param arenaId    <p>The id of the arena to change</p>
     * @param arenaGroup <p>The group to add the arena to, or null to remove the current group</p>
     */
    public void setGroup(@NotNull UUID arenaId, @Nullable S arenaGroup) {
        if (arenaGroup == null) {
            // No need to remove something non-existing
            if (!this.arenaGroups.containsKey(arenaId)) {
                return;
            }

            // Remove the existing group
            S oldGroup = this.arenaGroups.remove(arenaId);
            oldGroup.removeArena(arenaId);
        } else {
            // Make sure to remove the arena from the old group's internal tracking
            if (this.arenaGroups.containsKey(arenaId)) {
                this.arenaGroups.remove(arenaId).removeArena(arenaId);
            }

            this.arenaGroups.put(arenaId, arenaGroup);
            arenaGroup.addArena(arenaId);
        }
        saveGroups();
    }

    /**
     * Gets the arena group with the given name
     *
     * @param groupName <p>The name of the group to get</p>
     * @return <p>The group, or null if not found</p>
     */
    public @Nullable S getGroup(String groupName) {
        String sanitized = StringSanitizer.sanitizeArenaName(groupName);
        for (S arenaGroup : this.arenaGroups.values()) {
            if (arenaGroup.getGroupNameSanitized().equals(sanitized)) {
                return arenaGroup;
            }
        }
        return null;
    }

    /**
     * Replaces an arena's lookup name
     *
     * @param oldName <p>The arena's old sanitized lookup name</p>
     * @param newName <p>The arena's new sanitized lookup name</p>
     */
    public void updateLookupName(@NotNull String oldName, @NotNull String newName) {
        UUID arenaId = this.arenaNameLookup.remove(oldName);
        if (arenaId != null) {
            this.arenaNameLookup.put(newName, arenaId);
        }
    }

    /**
     * Adds a new arena
     *
     * @param arena <p>The arena to add</p>
     */
    public void addArena(@NotNull K arena) {
        this.arenas.put(arena.getArenaId(), arena);
        this.arenaNameLookup.put(arena.getArenaNameSanitized(), arena.getArenaId());
        this.saveArenas();
    }

    /**
     * Gets the arena with the given id
     *
     * @param arenaId <p>The id of the arena to get</p>
     * @return <p>The arena, or null if no arena could be found</p>
     */
    public @Nullable K getArena(@NotNull UUID arenaId) {
        return this.arenas.get(arenaId);
    }

    /**
     * Gets the arena with the given name
     *
     * @param arenaName <p>The arena to get</p>
     * @return <p>The arena with the given name, or null if not found</p>
     */
    public @Nullable K getArena(@NotNull String arenaName) {
        return this.arenas.get(this.arenaNameLookup.get(StringSanitizer.sanitizeArenaName(arenaName)));
    }

    /**
     * Gets all known arenas
     *
     * @return <p>All known arenas</p>
     */
    public @NotNull Map<UUID, K> getArenas() {
        return new HashMap<>(this.arenas);
    }

    /**
     * Removes the given arena
     *
     * @param arena <p>The arena to remove</p>
     */
    public void removeArena(@NotNull K arena) {
        UUID arenaId = arena.getArenaId();
        this.playerRegistry.removeForArena(arena, false);
        this.arenas.remove(arenaId);
        this.arenaNameLookup.remove(arena.getArenaNameSanitized());
        this.arenaGroups.remove(arenaId);
        if (!arena.removeData()) {
            MiniGames.log(Level.WARNING, "Unable to remove arena data file " + arenaId + ".yml. " +
                    "You must remove it manually!");
        }
        this.saveArenas();
    }

    /**
     * Stores the data for the given arena
     *
     * @param arenaId <p>The id of the arena whose data should be saved</p>
     */
    public void saveData(UUID arenaId) {
        K arena = getArena(arenaId);
        if (arena != null) {
            if (!arena.saveData()) {
                MiniGames.log(Level.WARNING, "Unable to save data for arena with id " + arenaId +
                        " because of a write exception!");
            }
        } else {
            MiniGames.log(Level.WARNING, "Unable to save data for arena with id " + arenaId +
                    " because the arena could not be found!");
        }
    }

    /**
     * Saves all current groups to disk
     */
    public abstract void saveGroups();

    /**
     * Loads all groups from disk
     */
    protected abstract void loadGroups();

    /**
     * Loads all arenas and groups from disk
     */
    public void load() {
        loadArenas();
        loadGroups();
    }

    /**
     * Saves all current arenas to disk
     */
    public abstract void saveArenas();

    /**
     * Loads all arenas from disk
     */
    protected abstract void loadArenas();

}
