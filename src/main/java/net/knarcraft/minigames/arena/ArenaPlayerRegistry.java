package net.knarcraft.minigames.arena;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * A registry keeping track of all player sessions for some arenas
 *
 * @param <K> <p>The type of arena this registry stores</p>
 */
public interface ArenaPlayerRegistry<K extends Arena> {

    /**
     * Registers that the given player has started playing the given dropper arena session
     *
     * @param playerId     <p>The id of the player that started playing</p>
     * @param arenaSession <p>The arena session to register</p>
     */
    void registerPlayer(@NotNull UUID playerId, @NotNull ArenaSession arenaSession);

    /**
     * Removes this player from players currently playing
     *
     * @param playerId <p>The id of the player to remove</p>
     */
    boolean removePlayer(@NotNull UUID playerId);

    /**
     * Gets the player's active dropper arena session
     *
     * @param playerId <p>The id of the player to get arena for</p>
     * @return <p>The player's active arena session, or null if not currently playing</p>
     */
    @Nullable ArenaSession getArenaSession(@NotNull UUID playerId);

    /**
     * Removes all active sessions for the given arena
     *
     * @param arena       <p>The arena to remove sessions for</p>
     * @param immediately <p>Whether to immediately teleport the player</p>
     */
    void removeForArena(K arena, boolean immediately);

}
