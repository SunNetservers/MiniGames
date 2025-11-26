package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.minigames.arena.AbstractArenaPlayerRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * A registry to keep track of which players are playing in which arenas
 */
public class DropperArenaPlayerRegistry extends AbstractArenaPlayerRegistry<DropperArena> {

    @Override
    @NotNull
    protected String getEntryStateStorageKey() {
        return "dropper";
    }

}
