package net.knarcraft.minigames.arena;

/**
 * A registry keeping track of all player sessions for some arenas
 *
 * @param <K> <p>The type of arena this registry stores</p>
 */
public interface ArenaPlayerRegistry<K extends Arena> {

    /**
     * Removes all active sessions for the given arena
     *
     * @param arena       <p>The arena to remove sessions for</p>
     * @param immediately <p>Whether to immediately teleport the player</p>
     */
    void removeForArena(K arena, boolean immediately);

}
