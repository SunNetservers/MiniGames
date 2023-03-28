package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.util.ArenaStorageHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A handler that keeps track of all dropper arenas
 */
public class DropperArenaHandler {

    private Map<UUID, DropperArena> arenas = new HashMap<>();
    private Map<UUID, DropperArenaGroup> arenaGroups = new HashMap<>();
    private Map<String, UUID> arenaNameLookup = new HashMap<>();

    /**
     * Gets the group the given arena belongs to
     *
     * @param arenaId <p>The id of the arena to get the group of</p>
     * @return <p>The group the arena belongs to, or null if not in a group</p>
     */
    public @Nullable DropperArenaGroup getGroup(@NotNull UUID arenaId) {
        return this.arenaGroups.get(arenaId);
    }

    /**
     * Sets the group for the given arena
     *
     * @param arenaId    <p>The id of the arena to change</p>
     * @param arenaGroup <p>The group to add the arena to</p>
     */
    public void setGroup(@NotNull UUID arenaId, @NotNull DropperArenaGroup arenaGroup) {
        this.arenaGroups.put(arenaId, arenaGroup);
        arenaGroup.addArena(arenaId);
        saveGroups();
    }

    /**
     * Gets the dropper arena group with the given name
     *
     * @param groupName <p>The name of the group to get</p>
     * @return <p>The group, or null if not found</p>
     */
    public @Nullable DropperArenaGroup getGroup(String groupName) {
        String sanitized = ArenaStorageHelper.sanitizeArenaName(groupName);
        for (DropperArenaGroup arenaGroup : this.arenaGroups.values()) {
            if (arenaGroup.getGroupNameSanitized().equals(sanitized)) {
                return arenaGroup;
            }
        }
        return null;
    }

    /**
     * Removes the given arena from its group
     *
     * @param arenaId <p>The id of the arena to ungroup</p>
     */
    public void removeFromGroup(@NotNull UUID arenaId) {
        if (this.arenaGroups.containsKey(arenaId)) {
            this.arenaGroups.get(arenaId).removeArena(arenaId);
            this.arenaGroups.remove(arenaId);
            saveGroups();
        }
    }

    /**
     * Adds a new arena
     *
     * @param arena <p>The arena to add</p>
     */
    public void addArena(@NotNull DropperArena arena) {
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
    public @Nullable DropperArena getArena(@NotNull UUID arenaId) {
        return this.arenas.get(arenaId);
    }

    /**
     * Gets the arena with the given name
     *
     * @param arenaName <p>The arena to get</p>
     * @return <p>The arena with the given name, or null if not found</p>
     */
    public @Nullable DropperArena getArena(@NotNull String arenaName) {
        return this.arenas.get(this.arenaNameLookup.get(ArenaStorageHelper.sanitizeArenaName(arenaName)));
    }

    /**
     * Gets all known arenas
     *
     * @return <p>All known arenas</p>
     */
    public @NotNull Map<UUID, DropperArena> getArenas() {
        return new HashMap<>(this.arenas);
    }

    /**
     * Removes the given arena
     *
     * @param arena <p>The arena to remove</p>
     */
    public void removeArena(@NotNull DropperArena arena) {
        Dropper.getInstance().getPlayerRegistry().removeForArena(arena);
        this.arenas.remove(arena.getArenaId());
        this.arenaNameLookup.remove(arena.getArenaNameSanitized());
        this.arenaGroups.remove(arena.getArenaId());
        this.saveArenas();
    }

    /**
     * Stores the data for the given arena
     *
     * @param arenaId <p>The id of the arena whose data should be saved</p>
     */
    public void saveData(UUID arenaId) {
        try {
            ArenaStorageHelper.saveArenaData(this.arenas.get(arenaId).getData());
        } catch (IOException e) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Unable to save arena data! Data loss can occur!");
            Dropper.getInstance().getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Saves all current dropper groups to disk
     */
    public void saveGroups() {
        try {
            ArenaStorageHelper.saveDropperArenaGroups((Set<DropperArenaGroup>) this.arenaGroups.values());
        } catch (IOException e) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Unable to save current arena groups! " +
                    "Data loss can occur!");
            Dropper.getInstance().getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Loads all dropper groups from disk
     */
    public void loadGroups() {
        Set<DropperArenaGroup> arenaGroups = ArenaStorageHelper.loadDropperArenaGroups();
        Map<UUID, DropperArenaGroup> arenaGroupMap = new HashMap<>();
        for (DropperArenaGroup arenaGroup : arenaGroups) {
            for (UUID arenaId : arenaGroup.getArenas()) {
                arenaGroupMap.put(arenaId, arenaGroup);
            }
        }
        this.arenaGroups = arenaGroupMap;
    }

    /**
     * Saves all current arenas to disk
     */
    public void saveArenas() {
        try {
            ArenaStorageHelper.saveArenas(this.arenas);
        } catch (IOException e) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Unable to save current arenas! " +
                    "Data loss can occur!");
            Dropper.getInstance().getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Loads all arenas from disk
     */
    public void loadArenas() {
        this.arenas = ArenaStorageHelper.loadArenas();

        // Save a map from arena name to arena id for improved performance
        this.arenaNameLookup = new HashMap<>();
        for (Map.Entry<UUID, DropperArena> arena : this.arenas.entrySet()) {
            String sanitizedName = arena.getValue().getArenaNameSanitized();
            this.arenaNameLookup.put(sanitizedName, arena.getKey());
        }
    }

}
