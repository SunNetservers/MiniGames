package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.util.ArenaStorageHelper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A handler that keeps track of all dropper arenas
 */
public class DropperArenaHandler {

    private Map<UUID, DropperArena> arenas = new HashMap<>();
    private final Map<Player, Integer> stagesCleared = new HashMap<>();

    /**
     * Tries to register the given stage as cleared
     *
     * @param player <p>The player that cleared a stage</p>
     * @param stage  <p>The stage the player cleared</p>
     * @return <p>True if the player cleared a new stage</p>
     */
    public boolean registerStageCleared(@NotNull Player player, int stage) {
        if ((!stagesCleared.containsKey(player) && stage == 1) || (stagesCleared.containsKey(player) &&
                stage == stagesCleared.get(player) + 1)) {
            stagesCleared.put(player, stage);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds a new arena
     *
     * @param arena <p>The arena to add</p>
     */
    public void addArena(@NotNull DropperArena arena) {
        this.arenas.put(arena.getArenaId(), arena);
        this.saveArenas();
    }

    /**
     * Gets the arena with the given name
     *
     * @param arenaName <p>The arena to get</p>
     * @return <p>The arena with the given name, or null if not found</p>
     */
    public @Nullable DropperArena getArena(@NotNull String arenaName) {
        arenaName = ArenaStorageHelper.sanitizeArenaName(arenaName);
        for (DropperArena arena : arenas.values()) {
            if (ArenaStorageHelper.sanitizeArenaName(arena.getArenaName()).equals(arenaName)) {
                return arena;
            }
        }
        return null;
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
    }

}
