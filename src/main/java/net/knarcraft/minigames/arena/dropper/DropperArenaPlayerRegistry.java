package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.minigames.arena.ArenaPlayerRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A registry to keep track of which players are playing in which arenas
 */
public class DropperArenaPlayerRegistry implements ArenaPlayerRegistry<DropperArena> {

    private final Map<UUID, DropperArenaSession> arenaPlayers = new HashMap<>();

    /**
     * Registers that the given player has started playing the given dropper arena session
     *
     * @param playerId <p>The id of the player that started playing</p>
     * @param arena    <p>The arena session to register</p>
     */
    public void registerPlayer(@NotNull UUID playerId, @NotNull DropperArenaSession arena) {
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
     * Gets the player's active dropper arena session
     *
     * @param playerId <p>The id of the player to get arena for</p>
     * @return <p>The player's active arena session, or null if not currently playing</p>
     */
    public @Nullable DropperArenaSession getArenaSession(@NotNull UUID playerId) {
        return this.arenaPlayers.getOrDefault(playerId, null);
    }

    @Override
    public void removeForArena(DropperArena arena, boolean immediately) {
        for (Map.Entry<UUID, DropperArenaSession> entry : this.arenaPlayers.entrySet()) {
            if (entry.getValue().getArena() == arena) {
                // Kick the player gracefully
                entry.getValue().triggerQuit(immediately);
                this.arenaPlayers.remove(entry.getKey());
            }
        }
    }

}
