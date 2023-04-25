package net.knarcraft.minigames.arena;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A player registry to keep track of currently playing players
 *
 * @param <K> <p>The type of arena stored</p>
 */
public abstract class AbstractArenaPlayerRegistry<K extends Arena> implements ArenaPlayerRegistry<K> {

    private final Map<UUID, ArenaSession> arenaPlayers = new HashMap<>();
    private final Map<UUID, PlayerEntryState> entryStates = new HashMap<>();

    //TODO: Save all entry states each time the map changes
    //TODO: If a player joins, and their entry state exists, restore the state

    @Override
    public void registerPlayer(@NotNull UUID playerId, @NotNull ArenaSession arenaSession) {
        this.arenaPlayers.put(playerId, arenaSession);
        this.entryStates.put(playerId, arenaSession.getEntryState());
    }

    @Override
    public boolean removePlayer(@NotNull UUID playerId) {
        this.entryStates.remove(playerId);
        return this.arenaPlayers.remove(playerId) != null;
    }

    @Override
    public @Nullable ArenaSession getArenaSession(@NotNull UUID playerId) {
        return this.arenaPlayers.getOrDefault(playerId, null);
    }

    @Override
    public void removeForArena(K arena, boolean immediately) {
        for (Map.Entry<UUID, ArenaSession> entry : this.arenaPlayers.entrySet()) {
            if (entry.getValue().getArena() == arena) {
                // Kick the player gracefully
                entry.getValue().triggerQuit(immediately);
                this.arenaPlayers.remove(entry.getKey());
                this.entryStates.remove(entry.getKey());
            }
        }
    }

}
