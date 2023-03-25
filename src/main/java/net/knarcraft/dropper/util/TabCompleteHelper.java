package net.knarcraft.dropper.util;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper-class for common tab-completions
 */
public final class TabCompleteHelper {

    private TabCompleteHelper() {

    }

    /**
     * Gets the names of all current arenas
     *
     * @return <p>All arena names</p>
     */
    public static @NotNull List<String> getArenas() {
        List<String> arenaNames = new ArrayList<>();
        for (DropperArena dropperArena : Dropper.getInstance().getArenaHandler().getArenas()) {
            arenaNames.add(dropperArena.getArenaName());
        }
        return arenaNames;
    }

}
