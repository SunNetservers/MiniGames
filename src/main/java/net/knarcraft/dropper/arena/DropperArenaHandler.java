package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.util.ArenaStorageHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DropperArenaHandler {

    List<DropperArena> arenas = new ArrayList<>();

    private static final File arenaFile = new File(Dropper.getInstance().getDataFolder(), "arenas.yml");

    //TODO: Use this class to keep track of all created arenas. Saving and loading arenas is this class's responsibility
    //TODO: Keep track of which players are in which arenas (should possibly be its own class, depending on complexity)

    /**
     * Saves all current arenas to disk
     */
    private void saveArenas() {
        try {
            ArenaStorageHelper.saveArenas(this.arenas, arenaFile);
        } catch (IOException e) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Unable to save current arenas! " +
                    "Data loss can occur!");
        }
    }

    /**
     * Loads all arenas from disk
     */
    private void loadArenas() {
        this.arenas = ArenaStorageHelper.loadArenas(arenaFile);
    }

}
