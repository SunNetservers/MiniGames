package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.util.ArenaStorageHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A player registry to keep track of currently playing players
 *
 * @param <K> <p>The type of arena stored</p>
 */
public abstract class AbstractArenaPlayerRegistry<K extends Arena> implements ArenaPlayerRegistry<K> {

    private final Map<UUID, ArenaSession> arenaPlayers = new HashMap<>();
    private final Map<UUID, PlayerEntryState> entryStates = new HashMap<>();

    /**
     * Instantiates a new arena player registry
     */
    public AbstractArenaPlayerRegistry() {
        loadEntryStates();
    }

    @Override
    public @NotNull Set<UUID> getPlayingPlayers() {
        return arenaPlayers.keySet();
    }

    @Override
    public @Nullable PlayerEntryState getEntryState(@NotNull UUID playerId) {
        return this.entryStates.get(playerId);
    }

    @Override
    public void registerPlayer(@NotNull UUID playerId, @NotNull ArenaSession arenaSession) {
        this.arenaPlayers.put(playerId, arenaSession);
        this.entryStates.put(playerId, arenaSession.getEntryState());
        this.saveEntryStates();
    }

    @Override
    public boolean removePlayer(@NotNull UUID playerId, boolean restoreState) {
        // Try and restore the state. If it cannot be restored, retain the entry state
        PlayerEntryState entryState = this.entryStates.remove(playerId);
        if (restoreState) {
            if (entryState.restore()) {
                this.saveEntryStates();
            } else {
                this.entryStates.put(playerId, entryState);
            }
        } else {
            this.saveEntryStates();
        }

        return this.arenaPlayers.remove(playerId) != null;
    }

    @Override
    public @Nullable ArenaSession getArenaSession(@NotNull UUID playerId) {
        return this.arenaPlayers.getOrDefault(playerId, null);
    }

    @Override
    public void removeForArena(K arena, boolean immediately) {
        Set<UUID> removed = new HashSet<>();
        for (Map.Entry<UUID, ArenaSession> entry : this.arenaPlayers.entrySet()) {
            if (entry.getValue().getArena() == arena) {
                // Kick the player gracefully
                entry.getValue().triggerQuit(immediately, false);
                removed.add(entry.getKey());
            }
        }
        removed.forEach(this.arenaPlayers::remove);
    }

    /**
     * Gets a string key unique to this type of player registry
     *
     * @return <p>A unique key used for entry state storage</p>
     */
    protected abstract String getEntryStateStorageKey();

    /**
     * Saves all entry states to disk
     */
    private void saveEntryStates() {
        ArenaStorageHelper.storeArenaPlayerEntryStates(getEntryStateStorageKey(), new HashSet<>(entryStates.values()));
    }

    /**
     * Loads all entry states from disk
     */
    private void loadEntryStates() {
        this.entryStates.clear();
        Set<PlayerEntryState> entryStates = ArenaStorageHelper.getArenaPlayerEntryStates(getEntryStateStorageKey());
        for (PlayerEntryState entryState : entryStates) {
            this.entryStates.put(entryState.getPlayerId(), entryState);
        }
        if (!this.entryStates.isEmpty()) {
            MiniGames.log(Level.WARNING, entryStates.size() + " un-exited sessions found. This happens if " +
                    "players leave in the middle of a game, or if the server crashes. MiniGames will do its best " +
                    "to fix the players' states.");
        }
    }

}
