package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.util.ArenaStorageHelper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * A handler that keeps track of all dropper arenas
 */
public class DropperArenaHandler {

    private static final File arenaFile = new File(Dropper.getInstance().getDataFolder(), "arenas.yml");

    private List<DropperArena> arenas = new ArrayList<>();
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
        this.arenas.add(arena);
        this.saveArenas();
    }

    /**
     * Gets all known arenas
     *
     * @return <p>All known arenas</p>
     */
    public @NotNull List<DropperArena> getArenas() {
        return new ArrayList<>(this.arenas);
    }

    /**
     * Removes the given arena
     *
     * @param arena <p>The arena to remove</p>
     */
    public void removeArena(@NotNull DropperArena arena) {
        this.arenas.remove(arena);
        this.saveArenas();
    }

    /**
     * Saves all current arenas to disk
     */
    public void saveArenas() {
        try {
            ArenaStorageHelper.saveArenas(this.arenas, arenaFile);
        } catch (IOException e) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Unable to save current arenas! " +
                    "Data loss can occur!");
        }
    }

    /**
     * Loads all arenas from disk
     */
    public void loadArenas() {
        this.arenas = ArenaStorageHelper.loadArenas(arenaFile);
    }

}
