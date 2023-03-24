package net.knarcraft.dropper.arena;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry to keep track of which players are playing in which arenas
 */
public class DropperArenaPlayerRegistry {

    private final Map<Player, DropperArenaSession> arenaPlayers = new HashMap<>();

    /**
     * Registers that the given player has started playing the given dropper arena session
     *
     * @param player <p>The player that started playing</p>
     * @param arena  <p>The arena session to register</p>
     */
    public void registerPlayer(@NotNull Player player, @NotNull DropperArenaSession arena) {
        this.arenaPlayers.put(player, arena);
    }

    /**
     * Removes this player from players currently playing
     *
     * @param player <p>The player to remove</p>
     */
    public boolean removePlayer(@NotNull Player player) {
        return this.arenaPlayers.remove(player) != null;
    }

    /**
     * Gets the player's active dropper arena session
     *
     * @param player <p>The player to get arena for</p>
     * @return <p>The player's active arena session, or null if not currently playing</p>
     */
    public @Nullable DropperArenaSession getArenaSession(@NotNull Player player) {
        return this.arenaPlayers.getOrDefault(player, null);
    }

}
