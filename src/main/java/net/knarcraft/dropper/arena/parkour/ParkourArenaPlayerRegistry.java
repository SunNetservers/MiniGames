package net.knarcraft.dropper.arena.parkour;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A registry to keep track of which players are playing in which arenas
 */
public class ParkourArenaPlayerRegistry {

    private final Map<UUID, ParkourArenaSession> arenaPlayers = new HashMap<>();

    /**
     * Registers that the given player has started playing the given parkour arena session
     *
     * @param playerId <p>The id of the player that started playing</p>
     * @param arena    <p>The arena session to register</p>
     */
    public void registerPlayer(@NotNull UUID playerId, @NotNull ParkourArenaSession arena) {
        this.arenaPlayers.put(playerId, arena);
    }

    /**
     * Removes this player from players currently playing
     *
     * @param playerId <p>The id of the player to remove</p>
     */
    public boolean removePlayer(@NotNull UUID playerId) {
        return this.arenaPlayers.remove(playerId) != null;
    }

    /**
     * Gets the player's active parkour arena session
     *
     * @param playerId <p>The id of the player to get arena for</p>
     * @return <p>The player's active arena session, or null if not currently playing</p>
     */
    public @Nullable ParkourArenaSession getArenaSession(@NotNull UUID playerId) {
        return this.arenaPlayers.getOrDefault(playerId, null);
    }

    /**
     * Removes all active sessions for the given arena
     *
     * @param arena <p>The arena to remove sessions for</p>
     */
    public void removeForArena(ParkourArena arena) {
        for (Map.Entry<UUID, ParkourArenaSession> entry : this.arenaPlayers.entrySet()) {
            if (entry.getValue().getArena() == arena) {
                // Kick the player gracefully
                entry.getValue().triggerQuit(false);
                this.arenaPlayers.remove(entry.getKey());
            }
        }
    }

}
