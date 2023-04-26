package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.minigames.arena.AbstractArenaPlayerRegistry;

/**
 * A registry to keep track of which players are playing in which arenas
 */
public class ParkourArenaPlayerRegistry extends AbstractArenaPlayerRegistry<ParkourArena> {

    @Override
    protected String getEntryStateStorageKey() {
        return "parkour";
    }

}
