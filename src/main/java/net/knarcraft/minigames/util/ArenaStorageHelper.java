package net.knarcraft.minigames.util;

import net.knarcraft.minigames.MiniGames;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;

public final class ArenaStorageHelper {

    private ArenaStorageHelper() {

    }

    /**
     * Gets the file used to store the given arena id's data
     *
     * @param root    <p>The root directory for the file</p>
     * @param arenaId <p>The id of the arena to get a data file for</p>
     * @return <p>The file the arena's data is/should be stored in</p>
     */
    static @NotNull File getArenaDataFile(File root, @NotNull UUID arenaId) {
        File arenaDataFile = new File(root, arenaId + ".yml");
        if (!root.exists() && !root.mkdirs()) {
            MiniGames.log(Level.SEVERE, "Unable to create the arena data directories");
        }
        return arenaDataFile;
    }

}
