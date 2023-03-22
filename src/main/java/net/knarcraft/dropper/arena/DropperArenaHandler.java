package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.util.ArenaStorageHelper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private final Map<Player, DropperArena> arenaPlayers = new HashMap<>();

    /**
     * Registers that the given player has started playing in the given dropper arena
     *
     * @param player <p>The player that started playing</p>
     * @param arena  <p>The arena the player is playing in</p>
     */
    public void registerPlayer(@NotNull Player player, @NotNull DropperArena arena) {
        this.arenaPlayers.put(player, arena);
    }

    /**
     * Removes this player from players currently playing
     *
     * @param player <p>The player to remove</p>
     */
    public void removePlayer(@NotNull Player player) {
        this.arenaPlayers.remove(player);
    }

    /**
     * Gets whether the given player is currently playing in an arena
     *
     * @param player <p>The player to check</p>
     * @return <p>True if the player is currently in an arena</p>
     */
    public boolean isInArena(@NotNull Player player) {
        return getArena(player) != null;
    }

    /**
     * Gets the arena the given player is currently playing in
     *
     * @param player <p>The player to get arena for</p>
     * @return <p>The player's active arena, or null if not currently playing</p>
     */
    public @Nullable DropperArena getArena(@NotNull Player player) {
        return this.arenaPlayers.getOrDefault(player, null);
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
