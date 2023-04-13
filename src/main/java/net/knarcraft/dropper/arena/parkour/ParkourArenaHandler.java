package net.knarcraft.dropper.arena.parkour;

import net.knarcraft.dropper.MiniGames;
import net.knarcraft.dropper.util.ArenaStorageHelper;
import net.knarcraft.dropper.util.StringSanitizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A handler that keeps track of all dropper arenas
 */
public class ParkourArenaHandler {

    private Map<UUID, ParkourArena> arenas = new HashMap<>();
    private Map<UUID, ParkourArenaGroup> arenaGroups = new HashMap<>();
    private Map<String, UUID> arenaNameLookup = new HashMap<>();

    /**
     * Gets all arenas that are within a group
     *
     * @return <p>All arenas in a group</p>
     */
    public @NotNull Set<ParkourArena> getArenasInAGroup() {
        Set<ParkourArena> arenas = new HashSet<>();
        for (UUID arenaId : arenaGroups.keySet()) {
            arenas.add(this.arenas.get(arenaId));
        }
        return arenas;
    }

    /**
     * Gets a copy of all dropper groups
     *
     * @return <p>All dropper groups</p>
     */
    public Set<ParkourArenaGroup> getAllGroups() {
        return new HashSet<>(arenaGroups.values());
    }

    /**
     * Gets the group the given arena belongs to
     *
     * @param arenaId <p>The id of the arena to get the group of</p>
     * @return <p>The group the arena belongs to, or null if not in a group</p>
     */
    public @Nullable ParkourArenaGroup getGroup(@NotNull UUID arenaId) {
        return this.arenaGroups.get(arenaId);
    }

    /**
     * Sets the group for the given arena
     *
     * @param arenaId    <p>The id of the arena to change</p>
     * @param arenaGroup <p>The group to add the arena to, or null to remove the current group</p>
     */
    public void setGroup(@NotNull UUID arenaId, @Nullable ParkourArenaGroup arenaGroup) {
        if (arenaGroup == null) {
            // No need to remove something non-existing
            if (!this.arenaGroups.containsKey(arenaId)) {
                return;
            }

            // Remove the existing group
            ParkourArenaGroup oldGroup = this.arenaGroups.remove(arenaId);
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
     * Gets the dropper arena group with the given name
     *
     * @param groupName <p>The name of the group to get</p>
     * @return <p>The group, or null if not found</p>
     */
    public @Nullable ParkourArenaGroup getGroup(String groupName) {
        String sanitized = StringSanitizer.sanitizeArenaName(groupName);
        for (ParkourArenaGroup arenaGroup : this.arenaGroups.values()) {
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
    public void addArena(@NotNull ParkourArena arena) {
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
    public @Nullable ParkourArena getArena(@NotNull UUID arenaId) {
        return this.arenas.get(arenaId);
    }

    /**
     * Gets the arena with the given name
     *
     * @param arenaName <p>The arena to get</p>
     * @return <p>The arena with the given name, or null if not found</p>
     */
    public @Nullable ParkourArena getArena(@NotNull String arenaName) {
        return this.arenas.get(this.arenaNameLookup.get(StringSanitizer.sanitizeArenaName(arenaName)));
    }

    /**
     * Gets all known arenas
     *
     * @return <p>All known arenas</p>
     */
    public @NotNull Map<UUID, ParkourArena> getArenas() {
        return new HashMap<>(this.arenas);
    }

    /**
     * Removes the given arena
     *
     * @param arena <p>The arena to remove</p>
     */
    public void removeArena(@NotNull ParkourArena arena) {
        UUID arenaId = arena.getArenaId();
        MiniGames.getInstance().getParkourArenaPlayerRegistry().removeForArena(arena);
        this.arenas.remove(arenaId);
        this.arenaNameLookup.remove(arena.getArenaNameSanitized());
        this.arenaGroups.remove(arenaId);
        if (!ArenaStorageHelper.removeDropperArenaData(arenaId)) {
            MiniGames.log(Level.WARNING, "Unable to remove dropper arena data file " + arenaId + ".yml. " +
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
        try {
            ArenaStorageHelper.saveParkourArenaData(this.arenas.get(arenaId).getData());
        } catch (IOException e) {
            MiniGames.log(Level.SEVERE, "Unable to save arena data! Data loss can occur!");
            MiniGames.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Saves all current dropper groups to disk
     */
    public void saveGroups() {
        try {
            ArenaStorageHelper.saveParkourArenaGroups(new HashSet<>(this.arenaGroups.values()));
        } catch (IOException e) {
            MiniGames.log(Level.SEVERE, "Unable to save current arena groups! " +
                    "Data loss can occur!");
            MiniGames.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Loads all arenas and groups from disk
     */
    public void load() {
        loadArenas();
        loadGroups();
    }

    /**
     * Loads all dropper groups from disk
     */
    private void loadGroups() {
        Set<ParkourArenaGroup> arenaGroups = ArenaStorageHelper.loadParkourArenaGroups();
        Map<UUID, ParkourArenaGroup> arenaGroupMap = new HashMap<>();
        for (ParkourArenaGroup arenaGroup : arenaGroups) {
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
            ArenaStorageHelper.saveParkourArenas(this.arenas);
        } catch (IOException e) {
            MiniGames.log(Level.SEVERE, "Unable to save current arenas! " +
                    "Data loss can occur!");
            MiniGames.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Loads all arenas from disk
     */
    private void loadArenas() {
        this.arenas = ArenaStorageHelper.loadParkourArenas();

        // Save a map from arena name to arena id for improved performance
        this.arenaNameLookup = new HashMap<>();
        for (Map.Entry<UUID, ParkourArena> arena : this.arenas.entrySet()) {
            String sanitizedName = arena.getValue().getArenaNameSanitized();
            this.arenaNameLookup.put(sanitizedName, arena.getKey());
        }
    }

}
